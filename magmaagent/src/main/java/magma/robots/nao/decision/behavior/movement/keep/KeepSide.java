/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.keep;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.decision.behavior.movement.SidedMovementBehavior;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;

/**
 * @author Rajit Shahi, Ingo Schindler
 */
public class KeepSide extends SidedMovementBehavior implements IKeepBehavior, INaoJoints
{
	public KeepSide(Side side, IRoboCupThoughtModel thoughtModel)
	{
		super(side, IBehaviorConstants.KEEP_SIDE.BASE_NAME, thoughtModel, createMovement());
	}

	private static Movement createMovement()
	{
		Movement keep = new Movement("keepRight");
		keep.add(new MovementPhase("phase1", 200)
						 .add(LShoulderPitch, 90, 7f)
						 .add(LShoulderYaw, 0, 7f)
						 .add(LArmYaw, 0, 7f)
						 .add(LArmRoll, 0, 7f)

						 .add(LHipPitch, 0, 7f)
						 .add(LKneePitch, 0, 7f)
						 .add(LFootPitch, 0, 7f)
						 .add(LFootRoll, -20, 7f)

						 .add(RShoulderPitch, 90, 7f)
						 .add(RShoulderYaw, 0, 7f)
						 .add(RArmYaw, 0, 7f)
						 .add(RArmRoll, 0, 7f)

						 .add(RHipPitch, 60, 7f)
						 .add(RKneePitch, -120, 7f)
						 .add(RFootPitch, 60, 7f)
						 .add(RFootRoll, -20, 7f));
		return keep;
	}

	@Override
	public void perform()
	{
		if (!currentMovement.isFinished() && shouldAbortKeeping(getWorldModel())) {
			isFinished = true;
			switchTo(initialMovement);
			return;
		}

		super.perform();
	}
}
