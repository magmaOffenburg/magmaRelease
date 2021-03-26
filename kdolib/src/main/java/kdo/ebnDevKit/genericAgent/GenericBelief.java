/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.genericAgent;

import kdo.ebn.IEBNPerception;

/**
 * Generic belief
 * @author Thomas Rinklin
 */
public class GenericBelief implements IEBNPerception
{
	private final String name;

	/**
	 * constructor
	 * @param name name of the belief
	 */
	public GenericBelief(String name)
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
