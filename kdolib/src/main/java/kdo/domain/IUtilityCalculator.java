/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain;

import java.util.Map;

/**
 * Instances allow to calculate a utility in a Domain
 * @author kdorer
 */
public interface IUtilityCalculator {
	/**
	 * Called to measure a single utility of an agent.
	 * @param avgOutRunID a counter indicating which over sampling id this run has
	 * @param avgOutRuns the overall number of over sampling runs
	 * @param repeatCount a counter indicating how many times this measurement is repeated now
	 * @return the utility measured
	 */
	double singleRunUtility(int avgOutRunID, int avgOutRuns, int repeatCount);

	/**
	 * @return a map of additional properties of the run
	 */
	Map<String, Double> getProperties();

	/**
	 * Prints the parameters that were used to calculate the utility
	 */
	void printParams();
}
