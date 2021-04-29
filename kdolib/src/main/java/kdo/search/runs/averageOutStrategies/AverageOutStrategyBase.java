/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import kdo.domain.UtilityCalculatorParameters;

/**
 * Base class for all average utility calculation strategies
 * @author kdorer
 */
public abstract class AverageOutStrategyBase implements IAverageOutStrategy
{
	/** the number of runs to perform to average utility on */
	protected final int averageOutRuns;

	protected final boolean verbose;

	/** visitor that will be called to allow for domain specific implementation */
	protected IDomainVisitor visitor;

	protected List<Map<String, Double>> lastPropertiesHistory;

	/**
	 * @param averageOutRuns the number of runs to perform for the average
	 * @param verbose true if we print messages for each individual average out run
	 * @param visitor a visitor to inject domain specific implementations
	 */
	public AverageOutStrategyBase(int averageOutRuns, boolean verbose, IDomainVisitor visitor)
	{
		this.averageOutRuns = averageOutRuns;
		this.verbose = verbose;
		this.visitor = visitor;
	}

	protected void addValues(Map<String, Double> valuesToAdd, double utility, AverageOutState state)
	{
		if (verbose) {
			System.out.printf(Locale.US, "Run %d util: %.2f\n", state.count, utility);
		}

		state.addValues(valuesToAdd, utility);
		lastPropertiesHistory = state.propertiesHistory;
	}

	/**
	 * Called after all runs are finished
	 */
	protected void finishedCalculation(AverageOutState state)
	{
		System.out.printf(Locale.US, "Average utility: %5.3f averaged: %d, at %s, properties: ", state.averageUtility,
				state.count, new Date().toString());
		visitor.onFinishedCalculation(state.properties);
	}

	@Override
	public List<Map<String, Double>> getPropertiesHistory()
	{
		return lastPropertiesHistory;
	}

	/**
	 * Override this to create domain specific parameters
	 * @param id the id for this run
	 * @return a utility calculation parameter instance
	 */
	protected UtilityCalculatorParameters createUCParameters(int id)
	{
		return visitor.createUCParameters(id);
	}
}
