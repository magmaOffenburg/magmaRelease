/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.agent;

import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.IEbnAgentCreator;
import kdo.ebnDevKit.agent.util.AgentNameHelper;

/**
 * class the create agents for the staubs domain
 * @author Thomas Rinklin
 */
public class StaubsAgentCreator implements IEbnAgentCreator
{
	private final AgentNameHelper agentNameHelper;

	public StaubsAgentCreator()
	{
		agentNameHelper = new AgentNameHelper();
	}

	@Override
	public IEbnAgent createAgent()
	{
		return new StaubsAgent(agentNameHelper);
	}
}
