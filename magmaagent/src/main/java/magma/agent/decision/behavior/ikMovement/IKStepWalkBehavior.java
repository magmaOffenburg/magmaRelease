/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.VectorUtils;
import kdo.util.parameter.ParameterMap;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.ikMovement.walk.IKWalkMovementParametersBase;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public class IKStepWalkBehavior extends IKWalkBehavior
{
	private Pose2D target;

	private Vector2D speedAtTarget;

	public IKStepWalkBehavior(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.IK_WALK_STEP, thoughtModel, params, behaviors);
	}

	/**
	 * @param target the target position and orientation for any leg
	 * @param speedAtTarget the speed we want to have at the target
	 */
	public void setStepTarget(Pose2D target, Vector2D speedAtTarget)
	{
		this.target = target;
		this.speedAtTarget = speedAtTarget;
	}

	@Override
	public void perform()
	{
		calculateStepPlan(intendedStep);
		super.perform();
	}

	protected void calculateStepPlan(Step intendedStep)
	{
		Vector2D stopSpeed = speedAtTarget.negate();
		Step stopStepParams = new Step();
		// initialize stopStep with speed at target
		// can we assume that walk is symmetric? That we can plan backward walk
		// and perform the same thing forward?

		Pose2D currentTarget = target;
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		SupportFoot supportFoot = walkMovement.supportFoot;
		String otherFootName = supportFoot == SupportFoot.LEFT ? IHumanoidConstants.LFoot : IHumanoidConstants.RFoot;
		Pose3D currentOtherFootPose = getAgentModel().getBodyPart(otherFootName).getPose();
		Pose2D origin = thisPlayer.calculateGlobalBodyPose2D(currentOtherFootPose);

		// last step with desired speed
		stopStepParams = calculateStep(stopStepParams, stopSpeed, Angle.ZERO, paramSetName);
		currentTarget = newPositionFromStepParams(currentTarget, stopStepParams, supportFoot);
		supportFoot = switchSupportFoot(supportFoot);

		// calculate the position to which forward run has to aim
		Angle intendedTurn;
		do {
			// calculate new position of leg and new speed
			intendedTurn = currentTarget.getAngleTo(origin).getAdjacencyAngle().negate();
			Vector2D intendedWalk = getVectorTo(currentTarget, origin);
			stopStepParams = calculateStep(stopStepParams, intendedWalk, intendedTurn, paramSetName);
			currentTarget = newPositionFromStepParams(currentTarget, stopStepParams, supportFoot);
			supportFoot = switchSupportFoot(supportFoot);

		} while (Math.abs(intendedTurn.degrees()) > 20);

		// now calculate forward from origin until facing current target
		Step startStepParams = intendedStep.copy();
		Pose2D currentPose = origin;
		do {
			// calculate new position of leg and new speed
			intendedTurn = currentPose.getAngleTo(currentTarget);
			Vector2D intendedWalk = getVectorTo(currentPose, currentTarget);
			startStepParams = calculateStep(startStepParams, intendedWalk, intendedTurn, paramSetName);
			currentPose = newPositionFromStepParams(currentPose, startStepParams, supportFoot);
			supportFoot = switchSupportFoot(supportFoot);

		} while (Math.abs(intendedTurn.degrees()) > 20);

		// now calculate the step params that will lead to the current target
		IKWalkMovementParametersBase walkParams = walkMovement.getWalkParameters();
		// double distance = origin.getDistanceTo(currentTarget);
		Vector2D intendedWalk = getVectorTo(currentPose, currentTarget);

		intendedWalk = VectorUtils.to2D(thisPlayer.getOrientation().applyInverseTo(VectorUtils.to3D(intendedWalk)));
		double y = intendedWalk.getY();
		double x = intendedWalk.getX();
		int stepsForward = (int) (y / walkParams.getMaxStepLength());

		if (stepsForward > 2) {
			// intendedStep.y_targetDistance = walkParams.getMaxStepLength();
			intendedStep.forward = y / stepsForward;
			intendedStep.sideward = x / stepsForward;
		} else if (stepsForward > 0) {
			intendedStep.forward = y / stepsForward;
			intendedStep.sideward = x / stepsForward;
		} else {
			System.out.println(" should not get here ");
			intendedStep.forward = y / stepsForward;
			intendedStep.sideward = x / stepsForward;
		}

		// do we need turnPriority for calculateStepParameter?
		// what if backward calculation ends 'behind' origin?
	}

	private SupportFoot switchSupportFoot(SupportFoot supportFoot)
	{
		return (supportFoot == SupportFoot.LEFT) ? SupportFoot.RIGHT : SupportFoot.LEFT;
	}

	private Pose2D newPositionFromStepParams(Pose2D currentTarget, Step stopStepParams, SupportFoot supportFoot)
	{
		return calculateFreeFootPose(currentTarget, supportFoot, stopStepParams);
	}

	private Vector2D getVectorTo(Pose2D source, Pose2D destination)
	{
		return destination.getPosition().subtract(source.getPosition());
	}
}
