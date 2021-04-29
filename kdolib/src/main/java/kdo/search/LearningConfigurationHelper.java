/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kdo.search.impl.SearchParameters;
import kdo.search.runs.averageOutStrategies.IAverageOutStrategy;
import kdo.search.runs.averageOutStrategies.IDomainVisitor;
import kdo.search.runs.averageOutStrategies.ParallelAverageOutStrategy;
import kdo.search.runs.averageOutStrategies.SerialAverageOutStrategy;
import kdo.search.runs.averageOutStrategies.ThreadedAverageOutStrategy;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.local.BasicVariableNeighborhoodSearch;
import kdo.search.strategy.local.FullSearch;
import kdo.search.strategy.local.HillClimbing;
import kdo.search.strategy.local.HillClimbingRandom;
import kdo.search.strategy.local.HillClimbingRandomRestart;
import kdo.search.strategy.local.HillClimbingStateCache;
import kdo.search.strategy.local.NoLocalSearchStrategy;
import kdo.search.strategy.local.RandomSampling;
import kdo.search.strategy.local.RandomWalk;
import kdo.search.strategy.local.TabuSearch;
import kdo.search.strategy.local.UtilityMeasurement;
import kdo.search.strategy.local.annealing.HyperbolicTemperatureReduction;
import kdo.search.strategy.local.annealing.ITemperatureReductionStrategy;
import kdo.search.strategy.local.annealing.LinearTemperatureReduction;
import kdo.search.strategy.local.annealing.SigmoidalTemperatureReduction;
import kdo.search.strategy.local.annealing.SimulatedAnnealing;
import kdo.search.strategy.local.cmaes.impl.CMAESOptimizerAdapter;
import kdo.search.strategy.local.genetic.impl.GeneticSearch;
import kdo.search.strategy.local.genetic.impl.GeneticSearchParameter;
import kdo.search.strategy.local.genetic.impl.PSOSearch;
import kdo.search.strategy.local.threaded.HillClimbingMultiThreaded;
import kdo.search.strategy.local.threaded.TabuSearchMultiThreaded;

/**
 * @author kdorer
 */
public class LearningConfigurationHelper
{
	public enum OptimizationStrategy {
		NOLOCALSEARCH,
		MEASUREMENT,
		FULLSEARCH,
		GENETIC,
		CMAES,
		PSO,
		VNS,
		TABUSEARCH,
		TABUSEARCHTHREADED,
		SIMULATEDANNEALING,
		RANDOMWALK,
		HCRANDOMOPERATOR,
		HCRANDOMRESTART,
		HCMULTITHREADED,
		HILLCLIMBING,
		HILLCLIMBINGSTATECACHE,
		RANDOMSAMPLING;

		public static OptimizationStrategy fromString(String text)
		{
			for (OptimizationStrategy b : OptimizationStrategy.values()) {
				if (b.name().equalsIgnoreCase(text)) {
					return b;
				}
			}
			return null;
		}
	}

	// avg out strategies
	public static final String SERIAL = "serial";

	private static final String PARALLEL = "parallel";

	private static final String THREADED = "threaded";

	/**
	 * A read-only list of all available avg strategies.
	 */
	public static final List<String> AVG_STRATEGIES;

	static
	{
		List<String> avgStrategies = new ArrayList<>();
		avgStrategies.addAll(Arrays.asList(SERIAL, PARALLEL, THREADED));
		AVG_STRATEGIES = Collections.unmodifiableList(avgStrategies);
	}

	/**
	 *
	 * @param avgStrategy strategy name
	 * @param avgOutRuns number of runs
	 * @param threads the number of threads for parallel strategy
	 * @return an average out strategy for
	 */
	public static IAverageOutStrategy getAverageOutStrategy(
			String avgStrategy, int avgOutRuns, boolean verbose, IDomainVisitor visitor)
	{
		switch (avgStrategy) {
		case SERIAL:
			return new SerialAverageOutStrategy(avgOutRuns, verbose, visitor);
		case PARALLEL:
			return new ParallelAverageOutStrategy(avgOutRuns, verbose, visitor);
		case THREADED:
			return new ThreadedAverageOutStrategy(avgOutRuns, verbose, visitor);
		}
		return null;
	}

