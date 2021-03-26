/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface to represent a state hypothesis (belief)
 * @author Rico Schillings
 */
public interface IWeightedParticle {
	/**
	 * Retrieve the current pose of this particle
	 * @return the pose of this particle
	 */
	IPose3D getPose();

	/**
	 * Updates the pose for this particle by a received motion update
	 *
	 * @param odomTrans the received translation
	 * @param odomRotation the received rotation
	 */
	void updatePose(Vector3D odomTrans, Angle deltaZAngle, Rotation odomRotation);

	/**
	 *  Updates the rotation of the particle. Separated update is required
	 *  since we can receive an IMU update without odometry information
	 * @param newRotation
	 */
	void updateRotation(Rotation newRotation);

	double getWeight();

	void setWeight(double newWeight);
	/**
	 * Retrieve the current (normed) probability of this particle
	 * @return the current probability of this particle
	 */
	double getProbability();

	/**
	 * Set the new (normed) probability of this particle
	 * @param newProbability new calculated probability of this particle
	 */
	void setProbability(double newProbability);
}
