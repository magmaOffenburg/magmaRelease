/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.genericAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.util.AgentNameHelper;

/**
 * Generic not startable agent implementation, which gets the the names of the
 * behaviors, beliefs and resources via constructor parameters
 * @author Thomas Rinklin
 */
public class GenericAgent implements IEbnAgent
{
	private final Map<String, IEBNAction> behaviors;

	private final Map<String, IEBNPerception> beliefs;

	private final Map<String, IResourceBelief> resources;

	private final String name;

	/**
	 * constructor
	 * @param nameHelper helper class for creating names
	 * @param behaviors2 list of behavior names
	 * @param beliefs2 list of belief names
	 * @param resources2 list of resource names
	 */
	public GenericAgent(
			AgentNameHelper nameHelper, List<String> behaviors2, List<String> beliefs2, List<String> resources2)
	{
		name = nameHelper.getName("GenericAgent");
		behaviors = new HashMap<String, IEBNAction>();
		beliefs = new HashMap<String, IEBNPerception>();
		resources = new HashMap<String, IResourceBelief>();

		for (String string : behaviors2) {
			behaviors.put(string, new GenericBehavior(string));
		}
		for (String string : beliefs2) {
			beliefs.put(string, new GenericBelief(string));
		}
		for (String string : resources2) {
			resources.put(string, new GenericResourceBelief(string));
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Map<String, IEBNAction> getBehaviors()
	{
		return behaviors;
	}

	@Override
	public Map<String, IEBNPerception> getBeliefs()
	{
		return beliefs;
	}

	@Override
	public Map<String, IResourceBelief> getResources()
	{
		return resources;
	}

	@Override
	public AgentStatus getStatus()
	{
		return AgentStatus.Stopped;
	}

	@Override
	public void connectEbn(ExtendedBehaviorNetwork ebn)
	{
	}

	@Override
	public boolean isStartable()
	{
		return false;
	}

	@Override
	public void start()
	{
	}

	@Override
	public void stop()
	{
	}
}
