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
import magma.robots.nao.INaoConstants;
import magma.robots.nao.INaoJoints;

public class MoveZero extends MovementBehavior implements INaoJoints
{
	private static Movement createMovement(IRoboCupThoughtModel thoughtModel)
	{
		MovementPhase moveZero = new MovementPhase("moveZero", 30);
		for (String jointName : thoughtModel.getAgentModel().getJointNames()) {
			if (INaoJoints.NeckPitch.equals(jointName)) {
				moveZero.add(jointName, -45, INaoConstants.MAX_JOINT_SPEED);
			} else {
				moveZero.add(jointName, 0, INaoConstants.MAX_JOINT_SPEED);
			}
		}
		return new Movement("moveZero").add(moveZero);
	}

	public MoveZero(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.MOVE_ZERO, thoughtModel, createMovement(thoughtModel));
	}
}
