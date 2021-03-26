/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.action.IEffector;

/**
 * Default implementation for {@link IAction} interface.
 *
 * @author Stefan Glaser
 */
public class Action implements IAction
{
	/** The map of effectors (individual actions) that were generated within this agent cycle. */
	protected EffectorMap effectors;

	/** The action performer instance handling outgoing communication. */
	private IActionPerformer actionPerformer;

	/**
	 * Initializes all effectors that occur in the simulation
	 *
	 * @param actionPerformer the component that can send the actions
	 */
	public Action(IActionPerformer actionPerformer)
	{
		this.actionPerformer = actionPerformer;
		effectors = new EffectorMap();
	}

	@Override
	public void put(IEffector effector)
	{
		if (effector != null) {
			effectors.put(effector.getName(), effector);
		}
	}

	public EffectorMap getEffectorMap()
	{
		return effectors;
	}

	@Override
	public void send(IEffector effector)
	{
		sendAction(new EffectorMap(effector));
	}

	@Override
	public void sendAction()
	{
		sendAction(effectors);
		effectors.clear();
	}

	protected void sendAction(EffectorMap effectors)
	{
		actionPerformer.performAction(effectors);
	}
}
