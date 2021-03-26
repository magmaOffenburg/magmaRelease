/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.selection;

import kdo.domain.IIndividuum;
import kdo.util.IRandomSource;

/**
 * Selects the parents of a crossover by chance influenced by the rank in the
 * list of individuums
 * @author klaus
 *
 */
public class RankSelection extends RouletteWheelSelectionBase
{
	/**
	 * @param ascending true if high values represent good utilities
	 * @param rand source for random numbers
	 */
	public RankSelection(boolean ascending, IRandomSource rand)
	{
		this("RankSelection", rand, ascending);
	}

	/**
	 * @param name the name of this strategy
	 * @param rand source for random numbers
	 * @param ascending true if high values represent good utilities
	 */
	public RankSelection(String name, IRandomSource rand, boolean ascending)
	{
		super(name, rand, ascending);
	}

	/**
	 * @param individuum the individuum for which to return the selection
	 *        criteria
	 * @param rank the rank of the individuum in the list of individuums
	 * @return the value of the selection criteria making the size of the
	 *         roulette wheel
	 */
	@Override
	protected float getCriteria(IIndividuum individuum, int rank)
	{
		return rank;
	}
}
