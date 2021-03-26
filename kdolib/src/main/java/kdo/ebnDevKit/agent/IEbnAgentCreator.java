/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.agent;

/**
 * Interface of a startup component for ebnAgents
 * @author Thomas Rinklin
 *
 */
public interface IEbnAgentCreator {
	/**
	 * create an agent
	 * @return a interface to a instance of an agent
	 */
	IEbnAgent createAgent();
}
