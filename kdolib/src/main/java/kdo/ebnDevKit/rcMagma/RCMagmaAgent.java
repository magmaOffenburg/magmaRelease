/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.rcMagma;

import java.util.HashMap;
import java.util.Map;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;
import kdo.ebnDevKit.agent.IEbnAgent;
import kdo.ebnDevKit.agent.util.AgentNameHelper;

/**
 * Agent for the magma robocup agents. This integration would need to be placed
 * into the RoboCup code.
 * @author Thomas Rinklin
 */
public class RCMagmaAgent implements IEbnAgent
{
	private final String name;

	@SuppressWarnings("unused")
	private ExtendedBehaviorNetwork ebn;

	// private final AgentRuntime rcClient;

	private Map<String, IEBNPerception> beliefs;

	private Map<String, IEBNAction> behaviors;

	public RCMagmaAgent(AgentNameHelper nameHelper)
	{
		name = nameHelper.getName("RCMagma");

		// ComponentFactory cf = new NAOComponentFactory() {
		// @Override
		// public IDecisionMaker createDecisionMaker(
		// Map<String, IEBNAction> behaviors, IThoughtModel thoughtModel,
		// int playerNumber, int serverVersion, String decisionMakerName,
		// ParameterMap learningParam)
		// {
		// RCMagmaAgent.this.beliefs = beliefs;
		// RCMagmaAgent.this.behaviors = behaviors;
		// return new EBNProxy();
		// }
		// };
		//
		// PlayerParameters pp = new PlayerParameters(
		// IMagmaConstants.DEFAULT_TEAMNAME, (byte) 0, 0,
		// IServerConnection.SERVER_IP, IServerConnection.SERVER_PORT, null,
		// Level.FINE, IServerConfigFilesConstants.DEFAULT_SERVER_VERSION, cf,
		// IMagmaConstants.DEFAULT_DECISION_MAKER);
		//
		// rcClient = new AgentRuntime(pp);
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
	public String getName()
	{
		return name;
	}

	@Override
	public Map<String, IResourceBelief> getResources()
	{
		return new HashMap<String, IResourceBelief>();
	}

	@Override
	public AgentStatus getStatus()
	{
		// if (rcClient.isConnected()) {
		// return AgentStatus.Running;
		// }

		return AgentStatus.Both;
	}

	@Override
	public boolean isStartable()
	{
		return true;
	}

	@Override
	public void start()
	{
		Thread t = new Thread(new Runnable() {
			@Override
			public void run()
			{
				// rcClient.startClient();
			}
		});

		t.start();
	}

	@Override
	public void stop()
	{
		// rcClient.stopClient();
	}

	// private class EBNProxy implements IDecisionMaker
	// {
	//
	// @Override
	// public boolean decide()
	// {
	// System.out.println("decide");
	//
	// if (ebn != null) {
	// return ebn.decide();
	// }
	// return false;
	// }
	//
	// @Override
	// public IEBNAction getBehavior(String name)
	// {
	// return behaviors.get(name);
	// }
	//
	// public IEBNPerception getBelief(String name)
	// {
	// return beliefs.get(name);
	// }
	//
	// @Override
	// public IEBNAction getCurrentBehavior()
	// {
	// return ebn.getCurrentBehavior();
	// }
	//
	// @Override
	// public IEBNAction getDesiredBehavior()
	// {
	// return null;
	// }
	// }
}
