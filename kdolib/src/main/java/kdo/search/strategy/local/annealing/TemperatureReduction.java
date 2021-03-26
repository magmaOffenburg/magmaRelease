/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.annealing;

/**
 * Base class for temperature reduction strategies
 */
public abstract class TemperatureReduction implements ITemperatureReductionStrategy
{
	/** the initial temperature (>0) */
	protected double startTemperature;

	/** the intended temperature at the end(>=0) */
	protected double endTemperature;

	/** the current temperature (>=0) */
	protected double temperature;

	/**
	 * @param startTemperature the initial temperature (>0)
	 * @param endTemperature the intended temperature at the end(>=0)
	 */
	public TemperatureReduction(double startTemperature, double endTemperature)
	{
		super();
		this.startTemperature = startTemperature;
		this.endTemperature = endTemperature;
		this.temperature = startTemperature;
	}

	/*
	 * (non-Javadoc)
	 * @see search.strategy.local.ITemperatureReductionStrategy#getCurrentTemperature()
	 */
	@Override
	public double getCurrentTemperature()
	{
		return temperature;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuffer buffer = new StringBuffer(40);
		buffer.append("start: ").append(startTemperature);
		buffer.append(" current: ").append(temperature);
		buffer.append(" end: ").append(endTemperature);
		return buffer.toString();
	}
}