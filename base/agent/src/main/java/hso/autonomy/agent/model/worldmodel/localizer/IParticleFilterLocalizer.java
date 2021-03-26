/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import hso.autonomy.util.geometry.IPose3D;
import java.util.List;

/**
 * This interface provides all steps for a featured-based particle filter localization.
 * Since the algorithms in the single steps can be implemented with different methods,
 * you can vary those in the individual implementation
 *
 * @author Rico Schillings
 */
public interface IParticleFilterLocalizer extends IFeatureLocalizer {
	/**
	 * Distributes the particles for the localizer for the initial state.
	 * @param nrOfParticles number of desired particles
	 * @param initialPoseEstimation if known, set a position to start
	 */
	void initializeParticles(int nrOfParticles, IPose3D initialPoseEstimation);

	/**
	 * Tries to match the observed feature with those from the known map.
	 * It also calculates a "weight" for each particle, based on the correspondences.
	 * This could be for example the mean deviation of all correspondences per particle
	 *
	 * @param pointObservations detected features from the used sensor
	 * @param map map of the environment with the known features
	 * @return the sum of all particles weights for further normalization
	 */
	double sampleImportanceFromObservations(List<IPointFeatureObservation> pointObservations, IFeatureMap map);

	double sampleImportanceFromMap(IFeatureMap map);
	/**
	 * normalizes the weight of the particles and evaluates the particle set to define if a resampling is required
	 * @param normFactor sum of all weights to normalize
	 */
	void normalizeImportance(double normFactor);
	/**
	 * resamples the particles based on the updated importance factor.
	 * "Survival of the fittest" -> replace unlikely particles by more likely ones
	 */
	void resampleParticles();

	/**
	 * Updates the current best particle, based on new weighted particle set
	 */
	void updateBestCandidate();

	/**
	 * Add new random particle, based on a given existing particle
	 */
	IWeightedParticle addRandomParticle(IWeightedParticle particle);
}
