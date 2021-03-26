/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.local.concurrent;

import java.util.Iterator;
import kdo.domain.IConcurrentProblem;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.local.TabuSearch;

/**
 * Implementation of local search with tabu list for cycle prevention. In order
 * to work you have to make sure that you have implemented a meaningfull equals
 * method for your domain's state class!
 */
public class TabuSearchCC extends TabuSearch
{
	/**
	 * Constructor setting the size of the tabu list
	 * @param shortTermTabuSize the size of the short term tabu list
	 * @param maxIterations the maximal number of iterations done before stopping
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public TabuSearchCC(int shortTermTabuSize, long maxIterations, long maxRuntime)
	{
		super("Tabu Search Concurrent. shortTermTabuSize: " + shortTermTabuSize + " maxIterations: " + maxIterations,
				shortTermTabuSize, maxIterations, maxRuntime);
	}

	/**
	 * @return the concurrent current state of all optimizers
	 */
	@Override
	protected IProblemState getCurrentState(IProblem problem, IProblemState currentState)
	{
		return ((IConcurrentProblem) problem).getConcurrentState();
	}

	/**
	 * @param problem
	 * @param currentState
	 * @param bestSuccessorState
	 * @param bestOperator
	 */
	@Override
	protected IProblemState performStateChange(
			IProblem problem, IProblemState currentState, IProblemState bestSuccessorState, IOperator bestOperator)
	{
		currentState = ((IConcurrentProblem) problem).switchConcurrentState(currentState, bestSuccessorState);
		if (currentState != null && bestOperator != null) {
			// inform state that it was selected
			currentState.onSelection(bestOperator);
		}
		return currentState;
	}

	/**
	 * Checks if this cycle of neighborhood exploration can be continued
	 * @param problem the problem to solve
	 * @param currentState the algorithm's current state
	 * @param operators the iterator to retrieve the neighborhood
	 * @return true if neighborhood exploration can be continued
	 */
	@Override
	protected boolean canContinueCycle(IProblem problem, IProblemState currentState, Iterator<IOperator> operators)
	{
		// check if meanwhile the current state has changed
		if (((IConcurrentProblem) problem).concurrentStateHasChanged(currentState)) {
			return false;
		}

		return super.canContinueCycle(problem, currentState, operators);
	}
}
