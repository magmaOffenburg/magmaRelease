/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kdo.domain.IIndividuum;
import kdo.domain.IUtilityCalculator;
import kdo.domain.UtilityCalculatorParameters;
import kdo.util.misc.ValueUtil;

/**
 * @author kdorer
 *
 */
public class AverageOutState
{
	/** the number of tries that added to this average */
	public int count;

	/** Map to be used for additional information */
	public Map<String, Double> properties;

	/** Current average measured utility */
	public double averageUtility;

	protected List<Map<String, Double>> propertiesHistory;

	/** the individual we work on */
	protected IIndividuum individuum;

	/**
	 *
	 */
	public AverageOutState(IIndividuum individuum)
	{
		this.individuum = individuum;
		count = 0;
		properties = null;
		averageUtility = 0;
	}

	protected IUtilityCalculator getUtilityCalculator(UtilityCalculatorParameters ucParameter)
	{
		return individuum.getUtilityCalculator(ucParameter);
	}

	protected void addValues(Map<String, Double> valuesToAdd, double utility)
	{
		if (count == 0) {
			averageUtility = utility;
			properties = new LinkedHashMap<>(valuesToAdd);
			propertiesHistory = new ArrayList<>();

		} else {
			calculateNewAverages(valuesToAdd, utility);
		}
		propertiesHistory.add(valuesToAdd);
		count++;
	}

	public void calculateNewAverages(Map<String, Double> valuesToAdd, double utility)
	{
		averageUtility = ValueUtil.addToAverage(averageUtility, utility, count + 1);

		for (String key : valuesToAdd.keySet()) {
			double newValue = valuesToAdd.get(key);
			Double oldAverage = properties.get(key);
			if (oldAverage == null) {
				properties.put(key, newValue);
				continue;
			}

			double newAverage = ValueUtil.addToAverage(properties.get(key), newValue, count + 1);
			properties.put(key, newAverage);
		}
	}
}
