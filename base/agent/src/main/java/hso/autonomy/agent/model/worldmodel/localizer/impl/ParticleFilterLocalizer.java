/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.agentmodel.ICamera;
import hso.autonomy.agent.model.agentmodel.IOdometryErrorModel;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.agent.model.agentmodel.impl.Odometry;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizationInfo;
import hso.autonomy.agent.model.worldmodel.localizer.IParticleFilterLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IWeightedParticle;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class ParticleFilterLocalizer implements IParticleFilterLocalizer
{
	/** The internal state of the localizer. */
	protected LocalizationInfo state;

	/** The number of particles to use for this localizer */
	protected final int nrOfParticles;

	/** calculates the current std-dev of the particles, used for particleGauss */
	private StandardDeviation standardDeviationCalculator;

	/** current best particle, will be updated each successful correction step */
	protected int indexOfBestCandidate;

	/** array of the current probabilities from the particles */
	private double[] probabilities;

	/** current maximal probability from the "best" particle */
	private double curMaxProb;

	/** current standard deviation of the probabilities */
	private double curSigma;

	/** percentage of how many "plausible" particles should be at less inside the container */
	protected double resamplingThreshold;

	private Angle lastReceivedZAngle;

	public Map<String, ISensor> sensors;

	protected List<IWeightedParticle> particles;

	protected ICamera camera;

	/** allowed standard deviation for observations (degrees) */
	private final double observationsSigma;

	private IOdometryErrorModel odometryErrorModel;

	public ParticleFilterLocalizer(Map<String, ISensor> sensors, int nrOfParticles)
	{
		this.nrOfParticles = nrOfParticles;
		this.resamplingThreshold = 0.98;
		this.observationsSigma = 5;
		state = new LocalizationInfo();
		particles = new ArrayList<>();
		this.standardDeviationCalculator = new StandardDeviation();
		this.lastReceivedZAngle = Angle.ZERO;
		this.sensors = sensors;
		initializeSensors();
	}

	protected void initializeSensors()
	{
		// TODO: replace this with a non-static solution
		if (sensors.containsKey("Odometry")) {
			Odometry odometry = (Odometry) sensors.get("Odometry");
			this.odometryErrorModel = odometry.getErrorModel();
		}

		if (sensors.containsKey("Camera")) {
			this.camera = (ICamera) sensors.get("Camera");
		}
	}

	@Override
	public ILocalizationInfo getState()
	{
		return state;
	}

	@Override
	public Map<String, ISensor> getSensors()
	{
		return sensors;
	}

	@Override
	public void reset(float globalTime, IPose3D initialPoseEstimation)
	{
		state.set(globalTime, initialPoseEstimation);
		particles.clear();

		initializeParticles(nrOfParticles, initialPoseEstimation);
	}

	@Override
	public void initializeParticles(int nrOfParticles, IPose3D initialPoseEstimation)
	{
		// add pose estimation also as particle
		particles.add(new WeightedParticle(initialPoseEstimation, 0));

		// extract 2d pose to generate particles
		double posX = initialPoseEstimation.getX();
		double posY = initialPoseEstimation.getY();
		Angle theta = initialPoseEstimation.getHorizontalAngle();

		// then generate some random particles around the pose estimation
		Random r = new Random();
		for (int i = 0; i < nrOfParticles; i++) {
			double randomX = r.nextGaussian() + posX;
			double randomY = r.nextGaussian() + posY;
			Angle randomTheta = Angle.deg(r.nextGaussian() * 90 + theta.degrees());

			Vector3D randomPosition = new Vector3D(randomX, randomY, initialPoseEstimation.getZ());
			particles.add(new WeightedParticle(new Pose3D(randomPosition, randomTheta), 0));
		}
		this.indexOfBestCandidate = 0;
	}

	@Override
	public boolean predict(float globalTime, IPose3D odometry, Rotation orientationEstimation, double cameraHeight)
	{
		Vector3D localizedPosition = state.getLocalizedPosition();
		Rotation localizedOrientation = state.getLocalizedOrientation();

		if (odometry != null) {
			Vector2D odomTrans = VectorUtils.to2D(odometry.getPosition());

			// calculate deltaZ between last update and this one
			Angle currentZAngle = Geometry.getHorizontalAngle(orientationEstimation);
			Angle deltaZAngle = currentZAngle.subtract(lastReceivedZAngle);
			lastReceivedZAngle = currentZAngle;

			// Remove Z rotation from orientation estimation, since this is represented by the particle
			double[] angles = orientationEstimation.getAngles(RotationOrder.ZYX, RotationConvention.VECTOR_OPERATOR);
			orientationEstimation =
					new Rotation(RotationOrder.ZYX, RotationConvention.VECTOR_OPERATOR, 0, angles[1], angles[2]);

			// update the particles with new odometry information
			for (IWeightedParticle particle : particles) {
				if (odometryErrorModel != null) {
					odomTrans = odometryErrorModel.applyNoiseToTrans(odomTrans);
					deltaZAngle = odometryErrorModel.applyNoiseToRotation(deltaZAngle);
				}
				particle.updatePose(new Vector3D(odomTrans.getX(), odomTrans.getY(), cameraHeight), deltaZAngle,
						orientationEstimation);
			}

			// use the pose of the best candidate since the last correction step
			IPose3D bestCandidatePose = getBestCandidate().getPose();
			localizedPosition = bestCandidatePose.getPosition();
			localizedOrientation = bestCandidatePose.getOrientation();
		} else if (orientationEstimation != null) {
			localizedOrientation = getBestCandidate().getPose().getOrientation();
		} else {
			return false;
		}

		state.set(globalTime, localizedPosition, localizedOrientation);
		return true;
	}

	@Override
	public boolean correct(float globalTime, IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation, double cameraHeight)
	{
		// update particles with most recent orientation estimation (received from vision instead of odometry)
		//		for (IWeightedParticle particle : particles) {
		//						particle.updatePose(new Vector3D(0, 0, cameraHeight), orientationEstimation);
		//		}

		// first try to calculate the new weight by camera observations
		double normFactor = 0.0;
		if (!pointObservations.isEmpty()) {
			normFactor = sampleImportanceFromObservations(pointObservations, map);
		}

		// then try to add additional weight if the map can be used
		double mapNormFactor = sampleImportanceFromMap(map);
		if (!Double.isNaN(mapNormFactor)) {
			normFactor = mapNormFactor;
		}
		normalizeImportance(normFactor);
		// only execute resampling when it contains less then threshold "good" particles
		if (PFImportanceEvaluator.isResamplingRequired(probabilities, resamplingThreshold)) {
			resampleParticles();
		}

		updateBestCandidate();

		IWeightedParticle candidate = getBestCandidate();
		if (candidate != null) {
			state.set(globalTime, candidate.getPose());
			return true;
		}
		return false;
	}

	@Override
	public double sampleImportanceFromObservations(List<IPointFeatureObservation> pointObservations, IFeatureMap map)
	{
		double probabilitySum = 0.0;
		for (int i = 0; i < particles.size(); i++) {
			IWeightedParticle particle = particles.get(i);
			List<Double> alphaDiffs = new ArrayList<>();
			List<Double> deltaDiffs = new ArrayList<>();
			// 1. try to match known to see by finding those with the closest angle deviation
			for (IPointFeatureObservation pointObservation : pointObservations) {
				String currentType = pointObservation.getType();
				Collection<IPointFeature> knownFeaturesByType = map.getPointFeatures(currentType);
				//				Collection<IPointFeature> knownFeaturesByType =
				// LocalizerUtil.extractPlausiblePointFeatures(
				//						map, currentType, particle.getPose(), camera.getFoV(), 10);

				Angle closestAlphaDiff = Angle.deg(179);
				Angle closestDeltaDiff = Angle.deg(179);
				double seenAlpha = pointObservation.getObservedPosition().getAlpha();
				double seenDelta = pointObservation.getObservedPosition().getDelta();
				IPointFeature closestFeature = null;
				for (IPointFeature knownFeature : knownFeaturesByType) {
					Vector3D localPosition = particle.getPose().applyInverseTo(knownFeature.getKnownPosition());
					double knownAlpha = localPosition.getAlpha();
					double knownDelta = localPosition.getDelta();
					Angle curAlphaDiff = Angle.rad(seenAlpha - knownAlpha);
					Angle curDeltaDiff = Angle.rad(seenDelta - knownDelta);
					if (Math.abs(curAlphaDiff.degrees()) <= Math.abs(closestAlphaDiff.degrees()) &&
							Math.abs(curDeltaDiff.degrees()) <= Math.abs(closestDeltaDiff.degrees())) {
						closestFeature = knownFeature;
						closestAlphaDiff = Angle.deg(Math.abs(curAlphaDiff.degrees()));
						closestDeltaDiff = Angle.deg(Math.abs(curDeltaDiff.degrees()));
					}
				}

				// TODO: use correspondences for matching here
				if (closestFeature != null) {
					alphaDiffs.add(closestAlphaDiff.degrees());
					deltaDiffs.add(closestDeltaDiff.degrees());
					knownFeaturesByType.remove(closestFeature);
				}
			}

			if (!alphaDiffs.isEmpty()) {
				double newWeight = PFImportanceEvaluator.calculateImportance(alphaDiffs, observationsSigma, 0);
				newWeight += PFImportanceEvaluator.calculateImportance(deltaDiffs, observationsSigma, 0);
				particle.setProbability(newWeight);
			}
			probabilitySum += particle.getProbability();
		}
		return probabilitySum;
	}

	@Override
	public double sampleImportanceFromMap(IFeatureMap map)
	{
		// calculating weights based on map needs domain-specific information, so this base localizer doesn't support it
		return Double.NaN;
	}

	@Override
	public void normalizeImportance(double normFactor)
	{
		curMaxProb = 0.0;
		// normalize the probabilities
		this.probabilities = new double[particles.size()];
		for (int i = 0; i < particles.size(); i++) {
			IWeightedParticle particle = particles.get(i);
			double normalizedProbability = particle.getProbability() / normFactor;
			if (normalizedProbability > curMaxProb) {
				curMaxProb = normalizedProbability;
			}
			particle.setProbability(normalizedProbability);
			probabilities[i] = particle.getProbability();
		}

		curSigma = standardDeviationCalculator.evaluate(probabilities);
	}

	@Override
	public void resampleParticles()
	{
		if (Double.isNaN(curSigma) || curSigma <= 1.0 / particles.size()) {
			return;
		}

		//		System.out.println("Execute Resampling (Sigma): " + curSigma);
		// implementation of the "Low-Variance-Resampling-Algorithm"
		List<IWeightedParticle> newParticles = new ArrayList<>();

		double inverseSize = 1.0 / particles.size();
		double r = Math.random() * (inverseSize);
		double c = particles.get(0).getProbability();
		int i = 0;
		int lastParticleIndex = i;

		for (int m = 0; m < particles.size(); m++) {
			double u = r + (m - 1) * inverseSize;
			for (int j = i; j < particles.size(); j++) {
				if (u <= c) {
					break;
				}
				c += particles.get(j).getProbability();
				i = j;
			}

			IWeightedParticle newParticle = pickParticleForResampling(i, i == lastParticleIndex);
			newParticles.add(newParticle);
			lastParticleIndex = i;
		}

		particles.clear();
		particles.addAll(newParticles);
	}

	protected IWeightedParticle pickParticleForResampling(int index, boolean sameParticle)
	{
		//		if (!sameParticle) {
		return new WeightedParticle(particles.get(index));
		//		}
		//		return addRandomParticle(particles.get(index));
	}

	@Override
	public IWeightedParticle addRandomParticle(IWeightedParticle particle)
	{
		IPose3D particlePose = particle.getPose();
		double newX = Math.random() * 0.5 + particlePose.getX();
		double newY = Math.random() * 0.5 + particlePose.getY();
		IPose3D newPose = new Pose3D(newX, newY, particlePose.getHorizontalAngle());
		return new WeightedParticle(newPose, particle.getProbability());
	}

	@Override
	public void updateBestCandidate()
	{
		// Update the gaussian to fit the particles after resampling

		Mean meanEval = new Mean();
		double meanProb = meanEval.evaluate(probabilities);
		double maxProb = 0.0;
		for (int i = 0; i < particles.size(); i++) {
			IWeightedParticle particle = particles.get(i);
			double curProb = PFImportanceEvaluator.gauss(particle.getProbability(), meanProb, curSigma);
			if (curProb > maxProb) {
				maxProb = curProb;
				this.indexOfBestCandidate = i;
			}
		}
	}

	private IWeightedParticle getBestCandidate()
	{
		return particles.get(indexOfBestCandidate);
	}

	public List<IWeightedParticle> getParticles()
	{
		return particles;
	}

	public double getCurMaxProb()
	{
		return curMaxProb;
	}
}
