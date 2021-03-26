/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.impl;

import kdo.search.impl.SearchParameters;
import kdo.search.strategy.local.genetic.fitness.IFitnessCalculator;
import kdo.search.strategy.local.genetic.fitness.ParallelFitnessCalculator;
import kdo.search.strategy.local.genetic.fitness.SerialFitnessCalculator;
import kdo.util.IRandomSource;

public class OptimizationSearchParameter extends SearchParameters
{
	private int iterations;

	private int populationSize;

	private int groups;

	/**
	 * true if the utility calculation is expensive and should be avoided if
	 * possible
	 */
	private boolean expensiveUtilityCalculation;

	private int utilityCalculationThreadpoolSize;

	public OptimizationSearchParameter(IRandomSource random)
	{
		super(random, "40");
		groups = 2;
		iterations = 300;
		populationSize = 240;
		expensiveUtilityCalculation = false;
		utilityCalculationThreadpoolSize = 1;
	}

	public int getIterations()
	{
		return iterations;
	}

	public void setIterations(int iteration)
	{
		this.iterations = iteration;
	}

	public int getPopulationSize()
	{
		return populationSize;
	}

	public void setPopulationSize(int population)
	{
		if (population < 4 || population > 10000) {
			throw new IllegalArgumentException("Invalid population size [4..10000]: " + population);
		}
		this.populationSize = population;
	}

	/**
	 * @return the expensiveUtilityCalculation
	 */
	public boolean isExpensiveUtilityCalculation()
	{
		return expensiveUtilityCalculation;
	}

	/**
	 * @param expensiveUtilityCalculation the expensiveUtilityCalculation to set
	 */
	public void setExpensiveUtilityCalculation(boolean expensiveUtilityCalculation)
	{
		this.expensiveUtilityCalculation = expensiveUtilityCalculation;
	}

	public int getFitnessCalculationThreadpoolSize()
	{
		return utilityCalculationThreadpoolSize;
	}

	public void setFitnessCalculationThreadpoolSize(int size)
	{
		utilityCalculationThreadpoolSize = size;
	}

	/**
	 * @return the groups
	 */
	public int getGroups()
	{
		return groups;
	}

	/**
	 * @param groups the groups to set
	 */
	public void setGroups(int groups)
	{
		if (groups < 1 || groups > this.getPopulationSize()) {
			throw new IllegalArgumentException("Invalid number of groups [1..population]: " + groups);
		}
		this.groups = groups;
	}

	public IFitnessCalculator createFitnessCalculationStrategy()
	{
		if (utilityCalculationThreadpoolSize > 1) {
			return new ParallelFitnessCalculator(utilityCalculationThreadpoolSize);
		}
		return new SerialFitnessCalculator();
	}

	@Override
	public String toString()
	{
		return super.toString() + "OptimizationSearchParameter [iterations=" + iterations +
				", populationSize=" + populationSize + ", groups=" + groups +
				", expensiveUtilityCalculation=" + expensiveUtilityCalculation + "]";
	}
}