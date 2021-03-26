/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.agent;

import java.util.Map;
import kdo.ebn.ExtendedBehaviorNetwork;
import kdo.ebn.IEBNAction;
import kdo.ebn.IEBNPerception;
import kdo.ebn.IResourceBelief;

/**
 * interface for an agent to the devkit
 * @author Thomas Rinklin
 *
 */
public interface IEbnAgent {
	enum AgentStatus
	{
		Running,
		Stopped,
		Both
	}

	/**
	 * returns a map of all beliefs
	 * @return a map of all beliefs
	 */
	Map<String, IEBNPerception> getBeliefs();

	/**
	 * returns a map of all behaviors
	 * @return a map of all behaviors
	 */
	Map<String, IEBNAction> getBehaviors();

	/**
	 * returns a map of all resources
	 * @return a map of all resources
	 */
	Map<String, IResourceBelief> getResources();

	/**
	 * returns the agent status
	 * @return the agent status
	 */
	AgentStatus getStatus();

	/**
	 * starts the agent
	 */
	void start();

	/**
	 * stops the agent
	 */
	void stop();

	/**
	 * returns if the agent is startable
	 * @return true if the agent is startable
	 */
	boolean isStartable();

	/**
	 * returns the name of an agent
	 * @return the name of an agent
	 */
	String getName();

	/**
	 * connects or disconnects an ebn
	 * @param ebn the new ebn, or null for disconnecting
	 */
	void connectEbn(ExtendedBehaviorNetwork ebn);
}
