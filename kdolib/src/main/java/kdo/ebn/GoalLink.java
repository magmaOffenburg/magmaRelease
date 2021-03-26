/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents all links that activate a competence module by a goal
 */
public class GoalLink extends GoalConnection
{
	/**
	 * Calls parent constructor.
	 * @param params the network parameters that define the runtime dynamics of
	 *        the network
	 * @param source the source proposition of the goal condition
	 * @param destination the destination proposition of the competence module's
	 *        effects list
	 */
	public GoalLink(NetworkParams params, Proposition source, Proposition destination)
	{
		super(params, source, destination);
	}

	/**
	 * @return the activation of this link as gamma multiplied by activation and
	 *         probability
	 */
	@Override
	public double getActivation()
	{
		// TODO I wonder why value of source prop is not included
		// original:
		// #ifdef ACTIVATION_FAN
		// dProposition = (1.0 - m_pSourceProp->GetValue());
		// #else
		// dProposition = 1.0;
		// #endif

		activation = networkParams.getGamma() * sourceProposition.getGoalActivation() *
					 ((Effect) destinationProposition).getInfluence();
		return activation;
	}
}
