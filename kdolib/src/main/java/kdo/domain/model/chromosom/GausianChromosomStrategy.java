/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model.chromosom;

import kdo.util.IRandomSource;

/**
 * Strategy returning a gausian distributed value for each gene. Genes are not
 * dependent on each other.
 *
 * @author kdorer
 */
public class GausianChromosomStrategy extends BaseChromosomStrategy
{
	/** the mean value for each gene */
	private float[] mean;

	/** the standard deviation for each gene */
	private float[] stddev;

	/**
	 * Creates a uniformly distributed alele strategy.
	 * @param rand source for random numbers.
	 * @param mean the mean value for each gene
	 * @param stddev the standard deviation for each gene
	 */
	public GausianChromosomStrategy(IRandomSource rand, float[] mean, float[] stddev)
	{
		super(rand, mean.length);
		this.mean = mean;
		this.stddev = stddev;
	}

	/**
	 * Creates a uniformly distributed alele strategy.
	 * @param rand source for random numbers.
	 * @param mean the mean value for each gene
	 * @param deviation the percentual deviation from mean value
	 */
	public GausianChromosomStrategy(IRandomSource rand, float[] mean, float deviation)
	{
		super(rand, mean.length);
		this.mean = mean;
		this.stddev = new float[mean.length];
		for (int i = 0; i < stddev.length; i++) {
			if (Math.abs(mean[i]) < 0.01f) {
				// workaround: if mean is 0 we will never have different values
				this.stddev[i] = 0.01f;
			} else {
				this.stddev[i] = Math.abs(mean[i] * deviation);
			}
		}
	}

	/**
	 * Creates a uniformly distributed alele strategy.
	 * @param rand source for random numbers.
	 * @param mean the mean for each gene
	 * @param stddev the standard deviation used for Gausian values
	 * @param size the size of the entire chromosom
	 */
	public GausianChromosomStrategy(IRandomSource rand, float mean, float stddev, int size)
	{
		super(rand, size);
		this.mean = new float[size];
		this.stddev = new float[size];
		for (int i = 0; i < size; i++) {
			this.mean[i] = mean;
			this.stddev[i] = stddev;
		}
	}

	@Override
	public float getRandomAlele(int index, float[] chromosom)
	{
		return (float) rand.nextGaussian() * stddev[index] + mean[index];
	}
}
