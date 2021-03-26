/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

/**
 * Belief for a proposition that is always true
 */
public class BeliefTrue extends Belief
{
	/**
	 * @param name the name used to display
	 */
	public BeliefTrue(String name)
	{
		super(name);
	}

	@Override
	public float getTruthValue()
	{
		return 1.0f;
	}
}
