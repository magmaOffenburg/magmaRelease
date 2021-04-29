/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import kdo.domain.IUtilityCalculator;
import kdo.domain.UtilityCalculatorParameters;

/**
 * Does a serial run of all trials, expecting that the server is already
 * running.
 * @author kdorer
 */
public class SerialAverageOutStrategy extends AverageOutStrategyBase
{
	public SerialAverageOutStrategy(int averageOutRuns, boolean verbose, IDomainVisitor visitor)
	{
		super(averageOutRuns, verbose, visitor);
	}

	@Override
	public double getAverageUtility(AverageOutState state)
	{
		UtilityCalculatorParameters ucParameters = createUCParameters(0);
		IUtilityCalculator utilityCalculator = null;
		int repeatCount = 0;
		while (state.count < averageOutRuns) {
			utilityCalculator = state.getUtilityCalculator(ucParameters);
			double util = utilityCalculator.singleRunUtility(state.count, averageOutRuns, repeatCount);
			if (util == Double.NEGATIVE_INFINITY) {
				// we do not count invalid measurement runs
				repeatCount++;
				continue;
			}
			repeatCount = 0;

			addValues(utilityCalculator.getProperties(), util, state);
		}

		if (utilityCalculator != null) {
			utilityCalculator.printParams();
		}
		finishedCalculation(state);
		return state.averageUtility;
	}
}
