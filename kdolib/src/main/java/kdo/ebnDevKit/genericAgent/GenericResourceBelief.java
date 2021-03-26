/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.genericAgent;

import kdo.ebn.IResourceBelief;

/**
 * generic resource belief
 *
 * @author Thomas Rinklin
 */
public class GenericResourceBelief implements IResourceBelief
{
	private final String name;

	/**
	 * constructor
	 * @param name of the resource
	 */
	public GenericResourceBelief(String name)
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
