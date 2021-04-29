/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.fitness;

import java.util.List;
import kdo.domain.IOptimizationState;

/**
 * @author kdorer
 *
 */
public class SerialFitnessCalculator implements IFitnessCalculator
{
	@Override
	public double calculateFitness(List<List<IOptimizationState>> genderStates)
	{
		double sum = 0;
		int n = 0;
		for (List<IOptimizationState> net : genderStates) {
			for (IOptimizationState currentState : net) {
				sum += currentState.calculateUtility();
				n++;
			}
		}
		return sum / n;
	}
}
