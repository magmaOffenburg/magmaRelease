/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.agent;

import java.util.HashMap;
import java.util.Map;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.util.AgentNameHelper;
import kdo.ebnDevKit.staubs.decision.EBNCreator;
import kdo.ebnDevKit.staubs.model.IStaubs;
import kdo.ebnDevKit.staubs.model.Staubs;
import kdo.util.observer.IObserver;

/**
 * class for the staubs agent
 * @author Thomas Rinklin
 *
 */
public class StaubsAgent implements IEbnAgent
{
	/**
	 * world connection
	 */
	private IStaubs staubs;

	/**
	 * behaviors
	 */
	private Map<String, IEBNAction> behaviors;

	/**
	 * beliefs
	 */
	private Map<String, IEBNPerception> beliefs;

	/**
	 * resources
	 */
	private Map<String, IResourceBelief> resources;

	/**
	 * agent name
	 */
	private String name;

	/**
	 * helper class to create the name
	 */
	private final AgentNameHelper agentNameHelper;

	/**
	 * extended behavior network
	 */
	private ExtendedBehaviorNetwork ebn;

	public StaubsAgent(AgentNameHelper agentNameHelper)
	{
		this.agentNameHelper = agentNameHelper;
		init();
	}

	private void init()
	{
		staubs = new Staubs();

		EBNCreator ebnCreator = new EBNCreator(staubs);
		behaviors = ebnCreator.getBehaviors();
		beliefs = ebnCreator.getBeliefs();
		resources = ebnCreator.getResources();

		Map<String, IEBNPerception> mergedBeliefs = new HashMap<String, IEBNPerception>();
		mergedBeliefs.putAll(beliefs);
		mergedBeliefs.putAll(resources);

		name = agentNameHelper.getName("Staubs Agent");

		staubs.attachDecisionMaker(new IObserver<IStaubs>() {
			@Override
			public void update(IStaubs content)
			{
				if (ebn != null)
					ebn.decide();
			}
		});
	}

	@Override
	public void connectEbn(ExtendedBehaviorNetwork ebn)
	{
		this.ebn = ebn;
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
		if (staubs.isAutoTick())
			return AgentStatus.Running;
		return AgentStatus.Stopped;
	}

	@Override
	public void start()
	{
		staubs.setAutoTick(true);
	}

	@Override
	public void stop()
	{
		staubs.setAutoTick(false);
	}

	@Override
	public boolean isStartable()
	{
		return true;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
