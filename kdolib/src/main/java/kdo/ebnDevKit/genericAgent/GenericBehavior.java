/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.genericAgent;

import kdo.ebn.IEBNAction;

/**
 * Generic Behavior behavior which just returns a name
 * @author Thomas Rinklin
 */
public class GenericBehavior implements IEBNAction
{
	private final String name;

	protected int performs;

	/**
	 * constructor
	 * @param name name of the behavior
	 */
	public GenericBehavior(String name)
	{
		this.name = name;
		this.performs = 0;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isFinished()
	{
		return true;
	}

	@Override
	public void perform()
	{
	}

	@Override
	public float getIntensity()
	{
		return 0;
	}

	@Override
	public void setIntensity(float intensity)
	{
	}
}
