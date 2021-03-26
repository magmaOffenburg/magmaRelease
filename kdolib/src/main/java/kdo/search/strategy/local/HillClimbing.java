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
import kdo.domain.IIncrementalNeighborhoodIterator;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;

/**
 * Implementation of hill climbing local search
 */
public class HillClimbing extends LocalSearchBase
{
	/** the current best neighbor */
	protected IProblemState bestSuccessorState;

	/** the corresponding best operator that created the best neighbor */
	protected IOperator bestOperator;

	/**
	 * Constructor
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public HillClimbing(long maxRuntime)
	{
		super("HillClimbing", null, maxRuntime);
	}

	/**
	 * Constructor
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 */
	public HillClimbing(long maxRuntime, long maxCycles)
	{
		super("HillClimbing", null, maxRuntime, maxCycles);
	}

	/**
	 * Constructor
	 * @param name the name of the optimizer
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 */
	public HillClimbing(String name, long maxRuntime, long maxCycles)
	{
		super(name, null, maxRuntime, maxCycles);
	}

	/**
	 * Local search algorithm for hill climbing that does a steepest gradient
	 * ascend.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	public IProblemState search(IProblemState initialState)
	{
		IProblem problem = initialState.getProblem();
		IProblemState currentState = initialState;
		currentState.calculateUtility();

		// perform the search
		onStartSearch();
		while (true) {
			currentState = getCurrentState(problem, currentState);
			bestSuccessorState = currentState;
			bestOperator = null;

			// get a list of all directions (operators) where to go
			Iterator<IOperator> operators = exploreNeighborhood(problem, currentState);

			printState(bestSuccessorState);

			if (canStopHillClimbing(problem, bestSuccessorState, currentState, operators)) {
				// we have reached a (local) maximum
				// we return bestSuccessor, since it is either identical to
				// currentState or we found a better state but have been interrupted
				// by timeout
				return bestSuccessorState;
			}

			currentState = performStateChange(problem, currentState, bestSuccessorState, bestOperator);
		}
	}

	/**
	 * @param problem
	 * @param currentState
	 * @return
	 */
	protected Iterator<IOperator> exploreNeighborhood(IProblem problem, IProblemState currentState)
	{
		Iterator<IOperator> operators = neighborhoodIterator(currentState);
		while (canContinueCycle(problem, currentState, operators)) {
			IOperator operator = operators.next();
			// calculate the utility if we go this way
			IProblemState successorState = operator.getSuccessorState(currentState);
			successorState.calculateUtility();
			if (bestSuccessorState.isWorseThan(successorState)) {
				// we have a steeper gradient
				bestSuccessorState = successorState;
				bestOperator = operator;
				// System.out.println(bestSuccessorState.getUtility());
			}
		}
		return operators;
	}

	/**
	 * Checks if the hill climbing algorithm can be stopped. The default
	 * implementation checks if the best state to go is the same state where we
	 * are.
	 *
	 * @param problem the problem to solve
	 * @param bestSuccessorState the best state of the neighborhood to which to
	 *        go
	 * @param currentState the current state where we are
	 * @param neighborhood the iterator returning the neighborhood operators
	 * @return true if the algorithm can be stopped
	 */
	protected boolean canStopHillClimbing(IProblem problem, IProblemState bestSuccessorState,
			IProblemState currentState, Iterator<IOperator> neighborhood)
	{
		if (bestSuccessorState == currentState) {
			if (neighborhood instanceof IIncrementalNeighborhoodIterator<?>) {
				// the iterator supports incremental neighborhoods
				if (((IIncrementalNeighborhoodIterator<IOperator>) neighborhood).nextNeighborhood()) {
					// there are more neighbors to check, but check timeouts
					return super.canStop();
				}
			}
			// found a local minimum
			return true;
		}

		return super.canStop();
	}
}