	/**
	 * Returns a local search method to be used. Genetic search is treated
	 * separately, although in principle it is also a LocalSearch and properly
	 * inheriting.
	 * @param params local search parameters
	 * @param threads number of parallel threads
	 * @return the local search strategy to be used
	 */
	public static ILocalSearchStrategy getLocalSearchStrategy(SearchParameters params, int threads)
	{
		String strategyString = params.getStrategy();
		long maxRuntime = params.getMaxRuntime();
		long maxIterations = (long) params.getStrategyParameter("MaxIterations", 10000000);
		OptimizationStrategy strategy = OptimizationStrategy.fromString(strategyString);

		switch (strategy) {
		case NOLOCALSEARCH:
			return new NoLocalSearchStrategy();
		case HILLCLIMBING:
			return new HillClimbing(maxRuntime);
		case HILLCLIMBINGSTATECACHE:
			int cacheSize = (int) params.getStrategyParameter("CacheSize", 5);
			return new HillClimbingStateCache(maxRuntime, maxIterations, cacheSize);
		case HCRANDOMRESTART:
			int restarts = (int) params.getStrategyParameter("Restarts", 100);
			return new HillClimbingRandomRestart(restarts, maxRuntime);
		case HCRANDOMOPERATOR:
			return new HillClimbingRandom(maxIterations, maxRuntime);
		case HCMULTITHREADED:
			return new HillClimbingMultiThreaded(maxRuntime, maxIterations, threads);
		case RANDOMWALK:
			int randomSteps = (int) params.getStrategyParameter("RandomSteps", 30);
			return new RandomWalk(randomSteps, maxIterations, maxRuntime);
		case SIMULATEDANNEALING:
			int temperatureStrategy = (int) params.getStrategyParameter("TemperatureStrategy", 0);
			double startTemperature = params.getStrategyParameter("StartTemperature", 0.5);
			double endTemperature = params.getStrategyParameter("EndTemperature", 0.0);
			ITemperatureReductionStrategy reductionStrategy = null;
			switch (temperatureStrategy) {
			case 1:
				reductionStrategy = new SigmoidalTemperatureReduction(startTemperature, endTemperature);
				break;
			case 2:
				reductionStrategy = new HyperbolicTemperatureReduction(startTemperature, endTemperature);
				break;
			default:
				reductionStrategy = new LinearTemperatureReduction(startTemperature, endTemperature);
			}
			return new SimulatedAnnealing(reductionStrategy, maxIterations, maxRuntime);
		case TABUSEARCH:
			int tabulistSize = (int) params.getStrategyParameter("TabulistSize", 100);
			return new TabuSearch(tabulistSize, maxIterations, maxRuntime);
		case TABUSEARCHTHREADED:
			tabulistSize = (int) params.getStrategyParameter("TabulistSize", 100);
			return new TabuSearchMultiThreaded(tabulistSize, maxIterations, maxRuntime, threads);
		case VNS:
			// do basic variable neighborhood search
			int neighborhoodRadius = (int) params.getStrategyParameter("NeighborhoodRadius", 175);
			return new BasicVariableNeighborhoodSearch(new HillClimbing(-1), neighborhoodRadius, maxRuntime);
		case MEASUREMENT:
			return new UtilityMeasurement();
		case FULLSEARCH:
			return new FullSearch();
		case CMAES:
			int populationSize = (int) params.getStrategyParameter("PopulationSize", 50);
			int generations = (int) params.getStrategyParameter("Generations", 70);
			double stopFitness = params.getStrategyParameter("StopFitness", 100);
			double sigmaSpread = params.getStrategyParameter("Sigma", 0.1);
			return CMAESOptimizerAdapter.getInstance(populationSize, generations, stopFitness, sigmaSpread);
		case GENETIC:
			GeneticSearchParameter geneticParams = new GeneticSearchParameter(params.getRandomSource());
			geneticParams.setPopulationSize((int) params.getStrategyParameter("PopulationSize", 200));
			geneticParams.setGenerations((int) params.getStrategyParameter("Generations", 70));
			geneticParams.setMaxRuntime(6000000000000L);
			geneticParams.setSelectionStrategy(GeneticSearchParameter.MONTE_CARLO_SELECTION);
			geneticParams.setReproductionStrategy(GeneticSearchParameter.MULTI_CROSSOVER_RECOMBINATION);
			geneticParams.setMutationStrategy(GeneticSearchParameter.RANDOM_MUTATION);
			return new GeneticSearch(geneticParams);
		case PSO:
			GeneticSearchParameter parameter = new GeneticSearchParameter(params.getRandomSource());
			parameter.setMaxRuntime(maxRuntime);
			parameter.setIterations((int) maxIterations);
			parameter.setPopulationSize((int) params.getStrategyParameter("PopulationSize", 200));
			return new PSOSearch(parameter);

		case RANDOMSAMPLING:
			return new RandomSampling(60000, (int) params.getStrategyParameter("Samples", 100000));
		}
		return null;
	}
}
