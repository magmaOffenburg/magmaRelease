/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.action.impl.Action;
import hso.autonomy.agent.communication.action.impl.EffectorMap;
import magma.agent.communication.action.IRoboCupAction;

/**
 * This class generates server messages from Effector objects.
 */
public class RoboCupAction extends Action implements IRoboCupAction
{
	/**
	 * Initializes all effectors that occur in the simulation
	 *
	 * @param actionPerformer the component that can send the actions
	 */
	public RoboCupAction(IActionPerformer actionPerformer)
	{
		super(actionPerformer);
	}

	@Override
	public void sendSync()
	{
		sendAction(new EffectorMap());
	}
}
