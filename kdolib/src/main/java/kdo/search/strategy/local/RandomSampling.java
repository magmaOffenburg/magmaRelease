/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local;

import kdo.domain.IProblemState;

/**
 * Runs a specified number of random state sampling
 * @author dorer
 */
public class RandomSampling extends LocalSearchBase
{
	/** the number of random samples to take */
	private long howManySamples;

	/**
	 * Default constructor
	 */
	public RandomSampling(long maxRuntime, long howManySamples)
	{
		super("RandomSampling", null, maxRuntime);
		this.howManySamples = howManySamples;
	}

	@Override
	public IProblemState search(IProblemState startState)
	{
		onStartSearch();
		IProblemState bestState = startState;
		double bestUtility = startState.calculateUtility();

		for (int i = 0; i < howManySamples && !canStop(); i++) {
			IProblemState randomState = startState.getProblem().getRandomState();
			double utility = randomState.calculateUtility();
			if (utility > bestUtility) {
				bestUtility = utility;
				bestState = randomState;
			}
		}
		return bestState;
	}
}
