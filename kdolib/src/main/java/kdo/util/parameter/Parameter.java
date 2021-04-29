/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.parameter;

public class Parameter
{
	private String name;

	private float value;

	public Parameter(String name)
	{
		this.name = name;
		this.value = 0;
	}

	@Override
	public String toString()
	{
		return name + " = " + value;
	}

	public String getName()
	{
		return name;
	}

	public float getValue()
	{
		return value;
	}

	public void setValue(float value)
	{
		this.value = value;
	}
}
