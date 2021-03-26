/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents all links that inhibit a competence module by a goal
 */
public class ProtectedGoalLink extends GoalConnection
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
	public ProtectedGoalLink(NetworkParams params, Proposition source, Proposition destination)
	{
		super(params, source, destination);
	}

	/**
	 * @return the activation of this link as -delta * a * probability
	 */
	@Override
	public double getActivation()
	{
		activation = -networkParams.getDelta() * sourceProposition.getGoalActivation() *
					 ((Effect) destinationProposition).getInfluence();
		return activation;
	}
}
