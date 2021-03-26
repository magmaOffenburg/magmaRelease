/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.selection;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.local.genetic.IPopulation;
import kdo.util.IRandomSource;

/**
 * Selects randomly from the best n individuums
 * @author klaus
 */
public class EliteSelection extends SelectionStrategy
{
	/** the number of best individuums to randomly select from */
	private int bestCount;

	/**
	 * @param bestCount the number of best individuums to randomly select from
	 * @param rand source for random numbers
	 */
	public EliteSelection(int bestCount, IRandomSource rand)
	{
		super("EliteSelection", rand);
		if (bestCount < 1) {
			bestCount = 1;
		}
		this.bestCount = bestCount;
	}

	/**
	 * @param population the population for which to do selection
	 * @param oldIndividuums the list of individuums from which to create new.
	 *        This list needs not to be identical to the individuums list of
	 *        population, e.g. the sorting may vary
	 * @param generated the number of individuums already generated for this
	 *        generation
	 * @param parents the so far selected individuums
	 * @param parentID the number of parents selected so far
	 * @return the new individuum that was created
	 */
	public IIndividuum selectIndividuum(IPopulation population, List<IIndividuum> oldIndividuums, int generated,
			IIndividuum[] parents, int parentID)
	{
		int size = oldIndividuums.size();
		int i = size - rand.nextInt(bestCount) - 1;
		selected[i]++;
		sumSelected[i]++;
		return oldIndividuums.get(i);
	}

	/**
	 * @param count the number of best individuums to choose from
	 */
	public void setEliteCount(int count)
	{
		bestCount = count;
		if (bestCount < 1) {
			bestCount = 1;
		}
	}
}
