/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.local.annealing;

import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.local.HillClimbing;
import kdo.search.strategy.local.LocalSearchBase;

/**
 * Implementation of simulated annealing local search that is followed by a hill
 * climbing local improvement
 */
public class AnnealingAndClimbing extends LocalSearchBase
{
	/** the simulated annealing instance that is used for first improvement */
	private final ILocalSearchStrategy simulatedAnnealing;

	/** the hill climbing instance that is used for improvement */
	private final ILocalSearchStrategy hillClimbing;

	/**
	 * Creates a new simulated annealing instance with the passed starting
	 * temperature and number of temperature decreasing iterations. Also creates
	 * a hill climbing instance that is used for improvement after annealing.
	 * @param temperatureReduction strategy used for cooling down temperature
	 * @param maxIterations the maximal number of iterations to run through
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public AnnealingAndClimbing(ITemperatureReductionStrategy temperatureReduction, int maxIterations, long maxRuntime)
	{
		super("Annealing and HillClimbing. ", null, maxRuntime);
		simulatedAnnealing = new SimulatedAnnealing(temperatureReduction, maxIterations, maxRuntime);
		hillClimbing = new HillClimbing(maxRuntime);
	}

	/*
	 * (non-Javadoc)
	 * @see search.strategy.IStrategy#getName()
	 */
	@Override
	public String getName()
	{
		return "Annealing and HillClimbing. " + simulatedAnnealing.getName() + " " + hillClimbing.getName();
	}

	/**
	 * Special search algorithm for simulated annealing that does not expand a
	 * node completely, but only expands the randomly selected successor.
	 * @param problem the domain problem for which the search is done.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	public IProblemState search(IProblemState initialState)
	{
		// do simulated annealing
		IProblemState currentState = simulatedAnnealing.search(initialState);

		// try to improve result with hill climbing
		currentState = hillClimbing.search(currentState);

		return currentState;
	}
}
