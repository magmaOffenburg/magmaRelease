/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents all links that inhibit a competence module by another competence
 * module
 */
public class ConflictorLink extends CompetenceConnection
{
	/**
	 * Calls parent constructor.
	 * @param params the network parameters that define the runtime dynamics of
	 *        the network
	 * @param source the source proposition of the inhibition (negative
	 *        activation)
	 * @param destination the destination proposition
	 */
	public ConflictorLink(NetworkParams params, Proposition source, Proposition destination)
	{
		super(params, source, destination);
	}

	/**
	 * @return the activation of this link
	 */
	@Override
	public double getActivation()
	{
		return activation;
	}

	/**
	 * Calculates the activation received directly or indirectly by goal
	 * goalIndex.
	 * @param goalIndex index of the goal from which the activation is originated
	 * @return the activation received directly or indirectly by goal goalIndex.
	 */
	@Override
	public double getGoalActivation(int goalIndex)
	{
		double proposition;
		double goalActivation;

		goalActivation = sourceProposition.getTransferedActivation(goalIndex);
		proposition = sourceProposition.getTruthValue();

		goalActivation = -networkParams.getDelta() * proposition * goalActivation *
						 ((Effect) destinationProposition).getInfluence();

		if (goalIndex == 0) {
			this.activation = goalActivation;
		} else {
			this.activation += goalActivation;
		}

		return goalActivation;
	}
}
