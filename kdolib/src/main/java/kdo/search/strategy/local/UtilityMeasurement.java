/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local;

import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.base.StrategyBase;

/**
 * Simply takes the passed state and calculates its utility
 * @author dorer
 */
public class UtilityMeasurement extends StrategyBase implements ILocalSearchStrategy
{
	private int repeats;

	public UtilityMeasurement()
	{
		this(1);
	}

	/**
	 * Allows to repeat a measurement. Only good for printing each single
	 * measurement. It does not average the results! If you want averaged results
	 * use AverageOutStrategy and default constructor.
	 */
	public UtilityMeasurement(int repeats)
	{
		super("UtilityMeasurement");
		this.repeats = repeats;
	}

	@Override
	public IProblemState search(IProblemState stateToEvaluate)
	{
		for (int i = 0; i < repeats; i++) {
			double utility = stateToEvaluate.calculateUtility();
			double[] runtimeProperties = stateToEvaluate.getRuntimeProperties();
			if (runtimeProperties == null) {
				runtimeProperties = new double[1];
				runtimeProperties[0] = utility;
			}
		}
		return stateToEvaluate;
	}
}
