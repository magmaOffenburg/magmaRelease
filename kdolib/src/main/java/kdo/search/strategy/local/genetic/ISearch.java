/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic;

import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;

public interface ISearch extends ILocalSearchStrategy {
	/**
	 * Starts or continues the optimisation.
	 */
	IProblemState start();

	/**
	 * Breeds a single generation.
	 */
	IProblemState step();

	/**
	 * Call to interrupt the search for better solutions. Kept for downward
	 * compatibility.
	 */
	void pause();

	/**
	 * Call to interrupt the search for better solutions. Kept for downward
	 * compatibility.
	 */
	void stop();

	/**
	 * Restart the optimization with a new population.
	 */
	void rewind();

	/**
	 * Call to interrupt the search for better solutions. Kept for downward
	 * compatibility.
	 */
	void interrupt();

	/**
	 * Called to indicate a state/utility change
	 */
	void update(IOptimizationStateGroup population);

	/**
	 * @return the number of generations the population has been breeded
	 */
	int getCurrentIteration();

	/**
	 * @return the population
	 */
	IOptimizationStateGroup getPopulation();
}
