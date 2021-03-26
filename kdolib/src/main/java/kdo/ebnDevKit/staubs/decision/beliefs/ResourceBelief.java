/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.decision.beliefs;

import kdo.ebn.IResourceBelief;

/**
 * Belief for a proposition that is always true
 */
public class ResourceBelief extends Belief implements IResourceBelief
{
	/** the number of resource units available */
	private final int maxAmount;

	public ResourceBelief(String name, int amount)
	{
		super(name, null);
		maxAmount = amount;
	}

	@Override
	public float getTruthValue()
	{
		return 1.0f;
	}

	@Override
	public int getAmountAvailable()
	{
		return maxAmount;
	}
}
