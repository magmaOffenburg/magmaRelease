/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.annealing;

/**
 * Reduces temperature linearly from start to end temperature
 */
public class LinearTemperatureReduction extends TemperatureReduction
{
	/**
	 * @param startTemperature the initial temperature (>0)
	 * @param endTemperature the intended temperature at the end(>=0)
	 */
	public LinearTemperatureReduction(double startTemperature, double endTemperature)
	{
		super(startTemperature, endTemperature);
	}

	/*
	 * (non-Javadoc)
	 * @see search.strategy.local.ITemperatureReductionStrategy#adjustTemperature(long,
	 *      long)
	 */
	public double adjustTemperature(long iteration, long maxIterations)
	{
		temperature = startTemperature - iteration * (startTemperature - endTemperature) / maxIterations;
		return temperature;
	}
}