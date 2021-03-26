/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.local;

import kdo.domain.IProblem;
import kdo.domain.IProblemState;

/**
 * Implementation of hill climbing local search
 */
public class HillClimbingRandomRestart extends HillClimbing
{
	/** the number of random restarts to perform */
	int restarts;

	/**
	 * Constructor
	 * @param restarts the number of restarts to perform
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public HillClimbingRandomRestart(int restarts, long maxRuntime)
	{
		this(restarts, maxRuntime, -1);
	}

	/**
	 * Constructor
	 * @param restarts the number of restarts to perform
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param maxCycles the number of cycles the optimization should run if not
	 *        maxRuntime is reached first
	 */
	public HillClimbingRandomRestart(int restarts, long maxRuntime, long maxCycles)
	{
		super(maxRuntime, maxCycles);
		this.restarts = restarts;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.IStrategy#getName()
	 */
	@Override
	public String getName()
	{
		return "HillClimbing Random Restart. restarts: " + restarts;
	}

	/**
	 * Local search algorithm for hill climbing that does a steepest gradient
	 * ascend.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	@Override
	public IProblemState search(IProblemState initialState)
	{
		IProblemState bestState = null;
		IProblem problem = initialState.getProblem();
		IProblemState currentState = initialState;
		currentState.calculateUtility();
		onStartSearch();
		for (int i = 0; i < restarts && !canStop(); i++) {
			// perform hill climbing
			IProblemState resultState = super.search(currentState);

			// remember best result
			if (bestState == null || resultState.getUtility() > bestState.getUtility()) {
				bestState = resultState;
			}

			System.out.println("Result of iteration " + i + "/" + restarts + ": " + resultState.getUtility());

			// create random start state
			currentState = problem.getRandomState();
			currentState.calculateUtility();
			problem.setCurrentState(currentState);
		}
		return bestState;
	}
}
