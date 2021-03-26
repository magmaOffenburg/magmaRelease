/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.logging;

import java.io.Serializable;

public class BehaviorData implements Serializable
{
	public static final long serialVersionUID = 4295507372474569249L;

	private final String name;

	private final double performs;

	private final double performedPercentage;

	public BehaviorData(String name, int performs, double performedPercentage)
	{
		this.name = name;
		this.performs = performs;
		this.performedPercentage = performedPercentage;
	}

	public String getName()
	{
		return name;
	}

	public double getPerforms()
	{
		return performs;
	}

	public double getPerformedPercentage()
	{
		return performedPercentage;
	}

	@Override
	public String toString()
	{
		return name + " " + performs + " " + performedPercentage + "%";
	}
}
