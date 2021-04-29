/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fall;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.movement.Movement;
import magma.agent.decision.behavior.movement.MovementBehavior;
import magma.agent.decision.behavior.movement.MovementPhase;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.INaoJoints;

/**
 * @author Klaus Dorer
 */
public class FallForward extends MovementBehavior
{
	private static Movement createMovement()
	{
		return new Movement("fallForward")
				.add(new MovementPhase("phase1", 10)
								.add(INaoJoints.LFootPitch, 60, 7f)
								.add(INaoJoints.RFootPitch, 60, 7f));
	}

	public FallForward(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.FALL_FORWARD, thoughtModel, createMovement());
	}
}
