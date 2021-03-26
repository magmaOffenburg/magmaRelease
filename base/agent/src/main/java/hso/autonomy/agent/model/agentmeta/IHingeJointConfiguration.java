/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for a hinge joint configuration.
 *
 * @author Stefan Glaser
 */
public interface IHingeJointConfiguration extends ISensorConfiguration {
	/**
	 * @return the name of this effector
	 */
	String getEffectorName();

	/**
	 * Returns a vector, which defines the rotation axis of the joint.
	 *
	 * @return the rotation axis
	 */
	Vector3D getJointAxis();

	/**
	 * Returns the minimum possible position-angle of the joint.
	 *
	 * @return the minimum position angle (deg)
	 */
	int getMinAngle();

	/**
	 * Returns the maximum possible position-angle of the joint.
	 *
	 * @return the maximum position angle (deg)
	 */
	int getMaxAngle();

	/**
	 * Returns the maximum possible speed to the joint.
	 *
	 * @return the maximum possible speed (deg/cycle)
	 */
	float getMaxSpeed();

	/**
	 * Changes the maximal speed of the joint
	 *
	 * @param value the maximum possible speed (deg/cycle)
	 */
	void setMaxSpeed(float value);

	/**
	 * Returns the maximum possible acceleration.
	 *
	 * @return the maximum possible speed (deg/cycle//cycle)
	 */
	float getMaxAcceleration();

	/**
	 * @return whether to move to initial position automatically if unused this cycle
	 */
	boolean getDefaultToInitialPos();
}
