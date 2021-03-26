/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import java.util.Map;
import kdo.domain.UtilityCalculatorParameters;

public interface IDomainVisitor {
	/**
	 * Called before utility calculation to setup domain specific parameters
	 * @param id the id of this average out run
	 * @return the utility calculator parameters required from the domain
	 */
	UtilityCalculatorParameters createUCParameters(int id);

	/**
	 * Called once oversampling is finished
	 */
	void onFinishedCalculation(Map<String, Double> properties);
}
