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
 * Selects the parents of a crossover by chance influenced by the fitness
 * @author klaus
 *
 */
public abstract class RouletteWheelSelectionBase extends SelectionStrategy
{
	/** true if high values represent good utilities */
	private final boolean ascending;

	/**
	 * @param name the name of this strategy
	 * @param rand source for random numbers
	 * @param ascending true if high values represent good utilities
	 */
	public RouletteWheelSelectionBase(String name, IRandomSource rand, boolean ascending)
	{
		super(name, rand);
		this.ascending = ascending;
	}

	/*
	 * (non-Javadoc)
	 * @see search.strategy.local.genetic.selection.SelectionStrategy#onBeforeSelection(List)
	 */
	@Override
	public void onBeforeSelection(List<IIndividuum> oldIndividuums)
	{
		super.onBeforeSelection(oldIndividuums);
		// calculate criteria sum
		// for performance reasons it could make sense to make two methods
		// depending on ascending value
		int size = oldIndividuums.size();
		float criteriaSum = 0.0f;
		float maximum = Float.NEGATIVE_INFINITY;
		float minimum = Float.POSITIVE_INFINITY;
		float currentCriteria;
		int rank = size;
		for (IIndividuum current : oldIndividuums) {
			currentCriteria = getCriteria(current, rank--);
			criteriaSum += currentCriteria;
			if (currentCriteria < minimum) {
				minimum = currentCriteria;
			}
			if (currentCriteria > maximum) {
				maximum = currentCriteria;
			}
		}

		// calculate relative share of roulette wheel
		float currentShare = 0.0f;
		rank = size;
		if (ascending) {
			// adjust range of values
			criteriaSum -= minimum * size;

			for (IIndividuum current : oldIndividuums) {
				// cumulative normalized fitness
				currentShare += ((getCriteria(current, rank--) - minimum) / criteriaSum);
				current.setSelectionCriteria(currentShare);
			}
		} else {
			// adjust range of values
			criteriaSum = -(criteriaSum - maximum * size);

			for (IIndividuum current : oldIndividuums) {
				// cumulative normalized fitness
				currentShare += (maximum - getCriteria(current, rank--)) / criteriaSum;
				current.setSelectionCriteria(currentShare);
			}
		}
		// System.out.println(currentShare);
	}

	/**
	 * @param individuum the individuum for which to return the roulette wheel
	 *        criteria
	 * @param rank the rank of the individuum in the list of individuums
	 * @return the value of the selection criteria making the size of the
	 *         roulette wheel
	 */
	protected abstract float getCriteria(IIndividuum individuum, int rank);

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
		// select individuums randomly taking into account their fitness
		double rouletteWheel = rand.nextFloat();

		// find the individuum for the wheel value thrown
		// it is important that the individuums have not been resorted after the
		// call to onBeforeSelection!!
		int i = 0;

		for (IIndividuum currentIndividuum : oldIndividuums) {
			if (rouletteWheel <= currentIndividuum.getSelectionCriteria()) {
				// found the proper individuum
				selected[i]++;
				sumSelected[i]++;
				return currentIndividuum;
			}
			i++;
		}
		return oldIndividuums.get(oldIndividuums.size() - 1);
	}
}
