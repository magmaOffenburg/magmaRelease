/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents all links that transport activation from a competence module to
 * another competence module
 */
public class SuccessorLink extends CompetenceConnection
{
	/**
	 * Calls parent constructor
	 * @param params the network parameters that define the runtime dynamics of
	 *        the network
	 * @param source the source proposition of the activation
	 * @param destination the destination proposition
	 */
	public SuccessorLink(NetworkParams params, Proposition source, Proposition destination)
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
	 * Calculates the activation received directly or indirectly by goal i.
	 * @param goalIndex index of the goal from which the activation is originated
	 * @return the activation received directly or indirectly by goal i.
	 */
	@Override
	public double getGoalActivation(int goalIndex)
	{
		double goalActivation = sourceProposition.getTransferedActivation(goalIndex);
		double proposition = (1.0 - sourceProposition.getTruthValue());
		goalActivation = networkParams.getGamma() * proposition * goalActivation *
						 ((Effect) destinationProposition).getInfluence();

		if (goalIndex == 0) {
			this.activation = goalActivation;
		} else {
			this.activation += goalActivation;
		}

		return goalActivation;
	}
}
