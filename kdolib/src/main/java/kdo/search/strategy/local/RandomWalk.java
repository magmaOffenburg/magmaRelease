/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.local;

import kdo.domain.IOperator;
import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;

/**
 * Represents a hill climbing local search with random walk steps when reaching
 * a (local) maximum. The number of steps can be passed to the constructor.
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.5 $
 */
public class RandomWalk extends LocalSearchBase
{
	/** the hill climbing instance that is used for improvement */
	private final ILocalSearchStrategy hillClimbing;

	/** the number of random walk steps to be done at a (local) maximum */
	private final int randomWalkSteps;

	/**
	 * Creates a new instance of random walk local improvement.
	 * @param steps the number of random walk steps to be done at a (local)
	 *        maximum
	 * @param iterations the maximal number of iterations the algorithm is run
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public RandomWalk(int steps, long iterations, long maxRuntime)
	{
		super("Random Walk. randomWalkSteps: " + steps + " maxIterations: " + iterations, null, maxRuntime, iterations);
		randomWalkSteps = steps;
		hillClimbing = new HillClimbing(maxRuntime);
	}

	/**
	 * Performs a hill climbing local search with random walk steps when reaching
	 * a (local) maximum.
	 *
	 * @param initialState the state to start the search from
	 * @return the best state found during search
	 *
	 * @see kdo.search.strategy.ILocalSearchStrategy#search(kdo.domain.IProblem,
	 *      kdo.domain.IProblemState)
	 */
	@Override
	public IProblemState search(IProblemState initialState)
	{
		IProblemState bestState = initialState;
		IProblemState currentState = initialState;

		onStartSearch();
		do {
			// do improvement
			currentState = hillClimbing.search(currentState);

			if (bestState.isWorseThan(currentState)) {
				bestState = currentState;
			}

			// do random walk
			IOperator randomOperator;
			for (int i = 0; i < randomWalkSteps; i++) {
				randomOperator = currentState.getRandomOperator();
				currentState = randomOperator.getSuccessorState(currentState);
				currentState.calculateUtility();
				// System.out.println(currentState.getUtility());
			}

			printState(bestState);

		} while (!canStop());

		return bestState;
	}
}
