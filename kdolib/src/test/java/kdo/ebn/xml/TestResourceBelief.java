/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

import kdo.ebn.IResourceBelief;

/**
 * Special implementation of a ResourceBelief for unit testing only
 *
 * @author Thomas Rinklin
 */
public class TestResourceBelief implements IResourceBelief
{
	private final String name;

	/**
	 * Constructor
	 *
	 * @param name Belief name
	 */
	public TestResourceBelief(String name)
	{
		this.name = name;
	}

	@Override
	public int getAmountAvailable()
	{
		return 0;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public float getTruthValue()
	{
		return 0;
	}
}
