/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.approximation.model;

import java.util.Iterator;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.model.Individuum;
import kdo.domain.model.ParameterChangeIterator;

/**
 * Demo application for genetic framework. Approximates the function called from
 * calculate fitness.
 * @author dorer
 */
public class FunctionIndividuum extends Individuum
{
	/**
	 * Constructor to create a new individuum using the chromosom passed
	 * @param chromosom the gene information to use for the individuum
	 */
	public FunctionIndividuum(IProblem problem, float[] chromosom)
	{
		super(problem, chromosom);
	}

	/**
	 * Calculates the utility as mean error of deviation from real function. The
	 * less the better.
	 * @param chromosom the chromosom for which to calculate
	 * @return the utility of the passed chromosom
	 */
	@Override
	public float calculateUtility()
	{
		double meanAbsError = 0.0;
		for (int i = 0; i < state.length; i++) {
			double real = ((FunctionProblem) problem).getValue(i);
			double isValue = state[i];
			meanAbsError += Math.abs(real - isValue);
		}
		// minus since big values mean bad utility
		utility = (float) -meanAbsError;
		return utility;
	}

	public double getValue(int offset)
	{
		return ((FunctionProblem) problem).getValue(offset);
	}

	public void setFunction(String function)
	{
		((FunctionProblem) problem).setFunction(function);
	}

	@Override
	public Iterator<IOperator> operatorIterator()
	{
		return new ParameterChangeIterator(this, 0.1f);
	}
}
