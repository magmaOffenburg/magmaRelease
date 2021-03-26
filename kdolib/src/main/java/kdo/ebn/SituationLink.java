/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents all links that connect perceptions and competence modules
 */
public class SituationLink extends Connection
{
	/**
	 * Calls parent constructor
	 * @param params the network parameters that define the runtime dynamics of
	 *        the network
	 * @param source the proposition associated with the perception
	 * @param destination the destination proposition of the competence module's
	 *        precondition
	 */
	public SituationLink(NetworkParams params, Proposition source, Proposition destination)
	{
		super(params, source, destination);
	}

	/**
	 * @return the activation of this link which is the truth value of the
	 *         perception
	 */
	@Override
	public double getActivation()
	{
		activation = destinationProposition.getTruthValue();

		return activation;
	}
}
