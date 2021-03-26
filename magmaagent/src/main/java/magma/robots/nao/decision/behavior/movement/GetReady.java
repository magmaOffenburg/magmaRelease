/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;

public class GetReady extends MovementBehavior implements INaoJoints
{
	private static Movement createMovement()
	{
		return new Movement("getReady")
				.add(new MovementPhase("getReady", 30)
								.add(RShoulderPitch, -50, 3)
								.add(RShoulderYaw, 0, 7)
								.add(RArmRoll, 0, 7)
								.add(RArmYaw, 0, 7)
								.add(RHipYawPitch, 0, 7)
								.add(RHipRoll, 0, 7)
								.add(RHipPitch, 30, 7)
								.add(RKneePitch, -60, 7)
								.add(RFootPitch, 30, 7)
								.add(RFootRoll, 0, 7)

								.add(LShoulderPitch, -50, 3)
								.add(LShoulderYaw, 0, 7)
								.add(LArmRoll, 0, 7)
								.add(LArmYaw, 0, 7)
								.add(LHipYawPitch, 0, 7)
								.add(LHipRoll, 0, 7)
								.add(LHipPitch, 30, 7)
								.add(LKneePitch, -60, 7)
								.add(LFootPitch, 30, 7)
								.add(LFootRoll, 0, 7));
	}

	public GetReady(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.GET_READY, thoughtModel, createMovement());
	}

	@Override
	public boolean isFinished()
	{
		return super.isFinished() || getThoughtModel().isInSoccerPosition();
	}
}
