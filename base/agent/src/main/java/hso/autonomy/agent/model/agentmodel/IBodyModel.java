/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.util.geometry.Pose6D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author dorer
 */
public interface IBodyModel {
	/**
	 * @param name the name of the sensor to return
	 * @return the first sensor in a body part that is found having passed name
	 */
	ISensor getSensor(String name);
	/**
	 * @param name the name of the actuator to return
	 * @return the first actuator in a body part that is found having passed name
	 */
	IActuator getActuator(String name);

	/**
	 * @param name name of the joint to retrieve
	 * @return the first joint specified by its name found in a body part, null
	 *         if not existing in this model
	 */
	IHingeJointR getJoint(String name);

	/**
	 * @return the root body part of this body model
	 */
	IBodyPart getRootBodyPart();

	/**
	 * @param name the name of the body part to return
	 * @return the first body part found with passed name, null if not existing
	 */
	IBodyPart getBodyPart(String name);

	/**
	 * @return the center of mass of the robot
	 */
	Vector3D getCenterOfMass();

	/**
	 * @param name the name of the hinge joint to change axis speed
	 * @param value the position this joint should reach (in degrees?)
	 */
	void setHingeJointPosition(String name, double value);

	/**
	 * Moves all joints towards their zero position. This method is usually
	 * called to bring the pose as close as possible to the initial pose while
	 * beaming.
	 */
	void performInitialPose();

	/**
	 * Reset all movements (set all joint speeds to 0).
	 */
	void resetAllMovements();

	/**
	 * Adjusts the desired speed of the passed joint to reach current position +
	 * delta
	 * @param name the name of the hinge joint to change axis speed
	 * @param delta the delta angle (deg) to add to the current position
	 */
	void adjustHingeJointPosition(String name, double delta);

	/**
	 * Get the position of the corners of the given BodyPart relative to the
	 * torso center.
	 *
	 * e.g. for a cube this will return eight vectors to the corners of it
	 *
	 * <pre>
	 *     6___________4
	 *    /|          /|
	 *   / |         / |
	 *  7___________5  |
	 *  |  |        |  |
	 *  |  3________|__1     z
	 *  | /         | /      |   x
	 *  |/          |/       | /
	 *  2___________0	     |/_____y
	 * </pre>
	 *
	 * TODO: change the method (BodyPart.getCorners()) that the sequence makes
	 * more sense
	 *
	 * @param part The BodyPart
	 * @return corners of the given part relative to the robots torso center
	 */
	Vector3D[] getCorners(IBodyPart part);

	/**
	 * Convenient method for
	 * {@link #moveBodyPartToPose(String, Vector3D, Vector3D)}.
	 *
	 * @param targetBodyName the name of the body part that should be moved
	 * @param targetPose the target pose
	 * @return success
	 */
	boolean moveBodyPartToPose(String targetBodyName, Pose6D targetPose);

	/**
	 * Move the targetBodyPart to the specified target pose relative to the torso
	 * using the underlying agent IK-solver.
	 *
	 * @param targetBodyName the name of the body part that should be moved
	 * @param targetPosition the target position
	 * @param targetAngles the target angles
	 * @return success
	 */
	boolean moveBodyPartToPose(String targetBodyName, Vector3D targetPosition, Vector3D targetAngles);

	/**
	 * Use this method to do inverse kinematics and calculate the joint angles,
	 * speed and acceleration of the target
	 * @param targetBodyName name of the body part for which to solve
	 * @param targetPose an array of three 6D target poses in the time difference
	 *        FunctionUtil.DELTA_T
	 * @return false if the body part is not existing or is not applicable to
	 *         multiple target position calculation.
	 */
	boolean moveBodyPartToPose(String targetBodyName, Pose6D[] targetPose);
}