/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.decision.behavior.basic.Behavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * @author Klaus Dorer This is a class which sends each effector a command to
 *         stop
 */
public class StopBehavior extends Behavior
{
	public StopBehavior(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.STOP, thoughtModel);
	}

	protected StopBehavior(String name, IThoughtModel thoughtModel)
	{
		super(name, thoughtModel);
	}

	@Override
	public void perform()
	{
		// we send a 0.0 to each effector
		getAgentModel().getFutureBodyModel().resetAllMovements();
		super.perform();
	}
}
