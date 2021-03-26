/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.xml;

import kdo.ebn.IEBNAction;

public class TestBehavior implements IEBNAction
{
	private final String name;

	public TestBehavior(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean isFinished()
	{
		return false;
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
