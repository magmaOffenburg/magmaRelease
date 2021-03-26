/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.basic;

import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class SendPassCommand extends RoboCupBehavior
{
	public SendPassCommand(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.SEND_PASS_COMMAND, thoughtModel);
	}

	@Override
	public void perform()
	{
		super.perform();

		getAgentModel().sendPassCommand();
	}
}
