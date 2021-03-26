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
 * Implementation of hill climbing local search that caches states it visited.
 * Use this version of hill climbing, if calculating utility is expensive and
 * the problem is low dimensional, so the parent node is one of only few
 * neighbors.
 */
public class HillClimbingStateCache extends HillClimbing
{
	/** cache for states */
	private List<IProblemState> cache;

	private int cacheSize;

	/**
	 * Constructor
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 * @param cacheSize the maximum size of the state cache
	 */
	public HillClimbingStateCache(long maxRuntime, long maxCycles, int cacheSize)
	{
		super("HillClimbingStateCache", maxRuntime, maxCycles);
		this.cacheSize = cacheSize;
		cache = new LinkedList<>();
	}

	public void setCacheSize(int cacheSize)
	{
		this.cacheSize = cacheSize;
	}

	/**
	 * @param problem
	 * @param currentState
	 * @return
	 */
	@Override
	protected Iterator<IOperator> exploreNeighborhood(IProblem problem, IProblemState currentState)
	{
		Iterator<IOperator> operators = neighborhoodIterator(currentState);
		while (canContinueCycle(problem, currentState, operators)) {
			IOperator operator = operators.next();
			// calculate the utility if we go this way
			IProblemState successorState = operator.getSuccessorState(currentState);
			int index = cache.indexOf(successorState);
			if (index >= 0) {
				successorState = cache.get(index);
			} else {
				successorState.calculateUtility();
			}
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
	 * Switches from current state to the best successor state passed.
	 * @param problem the current problem to solve
	 * @param currentState the current state up to this call
	 * @param bestSuccessorState the new state to switch to
	 * @param bestOperator the operator that created the new state
	 */
	@Override
	protected IProblemState performStateChange(
			IProblem problem, IProblemState currentState, IProblemState bestSuccessorState, IOperator bestOperator)
	{
		cache.add(bestSuccessorState);
		if (cache.size() > cacheSize) {
			cache.remove(0);
		}
		return super.performStateChange(problem, currentState, bestSuccessorState, bestOperator);
	}
}
