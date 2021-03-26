/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import magma.agent.IHumanoidJoints;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.basic.RoboCupBehavior;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class SwingArms extends RoboCupBehavior
{
	public SwingArms(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.SWING_ARMS, thoughtModel);
	}

	public void perform(SupportFoot supportFoot, int speed)
	{
		super.perform();

		// to avoid self collisions
		int armRollOffset = 30;

		IRoboCupAgentModel agentModel = getAgentModel();
		if (supportFoot == SupportFoot.RIGHT) {
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderPitch).performAxisPosition(-60, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderYaw).performAxisPosition(-20, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmRoll).performAxisPosition(25 + armRollOffset, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmYaw).performAxisPosition(90, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderPitch).performAxisPosition(-120, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderYaw).performAxisPosition(15, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmRoll).performAxisPosition(-68 - armRollOffset, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmYaw).performAxisPosition(-51, speed);
		} else {
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderPitch).performAxisPosition(-120, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.RShoulderYaw).performAxisPosition(-15, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmRoll).performAxisPosition(68 + armRollOffset, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.RArmYaw).performAxisPosition(51, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderPitch).performAxisPosition(-60, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LShoulderYaw).performAxisPosition(20, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmRoll).performAxisPosition(-25 - armRollOffset, speed);
			agentModel.getWriteableHJ(IHumanoidJoints.LArmYaw).performAxisPosition(-90, speed);
		}
	}
}
