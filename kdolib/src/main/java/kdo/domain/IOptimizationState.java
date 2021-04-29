/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain;

/**
 * @author cfueller
 *
 */
public interface IOptimizationState extends IProblemState, Comparable<IOptimizationState> {
	/**
	 * @param other the state to compare against
	 * @return the relative number of values that differ for this and the passed
	 *         state
	 */
	float getRelativeDiversity(IOptimizationState other);

	/**
	 * Checks the consistency of this state to be defined by each problem state
	 * separately
	 */
	@Override
	void checkConsistency();

	/**
	 * Gets the values of the optimization state
	 * @return The state as a array of float values
	 */
	float[] getState();

	/**
	 * Gets the iterations passed for this ProblemState
	 * @return the iterations passed
	 */
	int getIteration();

	/**
	 * increments the iterations passed for this ProblemState
	 */
	void incrementIteration();

	/**
	 * calculates the utility
	 * @return the calculated utility
	 */
	float calculateFitness();

	/**
	 * Append utility and gene information to text file generesult.txt
	 */
	void storeResult();
}
