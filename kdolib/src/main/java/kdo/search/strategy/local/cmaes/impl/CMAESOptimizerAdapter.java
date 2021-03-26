/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.search.strategy.local.cmaes.impl;

import kdo.domain.IOptimizationProblem;
import kdo.domain.IOptimizationState;
import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.base.RandomStrategy;
import kdo.util.IRandomSource;
import kdo.util.learningParameter.ILearningParameterList;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * A wrapper to the CMAESOptimizer in commons math. To be moved to kdoLib
 * @author kdorer
 */
public class CMAESOptimizerAdapter extends RandomStrategy implements ILocalSearchStrategy
{
	/** the adapted optimizer object */
	private CMAESOptimizer optimizer;

	/** the population size for optimization */
	private int populationSize;

	/** the maximal number of utility calculations */
	private int maxEvaluations;

	/** factor for interval of lower and upper bound used to calculate sigma */
	private double sigmaSpread;

	/** if maximization or minimization is required */
	private GoalType goalType;

	public static CMAESOptimizerAdapter getInstance(
			int populationSize, int iterations, double stopFitness, double sigmaSpread)
	{
		IRandomSource rand = new RandomSourceAdapter(new Well19937c());
		return new CMAESOptimizerAdapter(
				rand, populationSize, iterations, populationSize * iterations, stopFitness, sigmaSpread);
	}

	public static CMAESOptimizerAdapter getInstance(
			int populationSize, int iterations, double stopFitness, double sigmaSpread, boolean maximize)
	{
		GoalType goal = GoalType.MAXIMIZE;
		if (!maximize) {
			goal = GoalType.MINIMIZE;
		}
		IRandomSource rand = new RandomSourceAdapter(new Well19937c());
		return new CMAESOptimizerAdapter(
				rand, populationSize, iterations, populationSize * iterations, stopFitness, sigmaSpread, goal);
	}

	public CMAESOptimizerAdapter(IRandomSource rand, int populationSize, int maxIterations, int maxEvaluations,
			double stopFitness, double sigmaSpread)
	{
		this(rand, populationSize, maxIterations, maxEvaluations, stopFitness, sigmaSpread, GoalType.MAXIMIZE);
	}

	public CMAESOptimizerAdapter(IRandomSource rand, int populationSize, int maxIterations, int maxEvaluations,
			double stopFitness, double sigmaSpread, GoalType goalType)
	{
		super("CMA-ES", rand);
		this.populationSize = populationSize;
		this.maxEvaluations = maxEvaluations;
		this.sigmaSpread = sigmaSpread;
		this.goalType = goalType;
		optimizer = new CMAESOptimizer(maxIterations, stopFitness, true, 1, 10, (RandomGenerator) rand, true, null);
		// convergence checking (last argument) is difficult in noisy domain of
		// RoboCup
		// new SimpleValueChecker(-1, 0.001));
	}

	@Override
	public IProblemState search(IProblemState initialState)
	{
		float[] initialChromosom = ((IOptimizationState) initialState).getState();
		IOptimizationProblem problem = (IOptimizationProblem) initialState.getProblem();
		MultivariateFunction fitness = new UtilityCalculator(problem);
		ILearningParameterList params = problem.createParameters(initialChromosom);
		float[] minValues = params.getMinValues();
		float[] maxValues = params.getMaxValues();
		double[] lowerBounds = floatToDoubleArray(minValues);
		double[] upperBounds = floatToDoubleArray(maxValues);

		// calculate the sigma
		double[] sigma = new double[lowerBounds.length];
		for (int i = 0; i < sigma.length; i++) {
			sigma[i] = (upperBounds[i] - lowerBounds[i]) * sigmaSpread;
		}

		double[] initialPoint = floatToDoubleArray(initialChromosom);

		PointValuePair result =
				optimizer.optimize(new InitialGuess(initialPoint), goalType, new ObjectiveFunction(fitness),
						new CMAESOptimizer.Sigma(sigma), new CMAESOptimizer.PopulationSize(populationSize),
						new SimpleBounds(lowerBounds, upperBounds), new MaxEval(maxEvaluations));

		// System.out.println("Done CMAES: " + result.getSecond() + " values: "
		// + Arrays.toString(result.getFirst()));

		float[] dimension = doubleToFloatArray(result.getFirst());
		IOptimizationState individuum = problem.createState(dimension);
		individuum.setUtility(result.getSecond().floatValue());
		return individuum;
	}

	private static double[] floatToDoubleArray(float[] source)
	{
		double[] result = new double[source.length];
		for (int i = 0; i < source.length; i++) {
			result[i] = source[i];
		}
		return result;
	}

	private static float[] doubleToFloatArray(double[] source)
	{
		float[] result = new float[source.length];
		for (int i = 0; i < source.length; i++) {
			result[i] = (float) source[i];
		}
		return result;
	}

	@Override
	public String toString()
	{
		return "CMAESOptimizerAdapter [populationSize=" + populationSize + ", maxEvaluations=" + maxEvaluations +
				", sigmaSpread=" + sigmaSpread + ", goalType=" + goalType + "]";
	}

	class UtilityCalculator implements MultivariateFunction
	{
		private IOptimizationProblem problem;

		public UtilityCalculator(IOptimizationProblem problem)
		{
			this.problem = problem;
		}

		@Override
		public double value(double[] point)
		{
			float[] dimension = doubleToFloatArray(point);
			IOptimizationState state = problem.createState(dimension);
			return state.calculateFitness();
		}
	}
}
