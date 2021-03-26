/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer.impl;

import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureMap;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.ILocalizationInfo;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPositionCalculator;
import hso.autonomy.util.geometry.IPose3D;
import java.util.List;
import java.util.Map;
import org.apache.commons.math3.filter.DefaultMeasurementModel;
import org.apache.commons.math3.filter.DefaultProcessModel;
import org.apache.commons.math3.filter.KalmanFilter;
import org.apache.commons.math3.filter.MeasurementModel;
import org.apache.commons.math3.filter.ProcessModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

/**
 * Kalman Filter localizer component.
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public class KalmanLocalizer implements IFeatureLocalizer
{
	private KalmanFilter filter;

	private IPositionCalculator positionCalculator;

	private LocalizationInfo state;

	private double resetDistance;

	protected Map<String, ISensor> sensors;

	/**
	 *
	 * @param positionCalculator instance calculating a position from sensor input
	 * @param resetDistance the boundary distance of state and measured position at which the filter is reset, -1 if no
	 * reset is desired due to distance
	 */
	public KalmanLocalizer(IPositionCalculator positionCalculator, double resetDistance)
	{
		this.positionCalculator = positionCalculator;
		this.resetDistance = resetDistance;
		this.state = new LocalizationInfo();

		init(Vector3D.ZERO);
	}

	private void init(Vector3D realPos)
	{
		// A = state transition matrix for state {x, y, z}
		RealMatrix A = new Array2DRowRealMatrix(new double[][] {
				{1, 0, 0}, //
				{0, 1, 0}, //
				{0, 0, 1}, //
		});
		// B = control input matrix
		RealMatrix B = new Array2DRowRealMatrix(new double[][] {
				{1, 0, 0}, //
				{0, 1, 0}, //
				{0, 0, 1}, //
		});

		// H = measurement matrix
		RealMatrix H = new Array2DRowRealMatrix(new double[][] {
				{1, 0, 0}, //
				{0, 1, 0}, //
				{0, 0, 1}, //
		});
		// x = initial guess
		double[] pos = new double[] {realPos.getX(), realPos.getY(), realPos.getZ()};
		RealVector x = new ArrayRealVector(pos);

		// Q = estimate of process error
		double processNoise = 0.001;
		RealMatrix Q = new Array2DRowRealMatrix(new double[][] {
				{processNoise, 0, 0}, //
				{0, processNoise, 0}, //
				{0, 0, processNoise}, //
		});

		// P0 = initial guess of covariance matrix
		RealMatrix P0 = new Array2DRowRealMatrix(new double[][] {
				//
				{1, 0, 0}, //
				{0, 1, 0}, //
				{0, 0, 1}, //
		});
		// R = estimate of measurement noise
		// position measurement noise (meter)
		double mnoise = 0.1;
		RealMatrix R = new Array2DRowRealMatrix(new double[][] {
				{mnoise, 0, 0}, //
				{0, mnoise, 0}, //
				{0, 0, mnoise}, //
		});

		ProcessModel pm = new DefaultProcessModel(A, B, Q, x, P0);
		MeasurementModel mm = new DefaultMeasurementModel(H, R);
		filter = new KalmanFilter(pm, mm);
	}

	@Override
	public ILocalizationInfo getState()
	{
		return state;
	}

	@Override
	public void reset(float globalTime, IPose3D initialPoseEstimation)
	{
		init(initialPoseEstimation.getPosition());
		updateState(globalTime, initialPoseEstimation.getOrientation());
	}

	@Override
	public boolean predict(float globalTime, IPose3D odometry, Rotation orientationEstimation, double cameraHeight)
	{
		if (odometry == null || orientationEstimation == null) {
			return false;
		}

		// here we could add our actions with a vector u
		// double[] u = {0, 0, 0};
		double[] u = {odometry.getX(), odometry.getY(), odometry.getZ()};
		filter.predict(u);

		updateState(globalTime, orientationEstimation);

		return true;
	}

	@Override
	public boolean correct(float globalTime, IFeatureMap map, List<IPointFeatureObservation> pointObservations,
			List<ILineFeatureObservation> lineObservations, Rotation orientationEstimation, double cameraHeight)
	{
		IPose3D pose = positionCalculator.localize(map, pointObservations, lineObservations, orientationEstimation);

		if (pose == null) {
			return false;
		}

		if (resetDistance > 0 &&
				Vector3D.distance(state.getLocalizedPose().getPosition(), pose.getPosition()) > resetDistance) {
			// the sensor position and the state are off by more than reset distance. Believe sensors.
			reset(globalTime, pose);
			return true;
		}

		RealVector z = new ArrayRealVector(new double[] {pose.getX(), pose.getY(), pose.getZ()});
		filter.correct(z);

		updateState(globalTime, pose.getOrientation());

		return true;
	}

	@Override
	public Map<String, ISensor> getSensors()
	{
		return sensors;
	}

	private void updateState(float globalTime, Rotation localizedOrientation)
	{
		Vector3D pos = new Vector3D(
				filter.getStateEstimation()[0], filter.getStateEstimation()[1], filter.getStateEstimation()[2]);

		state.set(globalTime, pos, localizedOrientation);
	}
}
