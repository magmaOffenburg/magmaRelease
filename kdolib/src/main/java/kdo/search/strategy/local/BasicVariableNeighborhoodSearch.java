/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local;

import kdo.domain.IOperator;
import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;

/**
 * @author klaus
 *
 */
public class BasicVariableNeighborhoodSearch extends LocalSearchBase
{
	/** local search instance to run on */
	ILocalSearchStrategy localSearch;

	/** the kmax maximal radius that is explored */
	private final int maxNeighborhoodRadius;

	/**
	 * @param localSearch the local search strategy to run
	 * @param maxNeighborhoodRadius the kmax maximal radius that is explored
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public BasicVariableNeighborhoodSearch(ILocalSearchStrategy localSearch, int maxNeighborhoodRadius, long maxRuntime)
	{
		this(localSearch, maxNeighborhoodRadius, maxRuntime, -1);
	}

	/**
	 * @param localSearch the local search strategy to run
	 * @param maxNeighborhoodRadius the kmax maximal radius that is explored
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 */
	public BasicVariableNeighborhoodSearch(
			ILocalSearchStrategy localSearch, int maxNeighborhoodRadius, long maxRuntime, long maxCycles)
	{
		super("Basic Variable Neighborhood Search. maxNeighborhoodRadius: " + maxNeighborhoodRadius, null, maxRuntime,
				maxCycles);
		this.localSearch = localSearch;
		this.maxNeighborhoodRadius = maxNeighborhoodRadius;
	}

	/**
	 * Local search algorithm for variable neighborhood search that goes to
	 * randomly generated kth neighborhood and does local search there.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	public IProblemState search(IProblemState initialState)
	{
		// stopping condition is max runtime
		int neighborhoodRadius = 1;
		IProblemState currentState = initialState;
		IProblemState bestState = currentState;

		onStartSearch();
		while (neighborhoodRadius < maxNeighborhoodRadius && !canStop()) {
			// local search
			currentState = localSearch.search(currentState);

			// move or not
			if (bestState.isWorseThan(currentState)) {
				bestState = currentState;
				neighborhoodRadius = 1;
			} else {
				neighborhoodRadius++;
			}

			// shaking
			IOperator randomOperator = bestState.getRandomOperator(neighborhoodRadius);
			currentState = randomOperator.getSuccessorState(bestState);
			currentState.calculateUtility();
			System.out.println("Shaking utility: " + currentState.getUtility() +
							   " neighborhood: " + neighborhoodRadius + " time to go: " + getRemainingRuntime());
		}
		return bestState;
	}
}
