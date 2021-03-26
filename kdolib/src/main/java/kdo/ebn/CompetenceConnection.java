/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Base class for all connections between competence modules
 */
abstract class CompetenceConnection extends Connection
{
	/**
	 * Constructor creating a connection by specifying the network parameters
	 * used to calculate the activation passed by this connection and the source
	 * and destination propositions of the connection.
	 * @param params the network parameters controlling runtime dynamics
	 * @param source the source proposition
	 * @param destination the destination proposition
	 */
	CompetenceConnection(NetworkParams params, Proposition source, Proposition destination)
	{
		super(params, source, destination);
	}

	/**
	 * Calculates the activation received directly or indirectly by goal
	 * goalIndex.
	 * @param goalIndex index of the goal from which the activation is originated
	 * @return the activation received directly or indirectly by goal goalIndex.
	 */
	public abstract double getGoalActivation(int goalIndex);
}
