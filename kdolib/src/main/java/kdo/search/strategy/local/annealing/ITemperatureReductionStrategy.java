/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.annealing;

/**
 * Represents strategies to reduce the temperature for simulated annealing
 */
public interface ITemperatureReductionStrategy {
	/**
	 * Called to calculate the temperature function for a specific iteration in
	 * simulated annealing. Implementations should make sure that the returned
	 * temperature is always >= 0.
	 * @param iteration cycle (time) in which simulated annealing is currently
	 * @param maxIterations the number of planned iterations
	 * @return the temperature (>=0) we want to have in the given iteration
	 */
	double adjustTemperature(long iteration, long maxIterations);

	/**
	 * Returns the temperature that is currently valid
	 * @return the temperature that is currently valid
	 */
	double getCurrentTemperature();
}
