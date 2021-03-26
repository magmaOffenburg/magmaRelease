/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Base class for all connections to goals
 */
public abstract class GoalConnection extends Connection
{
	/**
	 * Calls parent constructor
	 * @param params the network parameters that define the runtime dynamics of
	 *        the network
	 * @param source the source proposition of the inhibition (negative
	 *        activation)
	 * @param destination the destination proposition of the competence module's
	 *        effects list
	 */
	GoalConnection(NetworkParams params, Proposition source, Proposition destination)
	{
		super(params, source, destination);
	}

	/**
	 * Returns the index of the goal to which this is connected
	 * @return the index of the goal to which this is connected
	 */
	public int getGoalIndex()
	{
		return ((Goal) getSourceModule()).getIndex();
	}
}
