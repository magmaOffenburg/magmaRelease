/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.reproduction;

import kdo.util.IRandomSource;

/**
 * For each chromosom it is randomly chosen, from which parent it comes
 * @author klaus
 *
 */
public class MultiCrossover extends Crossover
{
	/**
	 * @param rand the source for random numbers
	 */
	public MultiCrossover(IRandomSource rand)
	{
		super("MultiCrossover", rand);
	}

	/**
	 * @param parents the two parent individuums from which to create the new
	 *        individuum
	 * @return the new chromosom information as a two parts mix of the two
	 *         parents
	 */
	@Override
	protected CrossoverInfo createNewChromosom(float[][] parents)
	{
		int size = parents[0].length;
		float[] newChromosom = new float[size];
		int[] parentID = new int[size];

		// run through all chromosoms and take randomly
		for (int i = 0; i < size; i++) {
			int which = rand.nextInt(parents.length);
			newChromosom[i] = parents[which][i];
			parentID[i] = which + 1;
		}
		return new CrossoverInfo(newChromosom, parentID);
	}
}
