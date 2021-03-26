/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain;

/**
 * @author klaus
 *
 */
public interface IIndividuum extends IOptimizationState {
	/**
	 * @return the chromosom information that represents the individuum
	 */
	float[] getChromosom();

	/**
	 * @param criteria a float value that is used by the selection strategy
	 */
	void setSelectionCriteria(float criteria);

	/**
	 * @return a float value that is used by the selection strategy
	 */
	float getSelectionCriteria();

	/**
	 * Mutates the ith gene by replacing it with a random allowed gene
	 * @param i the index of the gene to mutate
	 */
	void mutate(int i);

	/**
	 * @param i index of the chromosom
	 * @return true if the passed chromosom has been mutated
	 */
	boolean isMutated(int i);

	/**
	 * Gives access to the wrapped individuum in case this is a decorator or
	 * similar.
	 * @return the underlying real individuum that is possibly decorated by this
	 *         instance
	 */
	IIndividuum getRootIndividuum();

	/**
	 * Adds information from which parent a chromosom is
	 * @param parentID id for each chromosom's parent (1 based)
	 */
	void setParentIDs(int[] parentID);

	/**
	 * @param index the index of the chromosom for which to get the parentid
	 * @return the index of the passed chromosom's parent
	 */
	int getParentID(int index);

	/**
	 * Calculates the fitness of the individuum
	 */
	@Override
	float calculateFitness();

	/**
	 * @return the fitness of this individuum as a positive float value
	 */
	float getFitness();

	/**
	 * @param ucParameter
	 * @return the instance calculating the utility function
	 */
	IUtilityCalculator getUtilityCalculator(UtilityCalculatorParameters ucParameter);

	/**
	 * @return the age of this individuum
	 */
	int getAge();

	/**
	 * Increments the age of this individuum by one (generation)
	 */
	void incrementAge();
}
