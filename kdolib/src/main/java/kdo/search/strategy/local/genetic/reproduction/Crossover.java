/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.reproduction;

import kdo.domain.IGeneticProblem;
import kdo.domain.IIndividuum;
import kdo.search.strategy.base.RandomStrategy;
import kdo.search.strategy.local.genetic.IReproduction;
import kdo.util.IRandomSource;

/**
 * @author klaus
 *
 */
public abstract class Crossover extends RandomStrategy implements IReproduction
{
	/**
	 * @param name the name of this strategy
	 * @param rand the source for random numbers
	 */
	public Crossover(String name, IRandomSource rand)
	{
		super(name, rand);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * search.strategy.local.genetic.IReproduction#crossOver(domain.IIndividuum,
	 * domain.IIndividuum)
	 */
	@Override
	public IIndividuum crossOver(IIndividuum[] parents)
	{
		int size = parents.length;
		float[][] parentChromosoms = new float[size][];
		for (int i = 0; i < size; i++) {
			parentChromosoms[i] = parents[i].getChromosom();
		}

		// get random order of parents
		swapRandomly(parentChromosoms);

		// create the chromosom of the new individuum
		CrossoverInfo result = createNewChromosom(parentChromosoms);
		float[] newChromosom = result.getChromosom();

		// create new individuum
		IIndividuum newIndividuum = ((IGeneticProblem) parents[0].getProblem()).createIndividuum(newChromosom);
		newIndividuum.setParentIDs(result.getParentID());

		return newIndividuum;
	}

	protected abstract CrossoverInfo createNewChromosom(float[][] parents);

	protected void swapRandomly(float[][] parents)
	{
		if (rand.nextBoolean()) {
			float[] temp = parents[0];
			parents[0] = parents[1];
			parents[1] = temp;
		}
	}
}
