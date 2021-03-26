/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.cmaes.impl;

import kdo.util.IRandomSource;
import org.apache.commons.math3.random.RandomGenerator;

class RandomSourceAdapter implements IRandomSource, RandomGenerator
{
	private RandomGenerator randomSource;

	public RandomSourceAdapter(RandomGenerator adaptee)
	{
		randomSource = adaptee;
	}

	@Override
	public int nextInt(int n)
	{
		return randomSource.nextInt();
	}

	@Override
	public float nextFloat()
	{
		return randomSource.nextFloat();
	}

	@Override
	public double nextDouble()
	{
		return randomSource.nextDouble();
	}

	@Override
	public boolean nextBoolean()
	{
		return randomSource.nextBoolean();
	}

	@Override
	public double nextGaussian()
	{
		return randomSource.nextGaussian();
	}

	@Override
	public void setSeed(int seed)
	{
		randomSource.setSeed(seed);
	}

	@Override
	public void setSeed(int[] seed)
	{
		randomSource.setSeed(seed);
	}

	@Override
	public void setSeed(long seed)
	{
		randomSource.setSeed(seed);
	}

	@Override
	public void nextBytes(byte[] bytes)
	{
		randomSource.nextBytes(bytes);
	}

	@Override
	public int nextInt()
	{
		return randomSource.nextInt();
	}

	@Override
	public long nextLong()
	{
		return randomSource.nextLong();
	}
}