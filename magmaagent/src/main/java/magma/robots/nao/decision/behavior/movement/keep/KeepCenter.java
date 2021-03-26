/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.keep;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKeepBehavior;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;

public class KeepCenter extends MovementBehavior implements IKeepBehavior, INaoJoints
{
	public KeepCenter(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.KEEP_CENTER, thoughtModel, createMovement());
	}

	private static Movement createMovement()
	{
		Movement keep = new Movement("keepCenter");

		keep.add(new MovementPhase("phase1", 200)
						 .add(RKneePitch, 0, 7.03f)
						 .add(LKneePitch, 0, 7.03f)

						 .add(RArmYaw, 0, 7.03f)
						 .add(LArmYaw, 0, 7.03f)

						 .add(RShoulderPitch, 0, 7.03f)
						 .add(LShoulderPitch, 0, 7.03f)

						 .add(RShoulderYaw, -50, 7.03f)
						 .add(LShoulderYaw, 50, 7.03f)

						 .add(RHipPitch, 75, 7.03f)
						 .add(LHipPitch, 75, 7.03f)

						 .add(RFootPitch, -45, 7.03f)
						 .add(LFootPitch, -45, 7.03f)

						 .add(RHipYawPitch, -90, 7.03f)
						 .add(LHipYawPitch, -90, 7.03f));
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
