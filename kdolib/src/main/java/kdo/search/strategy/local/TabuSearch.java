/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.local;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;

/**
 * Implementation of local search with tabu list for cycle prevention. In order
 * to work you have to make sure that you have implemented a meaningfull equals
 * method for your domain's state class!
 */
public class TabuSearch extends LocalSearchBase
{
	/** the size of the short term memory tabu list */
	private final int shortTermTabuSize;

	/** the short term tabu list to store states that are tabu */
	private final List<IProblemState> shortTermTabuList;

	/** the current best neighbor */
	protected IProblemState bestSuccessorState;

	/** the corresponding best operator that created the best neighbor */
	protected IOperator bestOperator;

	/**
	 * Constructor setting the size of the tabu list
	 * @param shortTermTabuSize the size of the short term tabu list
	 * @param maxIterations the maximal number of iterations done before stopping
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public TabuSearch(int shortTermTabuSize, long maxIterations, long maxRuntime)
	{
		this("Tabu Search. shortTermTabuSize: " + shortTermTabuSize + " maxIterations: " + maxIterations,
				shortTermTabuSize, maxIterations, maxRuntime);
	}

	/**
	 * Constructor setting the size of the tabu list
	 * @param name the name of the optimizer
	 * @param shortTermTabuSize the size of the short term tabu list
	 * @param maxIterations the maximal number of iterations done before stopping
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public TabuSearch(String name, int shortTermTabuSize, long maxIterations, long maxRuntime)
	{
		super(name, null, maxRuntime, maxIterations);
		this.shortTermTabuSize = shortTermTabuSize;
		shortTermTabuList = new LinkedList<>();
		bestSuccessorState = null;
		bestOperator = null;
	}

	/**
	 * Local search algorithm for tabu search.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	@Override
	public IProblemState search(IProblemState initialState)
	{
		// perform the search
		IProblem problem = initialState.getProblem();
		IProblemState currentState = initialState;
		IProblemState bestState = initialState;
		initialState.calculateUtility();

		onStartSearch();
		do {
			currentState = getCurrentState(problem, currentState);
			addToShortTermTabuList(currentState);
			bestSuccessorState = null;
			bestOperator = null;

			// get all possible state transition operators
			exploreNeighborhood(problem, currentState);

			// in case that no successor was found (all states run through were
			// tabu) we go on with the best state so far
			if (bestSuccessorState == null) {
				bestSuccessorState = bestState;
			}

			if (bestSuccessorState.getUtility() > bestState.getUtility()) {
				// we have found a new optimum
				bestState = bestSuccessorState;
				printState(bestState);
			}

			// remember current state in the short term tabu list
			currentState = performStateChange(problem, currentState, bestSuccessorState, bestOperator);

		} while (!canStop());

		return bestState;
	}

	/**
	 * @param problem
	 * @param currentState
	 */
	protected void exploreNeighborhood(IProblem problem, IProblemState currentState)
	{
		Iterator<IOperator> operators = neighborhoodIterator(currentState);

		while (canContinueCycle(problem, currentState, operators)) {
			IOperator operator = operators.next();
			// calculate the utility if we go this way
			IProblemState successorState = operator.getSuccessorState(currentState);
			successorState.calculateUtility();

			if (!isInTabuList(successorState)) {
				if (bestSuccessorState == null || bestSuccessorState.isWorseThan(successorState)) {
					// we have the steepest gradient so far that is not tabu
					bestSuccessorState = successorState;
					bestOperator = operator;
				}
			}
		}
	}

	/**
	 * @param state the state to check
	 * @return true if the state is in the tabu list
	 */
	protected boolean isInTabuList(IProblemState state)
	{
		return shortTermTabuList.contains(state);
	}

	/**
	 * Adds a problem state to the list of short termn tabu states, i.e. states
	 * that may not be visited in the next few steps
	 * @param state the state to add
	 */
	private void addToShortTermTabuList(IProblemState state)
	{
		shortTermTabuList.add(state);
		if (shortTermTabuList.size() > shortTermTabuSize) {
			shortTermTabuList.remove(0);
		}
	}

	@Override
	public String toString()
	{
		return super.toString() + " TabuSearch [shortTermTabuSize=" + shortTermTabuSize + "]";
	}
}
