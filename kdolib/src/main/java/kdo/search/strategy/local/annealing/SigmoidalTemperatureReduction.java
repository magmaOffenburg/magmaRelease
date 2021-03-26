/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.annealing;

/**
 * Reduces temperature in an s shaped curve from start to end temperature
 */
public class SigmoidalTemperatureReduction extends TemperatureReduction
{
	/**
	 * @param startTemperature the initial temperature (>0)
	 * @param endTemperature the intended temperature at the end(>=0)
	 */
	public SigmoidalTemperatureReduction(double startTemperature, double endTemperature)
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
		temperature = (startTemperature - endTemperature) /
							  (1.0 + Math.exp(0.3 * (iteration - maxIterations / 2) / (maxIterations / 50))) +
					  endTemperature;

		return temperature;
	}
}