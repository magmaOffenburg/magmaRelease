/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.rcMagma;

import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.IEbnAgentCreator;
import kdo.ebnDevKit.agent.util.AgentNameHelper;

/**
 * creator for the magma robocup agents
 * @author Thomas Rinklin
 *
 */
public class RCMagmaAgentCreator implements IEbnAgentCreator
{
	private final AgentNameHelper nameHelper = new AgentNameHelper();

	@Override
	public IEbnAgent createAgent()
	{
		return new RCMagmaAgent(nameHelper);
	}
}
