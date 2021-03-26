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
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.base.RandomStrategy;
import kdo.util.IRandomSource;

/**
 * Implementation of hill climbing local search
 */
/**
 * @author kdorer
 *
 */
public abstract class LocalSearchBase extends RandomStrategy implements ILocalSearchStrategy
{
	/** the maximal runtime to run in ms */
	long maximalRuntime;

	/** the time of the last call to search */
	long searchStarttime;

	/** time of last call to printState */
	long lastprintStateTime;

	/** the number of cycles the optimization should run */
	protected long maxCycles;

	/** the number of cycles the optimization did run so far */
	protected long cycles;

	/**
	 * Constructor
	 * @param name the name of this strategy
	 * @param rand source for pseudo random numbers
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public LocalSearchBase(String name, IRandomSource rand, long maxRuntime)
	{
		this(name, rand, maxRuntime, -1);
	}

	/**
	 * Constructor
	 * @param name the name of this strategy
	 * @param rand source for pseudo random numbers
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 */
	public LocalSearchBase(String name, IRandomSource rand, long maxRuntime, long maxCycles)
	{
		super(name, rand);
		maximalRuntime = maxRuntime;
		if (maximalRuntime <= 0) {
			maximalRuntime = Long.MAX_VALUE;
		}
		lastprintStateTime = 0;
		this.maxCycles = maxCycles;
		if (maxCycles <= 0) {
			this.maxCycles = Long.MAX_VALUE;
		}
	}

	protected void onStartSearch()
	{
		searchStarttime = System.currentTimeMillis();
		cycles = 0;
	}

	/**
	 * Returns an iterator to the list of operators that lead to the neighborhood
	 * states of the state passed. The default implementation returns an iterator
	 * to all possible operators
	 *
	 * @param currentState the state of the domain for which to get the operators
	 * @return an iterator to the list of operators that lead to the neighborhood
	 *         states of the state passed
	 */
	protected Iterator<IOperator> neighborhoodIterator(IProblemState currentState)
	{
		// get all possible state transition operators
		return currentState.operatorIterator();
	}

	/**
	 * @return the runtime since last call to onStartSearch
	 */
	protected long getRuntime()
	{
		return System.currentTimeMillis() - searchStarttime;
	}

	/**
	 * Checks if the search algorithm can be stopped. The default implementation
	 * checks if the specified max runtime is used up.
	 *
	 * @return true if the algorithm can be stopped
	 */
	protected boolean canStop()
	{
		long runtime = getRuntime();
		if (runtime > maximalRuntime) {
			return true;
		}

		cycles++;
		if (cycles > maxCycles) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if this cycle of neighborhood exploration can be continued
	 * @param problem the problem to solve
	 * @param currentState the algorithm's current state
	 * @param operators the iterator to retrieve the neighborhood
	 * @return true if neighborhood exploration can be continued
	 */
	protected boolean canContinueCycle(IProblem problem, IProblemState currentState, Iterator<IOperator> operators)
	{
		if (!operators.hasNext()) {
			return false;
		}

		return true;
	}

	/**
	 * Returns the state on which to continue work. The default implementation
	 * just returns the passed state.
	 * @param problem the current problem to solve
	 * @param currentState the current state up to this call
	 * @return the passed state
	 */
	protected IProblemState getCurrentState(IProblem problem, IProblemState currentState)
	{
		return currentState;
	}

	/**
	 * Switches from current state to the best successor state passed.
	 * @param problem the current problem to solve
	 * @param currentState the current state up to this call
	 * @param bestSuccessorState the new state to switch to
	 * @param bestOperator the operator that created the new state
	 */
	protected IProblemState performStateChange(
			IProblem problem, IProblemState currentState, IProblemState bestSuccessorState, IOperator bestOperator)
	{
		currentState = bestSuccessorState;
		if (currentState != null && bestOperator != null) {
			// inform state that it was selected
			currentState.onSelection(bestOperator);
		}
		return currentState;
	}

	/**
	 * @return the remaining runtime in seconds
	 */
	protected long getRemainingRuntime()
	{
		return (maximalRuntime - getRuntime()) / 1000;
	}

	/**
	 * @param state the current state of searching
	 * @return true if we printed, false if not
	 */
	protected boolean printState(IProblemState state)
	{
		long nowTime = System.currentTimeMillis();
		long timeDelta = nowTime - lastprintStateTime;
		if (timeDelta > 1000) {
			String output = cycles + " utility: " + state.getUtility();
			if (maximalRuntime < Long.MAX_VALUE) {
				output += " time to go: " + getRemainingRuntime();
			} else {
				output += " time running: " + (getRuntime() / 1000);
			}
			System.out.println(output);
			lastprintStateTime = nowTime;
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "LocalSearchBase [maximalRuntime=" + maximalRuntime + ", searchStarttime=" + searchStarttime +
				", maxCycles=" + maxCycles + "]";
	}
}
