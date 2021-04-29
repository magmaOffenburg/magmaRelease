/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import java.util.List;
import java.util.Map;

/**
 * Instances allow to average on different runs for non deterministic
 * environments.
 * @author kdorer
 */
public interface IAverageOutStrategy {
	/**
	 * Called to measure a single utility of an agent.
	 * @return the utility measured
	 */
	double getAverageUtility(AverageOutState state);

	List<Map<String, Double>> getPropertiesHistory();
}
