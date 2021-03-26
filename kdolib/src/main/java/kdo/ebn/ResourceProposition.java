/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * A resource proposition extends an ordinary proposition by also modelling the
 * amount of resource units used.
 */
public class ResourceProposition extends Proposition
{
	/** the amount of resource units this proposition can use */
	private final int amountUsed;

	/**
	 * Calls parent constructor, sets containing component and amountUsed
	 * attribute
	 * @param parent the containing network node of this proposition
	 * @param perceptionNode the perception node that this proposition is
	 *        associated with
	 * @param amountUsed the amount of resource units this proposition will use
	 */
	public ResourceProposition(NetworkNode parent, PerceptionNode perceptionNode, int amountUsed)
	{
		super(parent, perceptionNode, false);
		this.amountUsed = amountUsed;
	}

	/**
	 * @return the amount of resource units that this resource proposition will
	 *         use
	 */
	public int getAmountUsed()
	{
		return amountUsed;
	}

	/**
	 * @param propName name of the proposition to look for
	 * @param ebn the ebn to which we belong
	 * @return the resource node with the passed name if existing, a new resource
	 *         node otherwise
	 * @throws NetworkConfigurationException if no corresponding belief is
	 *         available to ground the perception
	 */
	@Override
	NetworkNode getOrCreateNetworkNode(String propName, ExtendedBehaviorNetwork ebn)
			throws NetworkConfigurationException
	{
		return ebn.getOrCreateResource(propName);
	}
}
