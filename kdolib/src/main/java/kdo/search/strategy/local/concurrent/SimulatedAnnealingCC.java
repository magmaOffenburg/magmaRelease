/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.local.concurrent;

import kdo.domain.IConcurrentProblem;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.local.annealing.ITemperatureReductionStrategy;
import kdo.search.strategy.local.annealing.SimulatedAnnealing;

/**
 * Implementation of simulated annealing local search
 */
public class SimulatedAnnealingCC extends SimulatedAnnealing
{
	/**
	 * Creates a new simulated annealing instance with the passed starting
	 * temperature and number of temperature decreasing iterations.
	 * @param
	 * @param maxIterations the maximal number of iterations to run through
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public SimulatedAnnealingCC(ITemperatureReductionStrategy temperatureReduction, long maxIterations, long maxRuntime)
	{
		super("Simulated Annealing Concurrent. maxIterations: " + maxIterations +
						" temperature: " + temperatureReduction.toString(),
				temperatureReduction, maxIterations, maxRuntime);
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
}
