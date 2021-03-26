/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.approximation.model;

import kdo.domain.IChromosomStrategy;
import kdo.domain.IIndividuum;
import kdo.domain.IOperator;
import kdo.domain.model.GeneticProblem;
import kdo.domain.model.ParameterChangeOperator;
import kdo.domain.model.VisualizationIndividuum;
import kdo.domain.model.chromosom.UniformChromosomStrategy;
import kdo.util.IRandomSource;

/**
 * Demo for a genetic problem. Approximates a function. The chromosom consists
 * of genesize genes. Each gene represents one value of the function to
 * approximate. Since we are currently restricted to int genes, we use a stretch
 * factor to also have deciaml digits.
 * @author dorer
 */
public class FunctionProblem extends GeneticProblem
{
	private enum FunctionEnum { sin, cos, log }

	/** min value the genes can have */
	private static float minValue = -320;

	/** max value the genes can have */
	private static float maxValue = 320;

	/** number of genes in the chromosom */
	private static final int genesize = 30;

	/** the factor by which we multiply values before casting them to int */
	private double stretch;

	/** x value the first gene corresponds to */
	private double firstX;

	/** x offset from one to the next gene */
	private double incrementX;

	/** which function to use */
	private FunctionEnum function;

	/**
	 * @param rand the source for random numbers to use
	 */
	public FunctionProblem(IRandomSource rand, IChromosomStrategy strategy)
	{
		super(null, rand, strategy);
		function = FunctionEnum.sin;
		setValueArea();
	}

	/**
	 * Creates an instance of a function problem to approximate functions
	 * @param rand the source for random numbers to use
	 * @return the function problem instance
	 */
	public static FunctionProblem getInstance(IRandomSource rand)
	{
		IChromosomStrategy strategy = new UniformChromosomStrategy(rand, minValue, maxValue, genesize);
		FunctionProblem result = new FunctionProblem(rand, strategy);
		result.setInitialState(new VisualizationIndividuum(result.getRandomIndividuum()));
		return result;
	}

	/**
	 * Creates an individuum from the passe chromosom
	 */
	public IIndividuum createIndividuum(float[] chromosom)
	{
		return new VisualizationIndividuum(new FunctionIndividuum(this, chromosom));
	}

	@Override
	public IOperator getRandomOperator(int neighborhoodRadius)
	{
		int chromosomSize = chromosomStrategy.getChromosomSize();
		int parameterIndex = rand.nextInt(chromosomSize);
		float parameterValue = chromosomStrategy.getRandomAlele(parameterIndex, null);
		return new ParameterChangeOperator("Change", 0.0f, parameterIndex, parameterValue);
	}

	private void setValueArea()
	{
		// this method has to be adjusted to the function that is approximated
		// TODO extract into a strategy with functionToApproximate
		stretch = 100.0;
		firstX = 0.0;
		incrementX = 0.2;
	}

	public double getValue(int x)
	{
		return functionToApproximate(getXValue(x)) * getStretch();
	}

	/**
	 * The function that is approximated
	 * @param x value for which to calculate the function value
	 * @return the function value
	 */
	double functionToApproximate(double x)
	{
		switch (function) {
		case cos:
			return Math.cos(x);
		case log:
			if (x < 0.1) {
				return -2.0;
			}
			return Math.log(x);
		default:
			return Math.sin(x);
		}
	}

	/**
	 * @param offset the index of the chromosom
	 * @return the x coordinate that fits to a chromosom index
	 */
	double getXValue(int offset)
	{
		return firstX + incrementX * offset;
	}

	/**
	 * @return the stretch
	 */
	double getStretch()
	{
		return stretch;
	}

	public void setFunction(String functionName)
	{
		if (functionName.equals("cos")) {
			function = FunctionEnum.cos;
		} else if (functionName.equals("log")) {
			function = FunctionEnum.log;
		} else {
			function = FunctionEnum.sin;
		}
	}
}
