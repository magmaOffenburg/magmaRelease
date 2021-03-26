/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

import kdo.ebn.IEBNPerception;

/**
 * Base class for all Beliefs
 */
public abstract class Belief implements IEBNPerception
{
	/** the name of the belief used to display */
	protected final String name;

	/**
	 * Constructor
	 *
	 * @param name Belief name
	 * @param thoughtModel Reference to the thought model object
	 */
	public Belief(String name)
	{
		this.name = name;
	}

	@Override
	public abstract float getTruthValue();

	@Override
	public String getName()
	{
		return name;
	}
}
