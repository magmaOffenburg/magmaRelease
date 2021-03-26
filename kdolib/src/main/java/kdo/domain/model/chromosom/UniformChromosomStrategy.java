/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model.chromosom;

import kdo.util.IRandomSource;

/**
 * Strategy returning a uniformly distributed value between specified border
 * values for each gene. Genes are not dependent on each other.
 *
 * @author kdorer
 */
public class UniformChromosomStrategy extends BaseChromosomStrategy
{
	/** the min and max values for each gene */
	private float[][] area;

	/**
	 * Creates a uniformly distributed alele strategy.
	 * @param rand source for random numbers.
	 * @param area min and max values for each gene. Min has to be less for max
	 *        in each case.
	 */
	public UniformChromosomStrategy(IRandomSource rand, float[][] area)
	{
		super(rand, area.length);
		checkArea(area);
		this.area = area;
	}

	/**
	 * Creates a uniformly distributed alele strategy.
	 * @param rand source for random numbers.
	 * @param minValue the minimal alele value for each gene.
	 * @param maxValue the maximal alele value for each gene.
	 * @param size the size of the entire chromosom
	 */
	public UniformChromosomStrategy(IRandomSource rand, float minValue, float maxValue, int size)
	{
		super(rand, size);
		this.area = new float[size][2];
		for (float[] entry : area) {
			entry[0] = minValue;
			entry[1] = maxValue;
		}
	}

	@Override
	public float getRandomAlele(int index, float[] chromosom)
	{
		float[] geneArea = area[index];
		return rand.nextFloat() * (geneArea[1] - geneArea[0]) + geneArea[0];
	}

	private void checkArea(float[][] area)
	{
		for (float[] entry : area) {
			if (entry[0] > entry[1]) {
				throw new IllegalArgumentException(
						"area values should be sorted, but are: min:" + entry[0] + " max: " + entry[1]);
			}
		}
	}
}
