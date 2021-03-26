/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.agentmodel;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.util.geometry.IPose2D;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Provides read only access to the agent model including universal joints,
 * hinge joints, force resistance and gyro rate sensors
 *
 * @author Stefan Glaser, Klaus Dorer
 */
public interface IRoboCupAgentModel extends IAgentModel {
	/**
	 * Set the message to say in that cycle.
	 */
	void sayMessage(String message);

	/**
	 * Set the beam position of the agent.
	 */
	void beamToPosition(IPose2D pose);

	void sendPassCommand();

	/**
	 * Returns the static pivot-point used as replacement of the CoM in the
	 * balancing engine related movements. This pivot-point is usually close to
	 * the initial CoM location or somewhere in the pelvis of the robot.
	 *
	 * @return the static pivot-point
	 */
	Vector3D getStaticPivotPoint();

	/**
	 * @return true if this agent has foot force sensors attached
	 */
	boolean hasFootForceSensors();

	/**
	 * @param offGroundBefore the minimal number of cycles the force sensor had to be zero in last off ground situation
	 * @param maxOnGround the maximal number of cycles the foot sensor has force since last ground contact
	 * @return the foot that just stepped properly on ground (off ground for at least n cycles and on ground no longer
	 *         than m cycles), or always <code>NONE</code> if <code>hasFootForceSensors() == false</code>
	 */
	SupportFoot getStepFoot(int offGroundBefore, int maxOnGround);

	/**
	 * @return the angle (in degrees) the knees should have when starting to walk
	 */
	float getSoccerPositionKneeAngle();

	/**
	 * @return the angle (in degrees) the hip pitch should have when starting to walk
	 */
	float getSoccerPositionHipAngle();

	/**
	 * @return the time (in s) for one robot cycle
	 */
	float getCycleTime();

	/**
	 * @return the time (in cycles) we look into future to predict goals
	 */
	int getGoalPredictionTime();

	/**
	 * @return the height of the robot (in m)
	 */
	float getHeight();

	/**
	 * @return the z coordinate of the torso center when the robot is upright
	 */
	float getTorsoZUpright();

	/**
	 * @return the name of this robot model as defined in the meta model
	 */
	String getModelName();

	/**
	 * @return a list of all joint names
	 */
	List<String> getJointNames();

	/**
	 * Mirrored joints are all roll joints, ShoulderYaw, ArmYaw
	 * @param name the name of the joint
	 * @return true if this joint needs to be mirrored when mirroring behaviors
	 */
	boolean isMirrorJoint(String name);
}