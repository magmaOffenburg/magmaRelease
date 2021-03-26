/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic;

import java.util.Comparator;
import kdo.domain.IOptimizationState;

/**
 * @author klaus
 *
 */
public interface IPopulation extends IOptimizationStateGroup {
	/**
	 * @return the number of different genders used
	 */
	int getGenders();

	/**
	 * @return the number of parents of each individuum
	 */
	int getParentsPerIndividuum();

	/**
	 * @return the number of generations this population has been breeded
	 */
	int getGenerations();

	/**
	 * @return the average diversity of all genes in the population
	 */
	@Override
	float getAverageDiversity();

	void setReproductionStrategy(IReproduction reproductionStrategy);

	void setMutationStrategy(IMutation mutationStrategy);

	void setSelectionStrategy(ISelection selectionStrategy);

	/**
	 * @param value the number of individuums to take over from an old into a new
	 *        generation
	 */
	void setOldToNew(float value);

	/**
	 * @param value the probability with which to select an individuum for
	 *        mutation
	 */
	void setIndividuumMutationProbability(float value);

	/**
	 * @param value the probability with which to mutate a gene
	 */
	void setGeneMutationProbability(float value);

	/**
	 * @param the comparator to use to compare individuums
	 * @return the best individuum of the population according to the passed
	 *         comparator, null if utility calculation is expensive and we have
	 *         not breeded yet
	 */
	IOptimizationState getBestIndividuum(Comparator<IOptimizationState> comparator);
}
