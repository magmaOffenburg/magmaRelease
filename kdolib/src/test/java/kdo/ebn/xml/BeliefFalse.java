/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

/**
 * Belief for a proposition that is always false
 */
public class BeliefFalse extends Belief
{
	/**
	 * @param name the name used to display
	 */
	public BeliefFalse(String name)
	{
		super(name);
	}

	@Override
	public float getTruthValue()
	{
		return 0.0f;
	}
}
