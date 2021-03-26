/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fall;

import static magma.robots.nao.INaoJoints.*;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class FallSide extends MovementBehavior
{
	private static Movement createMovement()
	{
		return new Movement("fallSide")
				.add(new MovementPhase("phase1", 200)
								.add(LShoulderPitch, -60, 7f)
								.add(LShoulderYaw, 0, 7f)
								.add(LArmYaw, 0, 7f)
								.add(LArmRoll, 0, 7f)

								.add(LHipPitch, 0, 7f)
								.add(LKneePitch, 0, 7f)
								.add(LFootPitch, 0, 7f)
								.add(LFootRoll, -20, 7f)

								.add(RShoulderPitch, -60, 7f)
								.add(RShoulderYaw, 0, 7f)
								.add(RArmYaw, 0, 7f)
								.add(RArmRoll, 0, 7f)

								.add(RHipPitch, 60, 7f)
								.add(RKneePitch, -120, 7f)
								.add(RFootPitch, 60, 7f)
								.add(RFootRoll, -20, 7f));
	}

	public FallSide(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.FALL_SIDE, thoughtModel, createMovement());
	}
}
