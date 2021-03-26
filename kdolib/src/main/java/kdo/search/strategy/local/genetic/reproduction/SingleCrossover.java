/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.reproduction;

import java.util.Arrays;
import kdo.util.IRandomSource;

/**
 * @author klaus
 *
 */
public class SingleCrossover extends Crossover
{
	/**
	 * @param rand the source for random numbers
	 */
	public SingleCrossover(IRandomSource rand)
	{
		super("SingleCrossover", rand);
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
		assert parents.length == 2 : "Does currently not support more than 2 parents";
		int size = parents[0].length;
		// get random chromosom position for crossover point
		// at least one gene is taken from each parent
		int crossoverIndex = rand.nextInt(size - 1) + 1;

		// create new chromosom from first and second parent
		float[] newChromosom = new float[size];
		System.arraycopy(parents[0], 0, newChromosom, 0, crossoverIndex);
		System.arraycopy(
				parents[1], crossoverIndex, newChromosom, crossoverIndex, newChromosom.length - crossoverIndex);

		int[] parentID = new int[size];
		Arrays.fill(parentID, 0, crossoverIndex, 1);
		Arrays.fill(parentID, crossoverIndex, size, 2);

		return new CrossoverInfo(newChromosom, parentID);
	}
}
