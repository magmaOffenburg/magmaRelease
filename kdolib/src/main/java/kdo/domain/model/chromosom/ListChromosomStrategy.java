/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model.chromosom;

import kdo.util.IRandomSource;

/**
 * Strategy returning a random value from a list of possible values. Genes are
 * not dependent on each other.
 *
 * @author kdorer
 */
public class ListChromosomStrategy extends BaseChromosomStrategy
{
	/** the possible values for each gene */
	private float[] values;

	/**
	 * Creates a list based alele strategy.
	 * @param rand source for random numbers.
	 * @param values the possible values from which to choose.
	 * @param size the chromosom size
	 */
	public ListChromosomStrategy(IRandomSource rand, float[] values, int size)
	{
		super(rand, size);
		this.rand = rand;
		this.values = values;
	}

	@Override
	public float getRandomAlele(int index, float[] chromosom)
	{
		return values[rand.nextInt(getChromosomSize())];
	}
}
