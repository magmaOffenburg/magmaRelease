/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fall;

import hso.autonomy.agent.decision.behavior.IBehavior;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromBack;
import magma.robots.nao.decision.behavior.movement.getup.GetUpFromFront;

/**
 * Moving the arms to fall from side to back
 *
 * @author Maximilian Kroeg
 *
 */
public class MoveArmsToFall extends MovementBehavior implements INaoJoints
{
	private static Movement createMovement()
	{
		Movement moveArms = new Movement("moveArmsToFall");
		moveArms.add(new MovementPhase("phase1", 10)
							 .add(LShoulderYaw, 90, 7f)
							 .add(RShoulderYaw, -90, 7f)
							 .add(LShoulderPitch, 0, 7f)
							 .add(RShoulderPitch, 0, 7f)
							 .add(LArmRoll, 0, 7f)
							 .add(RArmRoll, 0, 7f)
							 .add(LArmYaw, 0, 7f)
							 .add(RArmYaw, 0, 7f));

		moveArms.add(new MovementPhase("phase2", 10)
							 .add(LShoulderYaw, 0, 7f)
							 .add(RShoulderYaw, 0, 7f)
							 .add(LShoulderPitch, 0, 7f)
							 .add(RShoulderPitch, 0, 7f)
							 .add(LArmRoll, 0, 7f)
							 .add(RArmRoll, 0, 7f)
							 .add(LArmYaw, 0, 7f)
							 .add(RArmYaw, 0, 7f));
		return moveArms;
	}

	/** movement to stretch legs */
	protected Movement moveLegs;

	public MoveArmsToFall(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.MOVE_ARM_TO_FALL_BACK, thoughtModel, createMovement());

		moveLegs = new Movement("stretchLegs");
		moveLegs.add(new MovementPhase("phase1", 10)
							 .add(LHipPitch, 0, 7f)
							 .add(LHipYawPitch, 0, 7f)
							 .add(LKneePitch, 0, 7f)
							 .add(LFootPitch, 0, 7f)
							 .add(LHipRoll, 0, 7f)
							 .add(LFootRoll, 0, 7f)
							 .add(RHipPitch, 0, 7f)
							 .add(RHipYawPitch, 0, 7f)
							 .add(RKneePitch, 0, 7f)
							 .add(RFootPitch, 0, 7f)
							 .add(RHipRoll, 0, 7f)
							 .add(RFootRoll, 0, 7f));
	}

	@Override
	protected Movement getNextMovement()
	{
		if (currentMovement == initialMovement) {
			if (getConsecutivePerforms() > 5) {
				return moveLegs;
			}
		}
		return null;
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		IBehavior realBehavior = actualBehavior.getRootBehavior();

		if (realBehavior instanceof GetUpFromBack || realBehavior instanceof GetUpFromFront ||
				realBehavior instanceof IKeepBehavior) {
			// don't switch directly from keep and other get up behaviors
			return super.switchFrom(actualBehavior);
		}

		// by default allow switching to this behavior immediately
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
		} else {
			actualBehavior.abort();
		}

		return this;
	}
}
