/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic;

import java.util.List;
import kdo.domain.IOptimizationState;

/**
 * @author klaus
 *
 */
public interface IOptimizationStateGroup {
	/**
	 * @return the best state of the stateGroup according to the fitness
	 *         function, null if utility calculation is expensive and we have not
	 *         breeded yet
	 */
	IOptimizationState getBestState();

	/**
	 * Creates a randomly created state from the application domain this
	 * stateGroup is based on. The state is not added to the stateGroup.
	 * @return a randomly created state from the application domain this
	 *         stateGroup is based on
	 */
	IOptimizationState createRandomState();

	/**
	 * @return the number of states in the stateGroup
	 */
	int getSize();

	/**
	 * @return the number of generations this stateGroup has been breeded
	 */
	int getIterations();

	/**
	 * @return the average diversity of all genes in the stateGroup
	 */
	float getAverageDiversity();

	/**
	 * @return the average utility of all genes in the stateGroup
	 */
	double getAverageUtility();

	/**
	 * @return the time that breeding has taken until now
	 */
	long getRuntime();

	/**
	 * @return returns a sorted list of all states
	 */
	List<IOptimizationState> getSortedStates();

	/**
	 * @return true if the utilities have once been calculated, false for a fresh
	 *         created stateGroup
	 */
	boolean isUtilitiesCalculated();
}
