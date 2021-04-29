/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import java.util.HashMap;
import java.util.Map;

/**
 * Average out strategy that expects to be called from multiple threads at the
 * same time while running.
 * @author kdorer
 */
public class ThreadedAverageOutStrategy extends AverageOutStrategyBase
{
	private Map<String, SerialAverageOutStrategy> callers;

	public ThreadedAverageOutStrategy(int averageOutRuns, boolean verbose, IDomainVisitor visitor)
	{
		super(averageOutRuns, verbose, visitor);
		callers = new HashMap<>();
	}

	@Override
	public double getAverageUtility(AverageOutState state)
	{
		SerialAverageOutStrategy serial = new SerialAverageOutStrategy(averageOutRuns, verbose, visitor);
		callers.put(Thread.currentThread().getName(), serial);
		return serial.getAverageUtility(state);
	}
}
