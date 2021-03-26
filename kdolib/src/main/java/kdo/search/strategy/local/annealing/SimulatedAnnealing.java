/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.local.annealing;

import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.local.LocalSearchBase;

/**
 * Implementation of simulated annealing local search
 */
public class SimulatedAnnealing extends LocalSearchBase
{
	/** the strategy used for temperature reduction */
	private final ITemperatureReductionStrategy temperatureReduction;

	/**
	 * Creates a new simulated annealing instance with the passed starting
	 * temperature and number of temperature decreasing iterations.
	 * @param
	 * @param maxIterations the maximal number of iterations to run through
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public SimulatedAnnealing(ITemperatureReductionStrategy temperatureReduction, long maxIterations, long maxRuntime)
	{
		this("Simulated Annealing. maxIterations: " + maxIterations +
						" temperature: " + temperatureReduction.toString(),
				temperatureReduction, maxIterations, maxRuntime);
	}

	/**
	 * Creates a new simulated annealing instance with the passed starting
	 * temperature and number of temperature decreasing iterations.
	 * @param
	 * @param maxIterations the maximal number of iterations to run through
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public SimulatedAnnealing(
			String name, ITemperatureReductionStrategy temperatureReduction, long maxIterations, long maxRuntime)
	{
		super(name, null, maxRuntime, maxIterations);
		this.temperatureReduction = temperatureReduction;
	}

	/**
	 * Special search algorithm for simulated annealing that does not expand a
	 * node completely, but only expands the randomly selected successor.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	public IProblemState search(IProblemState initialState)
	{
		// perform the search
		IProblem problem = initialState.getProblem();
		IProblemState currentState = initialState;
		currentState.calculateUtility();
		// IProblemState allTimeBest = initialState;

		onStartSearch();
		do {
			double temperature = temperatureReduction.adjustTemperature(cycles, maxCycles);

			// choose one operator randomly
			currentState = getCurrentState(problem, currentState);
			IOperator operator = currentState.getRandomOperator();
			IProblemState newState = operator.getSuccessorState(currentState);
			newState.calculateUtility();
			IProblemState nextState = null;

			// calculate utility difference
			double deltaE = (newState.getUtility() - currentState.getUtility());
			if (deltaE > 0.0) {
				// if state improves we take it
				nextState = newState;
			} else {
				double criteria = Math.exp(deltaE / temperature);
				double random = Math.random();
				if (criteria > random) {
					// with some probability we go upward
					nextState = newState;
				}
			}

			if (nextState != null) {
				currentState = performStateChange(problem, currentState, nextState, operator);
			}

			// if (newState.getUtility() > allTimeBest.getUtility()) {
			// allTimeBest = newState;
			// }
			//
			// decrease temperature every cycle

			// debug only
			printState(currentState);

		} while (!canStop());
		// return allTimeBest;
		return currentState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * search.strategy.local.LocalSearchBase#printState(domain.IProblemState,
	 * long)
	 */
	@Override
	protected boolean printState(IProblemState state)
	{
		if (super.printState(state)) {
			System.out.println("temperature: " + temperatureReduction.getCurrentTemperature());
			return true;
		}
		return false;
	}
}
