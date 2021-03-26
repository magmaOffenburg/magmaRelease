/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.util;

/**
 * A non random implementation that returns always the specified value
 * @author klaus
 */
public class FixRandomSource implements IRandomSource
{
	/** the value to return to all requests */
	private double value;

	public FixRandomSource(double value)
	{
		this.value = value;
	}

	public boolean nextBoolean()
	{
		return (value > 0) ? true : false;
	}

	public double nextDouble()
	{
		return value;
	}

	public float nextFloat()
	{
		return (float) value;
	}

	public double nextGaussian()
	{
		return value;
	}

	public int nextInt(int n)
	{
		return (int) value;
	}
}
