/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * "HingeJoint" sensor read only interface.
 *
 * @author Stefan Glaser, Klaus Dorer
 */
public interface IHingeJointR extends ISensor, Serializable {
	/**
	 * Retrieve the joint axis.
	 * @return
	 */
	Vector3D getJointAxis();

	/**
	 * @return the minimal angle that can be reached by the joint (in degrees)
	 */
	float getMinAngle();

	/**
	 * @return the maximal angle that can be reached by the joint (in degrees)
	 */
	float getMaxAngle();

	/**
	 * @return the maximum speed this joint can go (in degree per cycle)
	 */
	float getMaxSpeed();

	/**
	 * Retrieve the current axis position angle of the joint.
	 *
	 * @return current angle of the joint (in degrees)
	 */
	float getAngle();

	/**
	 * Returns the next axis speed to the joint.
	 *
	 * @return next axis speed to perform (in deg/cycle)
	 */
	float getNextAxisSpeed();

	/**
	 * Retrieve the rotation matrix, related to the current axis positions of the
	 * joint.
	 *
	 * @return the rotation matrix of the joint
	 */
	Rotation getRotation();

	/**
	 * Retrieve the translation vector, related to the current axis positions of the joint.
	 *
	 * @return the translation vector of the joint
	 */
	Vector3D getTranslation();

	/**
	 * @return the acceleration in deg/cycle//cycle
	 */
	float getAcceleration();
}