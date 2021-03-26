/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.mutation;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.base.RandomStrategy;
import kdo.search.strategy.local.genetic.IMutation;
import kdo.util.IRandomSource;

/**
 * Mutation strategy that selects individuums with one probability for mutation
 * and has another probability to select a gene for mutation
 *
 * @author klaus
 */
public class RandomMutation extends RandomStrategy implements IMutation
{
	/** probability with which to select an individuum for mutation */
	private float individuumSelectionProbability;

	/** probability with which to mutate a gene */
	private float geneMutationProbability;

	/**
	 * @param individuumSelectionProbability probability with which to select an
	 *        individuum for mutation
	 * @param geneMutationProbability probability with which to mutate a gene
	 * @param rand source for random numbers
	 */
	public RandomMutation(float individuumSelectionProbability, float geneMutationProbability, IRandomSource rand)
	{
		super("RandomMutation", rand);
		this.individuumSelectionProbability = individuumSelectionProbability;
		this.geneMutationProbability = geneMutationProbability;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.local.genetic.StrategyBase#getName()
	 */
	@Override
	public String toString()
	{
		return getName() + " individuumSelectionProbability: " + individuumSelectionProbability +
				" geneMutationProbability: " + geneMutationProbability;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.local.genetic.IMutation#mutate()
	 */
	@Override
	public void mutate(List<IIndividuum> individuums)
	{
		if (individuumSelectionProbability <= 0) {
			// mutation is switched off
			return;
		}

		// run through each individuum of the population
		for (IIndividuum current : individuums) {
			if (rand.nextFloat() < individuumSelectionProbability) {
				// run through each gene for mutation check
				float[] chromosom = current.getChromosom();
				for (int i = 0; i < chromosom.length; i++) {
					if (rand.nextFloat() < geneMutationProbability) {
						// mutation is done
						current.mutate(i);
					}
				}
			}
		}
	}

	@Override
	public void setIndividuumMutationProbability(float individuumMutationProbability)
	{
		this.individuumSelectionProbability = individuumMutationProbability;
	}

	@Override
	public void setGeneMutationProbability(float geneMutationProbability)
	{
		this.geneMutationProbability = geneMutationProbability;
	}
}
