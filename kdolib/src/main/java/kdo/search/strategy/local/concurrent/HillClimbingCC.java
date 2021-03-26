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
import kdo.search.strategy.local.HillClimbing;

/**
 * Implementation of hill climbing local search
 */
public class HillClimbingCC extends HillClimbing
{
	/**
	 * Constructor
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 */
	public HillClimbingCC(long maxRuntime, long maxCycles)
	{
		super("HillClimbingCC", maxRuntime, maxCycles);
	}

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

	/**
	 * Checks if the hill climbing algorithm can be stopped. The default
	 * implementation checks if the best state to go is the same state where we
	 * are.
	 *
	 * @param problem the current problem to solve
	 * @param bestSuccessorState the best state of the neighborhood to which to
	 *        go
	 * @param currentState the current state where we are
	 * @return true if the algorithm can be stopped
	 */
	@Override
	protected boolean canStopHillClimbing(IProblem problem, IProblemState bestSuccessorState,
			IProblemState currentState, Iterator<IOperator> neighborhood)
	{
		// check runtime and max cycles
		// TODO: may be we should not do this at each call to save some time
		if (super.canStop()) {
			return true;
		}

		// if the current state of the problem has changed we have to continue
		if (((IConcurrentProblem) problem).concurrentStateHasChanged(currentState)) {
			return false;
		}

		// the check of canStop() is done twice, but without the first check
		// we might never end on timeout if the state is changed by someone else.
		return super.canStopHillClimbing(problem, bestSuccessorState, currentState, neighborhood);
	}
}
