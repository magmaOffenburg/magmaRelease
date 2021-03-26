/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.util.geometry.Pose6D;
import java.io.Serializable;
import magma.agent.model.agentmodel.SupportFoot;

/**
 * @author Stefan Glaser
 */
public interface IIKMovement extends IBalancingEngineParameters, Serializable {
	/**
	 * @return the name of the movement
	 */
	String getName();

	/**
	 * Called to reinitialize the movement.<br>
	 * This method is called once before execution, to allow the movement to
	 * setup/adapt the trajectory to the current parameterization/situation.<br>
	 * <br>
	 * The <code>other</code> movement instance is used as morphing instance. If
	 * several movements are executed in a row, parameters can be forwarded or
	 * calculated along movement executions. If the <code>other</code> movement
	 * is <code>null</code>, the movement should parameterize arbitrary.
	 *
	 * @param other - the other movement from which should be morphed
	 */
	void init(IIKMovement other);

	/**
	 * Update the progress of the movement.<br>
	 * This method is called continuously after initialization to perform the
	 * movement.<br>
	 * <br>
	 * By returning false, a movement can signal early abort during execution.
	 */
	boolean update();

	/**
	 * @return the support foot of the movement
	 */
	SupportFoot getSupportFoot();

	/**
	 * Called to determine the support foot after the movement. The value is an
	 * indicator for the subsequent movement which foot is preferred to support
	 * the next movement.
	 *
	 * @return the support foot after the movement has finished
	 */
	SupportFoot getNextSupportFoot();

	/**
	 * @return true if the movement is finished
	 */
	boolean isFinished();

	/**
	 * @return indicates if the movement is static or dynamic
	 */
	boolean isStatic();

	/**
	 * @return the number of cycles of the movement
	 */
	int getMovementCycles();

	/**
	 * @return the number of cycles to hold the last pose of the movement
	 */
	int getHoldCycles();

	/**
	 * @return the number of cycles since we started performing the movement
	 */
	int getMovementProgress();

	/**
	 * @return the target pose of the left foot in the current calculation step
	 */
	Pose6D getLeftFootPose();

	/**
	 * @return the target pose of the left arm in the current calculation step
	 */
	Pose6D getLeftArmPose();

	/**
	 * @return the target pose of the right foot in the current calculation step
	 */
	Pose6D getRightFootPose();

	/**
	 * @return the target pose of the right arm in the current calculation step
	 */
	Pose6D getRightArmPose();

	/**
	 * @return the trajectories of arms and legs;
	 */
	MovementTrajectories getMovementTrajectories();
}