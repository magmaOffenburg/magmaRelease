/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

import kdo.ebn.IEBNPerception;

public class TestBelief implements IEBNPerception
{
	private final String name;

	public TestBelief(String name)
	{
		this.name = name;
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
