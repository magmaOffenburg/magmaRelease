/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain.model.chromosom;

import kdo.domain.IChromosomStrategy;
import kdo.util.IRandomSource;

/**
 * Base class for chromosom creation strategies.
 *
 * @author kdorer
 */
public abstract class BaseChromosomStrategy implements IChromosomStrategy
{
	/** source for random numbers to use */
	protected IRandomSource rand;

	/** the size of a chromosom */
	private int size;

	/**
	 * Default constructor
	 * @param rand source for random numbers.
	 */
	public BaseChromosomStrategy(IRandomSource rand, int size)
	{
		super();
		this.rand = rand;
		this.size = size;
	}

	@Override
	public float[] getRandomChromosom()
	{
		float[] chromosom = new float[getChromosomSize()];
		for (int i = 0; i < chromosom.length; i++) {
			chromosom[i] = getRandomAlele(i, chromosom);
		}
		return chromosom;
	}

	@Override
	public int getChromosomSize()
	{
		return size;
	}
}
