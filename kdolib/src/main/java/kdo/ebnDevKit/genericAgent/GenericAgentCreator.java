/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.genericAgent;

import java.util.List;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.IEbnAgentCreator;
import kdo.ebnDevKit.agent.util.AgentNameHelper;

/**
 * Creator class for generic agents
 * @author Thomas Rinklin
 */
public class GenericAgentCreator implements IEbnAgentCreator
{
	private final AgentNameHelper nameHelper = new AgentNameHelper();

	private final List<String> behaviors;

	private final List<String> beliefs;

	private final List<String> resources;

	/**
	 * constructor
	 * @param behaviors list of behavior names
	 * @param beliefs list of belief names
	 * @param resources list of resource names
	 */
	public GenericAgentCreator(List<String> behaviors, List<String> beliefs, List<String> resources)
	{
		this.behaviors = behaviors;
		this.beliefs = beliefs;
		this.resources = resources;
	}

	@Override
	public IEbnAgent createAgent()
	{
		return new GenericAgent(nameHelper, behaviors, beliefs, resources);
	}
}
