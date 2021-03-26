/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

import java.io.File;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.IEbnAgentCreator;
import kdo.ebnDevKit.ebnAccess.impl.EbnAccess;

/**
 * Factory class for EbnAccess objects
 * @author Thomas Rinklin
 *
 */
public class EbnAccessFactory
{
	/** reference to the agent creator */
	private IEbnAgentCreator agentCreator;

	/**
	 * Adds a agent creator to the controller
	 * @param agentCreator the agent creator
	 */
	public void setAgentCreator(IEbnAgentCreator agentCreator)
	{
		this.agentCreator = agentCreator;
	}

	/**
	 * creates an ebnaccess object with an empty ebn
	 * @return the created ebnAccess object
	 */
	public IEbnAccess creatEmptyEbnAccess()
	{
		IEbnAgent agent = agentCreator.createAgent();
		return new EbnAccess(agent);
	}

	/**
	 * creates an ebnaccess object with an empty ebn from file
	 * @param f the ebn file
	 * @return the created ebnAccess object
	 */
	public IEbnAccess createEbnFromFile(File f)
	{
		IEbnAgent agent = agentCreator.createAgent();
		return new EbnAccess(agent, f);
	}
}
