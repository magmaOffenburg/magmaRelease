/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.tools.genetic.model;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.local.genetic.IMutation;
import kdo.search.strategy.local.genetic.IPopulation;
import kdo.search.strategy.local.genetic.IReproduction;
import kdo.search.strategy.local.genetic.ISelection;
import kdo.search.strategy.local.genetic.impl.GeneticSearch;
import kdo.search.strategy.local.genetic.impl.GeneticSearchParameter;
import kdo.util.observer.IObserver;
import kdo.util.observer.IPublishSubscribe;
import kdo.util.observer.Subject;

/**
 * application containing all the elements to do genetic optimization
 * @author klaus
 */
public class GeneticOptimization implements IObserver<IPopulation>
{
	public static final String WATCH_MAKER_PROBLEM = "WatchMaker";

	public static String FUNCTION_PROBLEM = "FunctionProblem";

	public static String LOGISTICS_PROBLEM = "LogisticsProblem";

	/** the search strategy that is used */
	protected GeneticSearch localSearchStrategy;

	/** true if the optimization is currently running */
	private boolean running;

	/** true if the optimization can not be continued */
	private boolean atEnd;

	private IPublishSubscribe<Boolean> observers;

	private List<IIndividuum> individuumsSorted;

	private int currentIndividuumIndex;

	/**
	 * Default constructor to create the model
	 */
	public GeneticOptimization(GeneticSearchParameter params)
	{
		running = false;
		observers = new Subject<>();
		currentIndividuumIndex = 0;
		localSearchStrategy = new GeneticSearch(params);

		// we want to be informed about improvements
		IPublishSubscribe<IPopulation> observerManager = new Subject<>();
		observerManager.attach(this);
		localSearchStrategy.setObserverManager(observerManager);
	}

	public void attach(IObserver<Boolean> observer)
	{
		observers.attach(observer);
		observers.onStateChange(false);
	}

	public void detach(IObserver<Boolean> observer)
	{
		observers.detach(observer);
	}

	/**
	 * Starts genetic optimization in its own thread.
	 */
	public float start()
	{
		if (running || atEnd) {
			return getBestFitness();
		}
		running = true;
		float result = runOptimization();
		running = false;
		observers.onStateChange(running);
		return result;
	}

	/**
	 * Stops genetic optimization, by requesting an interrup to the local
	 * optimization running. May not immediately stop.
	 */
	public void stop()
	{
		if (running) {
			localSearchStrategy.stop();
		}
	}

	public void rewind()
	{
		if (!running) {
			localSearchStrategy.rewind();
		}
	}

	public void step()
	{
		if (running || atEnd) {
			return;
		}
		localSearchStrategy.step();
	}

	public void pause()
	{
		if (running) {
			localSearchStrategy.pause();
		}
	}

	/**
	 * Performs a local genetic search
	 */
	private float runOptimization()
	{
		IIndividuum resultState;

		// do the local search
		// long startTime = System.currentTimeMillis();

		// now run optimization
		// System.out.println("Starting " + localSearchStrategy.getName());
		resultState = (IIndividuum) localSearchStrategy.search(null);
		float utility = resultState.getUtility();

		// long runtime = System.currentTimeMillis() - startTime;
		// StringBuffer result = new StringBuffer(500);
		// result.append("utility, time, strategy\n");
		// result.append(-utility).append(", ");
		// result.append(runtime).append(", ");
		// result.append(localSearchStrategy.getName()).append(", ");
		// System.out.println(result);
		// System.out.println(resultState);
		writeSolution(".txt", resultState, true);

		return utility;
	}

	/** Called if the model is updated */
	@SuppressWarnings("unchecked")
	@Override
	public void update(IPopulation population)
	{
		individuumsSorted = (List<IIndividuum>) (List<?>) population.getSortedStates();
		currentIndividuumIndex = 0;
		atEnd = population.getAverageDiversity() <= 1.0;
		observers.onStateChange(running);

		IIndividuum bestIndividuum = (IIndividuum) population.getBestState();
		if (bestIndividuum != null) {
			writeSolution(population.getGenerations() + "fit.txt", bestIndividuum, false);
		}
	}

	/**
	 * @return the params
	 */
	public GeneticSearchParameter getParams()
	{
		return localSearchStrategy.getParameters();
	}

	/**
	 * @param population
	 * @param bestIndividuum
	 */
	private void writeSolution(String suffix, IIndividuum bestIndividuum, boolean done)
	{
		if (done) {
			bestIndividuum.storeResult();
		}
	}

	/**
	 * @param strategyString the strategy for individuum selection
	 * @param params parameters to use, null if existing parameters should be
	 *        used
	 */
	public void setSelectionStrategy(String strategyString)
	{
		GeneticSearchParameter params = localSearchStrategy.getParameters();
		params.setSelectionStrategy(strategyString);
		ISelection strategy = params.createSelectionStrategy();
		if (localSearchStrategy != null) {
			localSearchStrategy.setSelectionStrategy(strategy);
		}
	}

	public void setMutationStrategy(String strategyString)
	{
		GeneticSearchParameter params = localSearchStrategy.getParameters();
		params.setMutationStrategy(strategyString);
		IMutation strategy = params.createMutationStrategy();
		if (localSearchStrategy != null) {
			localSearchStrategy.setMutationStrategy(strategy);
		}
	}

	public void setRecombinationStrategy(String strategyString)
	{
		GeneticSearchParameter params = localSearchStrategy.getParameters();
		params.setReproductionStrategy(strategyString);
		IReproduction strategy = params.createRecombinationStrategy();
		if (localSearchStrategy != null) {
			localSearchStrategy.setReproductionStrategy(strategy);
		}
	}

	public void setGenerations(int value)
	{
		GeneticSearchParameter params = localSearchStrategy.getParameters();
		params.setGenerations(value);
	}

	public void setMaxRuntime(int value)
	{
		GeneticSearchParameter params = localSearchStrategy.getParameters();
		params.setMaxRuntime(value);
	}

	public void setOldToNew(float value)
	{
		localSearchStrategy.setOldToNew(value);
	}

	public void setIndividuumMutationProbability(float value)
	{
		localSearchStrategy.setIndividuumMutationProbability(value);
	}

	public void setGeneMutationProbability(float value)
	{
		localSearchStrategy.setGeneMutationProbability(value);
	}

	public void setEliteSelectionRatio(float value)
	{
		localSearchStrategy.setEliteSelectionRatio(value);
	}

	/**
	 * @return the running
	 */
	public boolean isRunning()
	{
		return running;
	}

	/**
	 * @return true if the optimization can not be continued
	 */
	public boolean isAtEnd()
	{
		return atEnd;
	}

	public int getCurrentGeneration()
	{
		return localSearchStrategy.getCurrentGeneration();
	}

	public IPopulation getPopulation()
	{
		return localSearchStrategy.getPopulation();
	}

	public IIndividuum getCurrentIndividuum()
	{
		if (individuumsSorted == null) {
			return null;
		}
		// the individuums are sorted ascending, but currentIndividuumIndex is not
		return individuumsSorted.get(individuumsSorted.size() - currentIndividuumIndex - 1);
	}

	public int getCurrentIndividuumIndex()
	{
		return currentIndividuumIndex;
	}

	public int getNumberOfIndividuums()
	{
		if (individuumsSorted == null) {
			return 0;
		}
		return individuumsSorted.size();
	}

	public void setCurrentIndividuumIndex(int index)
	{
		currentIndividuumIndex = index;
		observers.onStateChange(running);
	}

	public boolean hasStarted()
	{
		return individuumsSorted != null;
	}

	public int getParents()
	{
		return getPopulation().getParentsPerIndividuum();
	}

	public int getGenders()
	{
		return getPopulation().getGenders();
	}

	public float getBestFitness()
	{
		IIndividuum bestIndividuum = getBestIndividuum();
		if (bestIndividuum == null) {
			return 0.0f;
		}
		return bestIndividuum.getFitness();
	}

	/**
	 * @return the best individuum, null if not existing
	 */
	public IIndividuum getBestIndividuum()
	{
		if (getPopulation() == null) {
			return null;
		}
		return (IIndividuum) getPopulation().getBestState();
	}

	public float getAverageDiversity()
	{
		if (getPopulation() == null) {
			return 0.0f;
		}
		return getPopulation().getAverageDiversity();
	}

	public float getRuntime()
	{
		if (getPopulation() == null) {
			return 0.0f;
		}
		return getPopulation().getRuntime();
	}

	public String[] getSelectionStrategyNames()
	{
		String[] names = {GeneticSearchParameter.ELITE_SELECTION, GeneticSearchParameter.RANK_SELECTION,
				GeneticSearchParameter.MONTE_CARLO_SELECTION,
				GeneticSearchParameter.MIN_DIVERSITY_MONTE_CARLO_SELECTION, GeneticSearchParameter.AI_LAB_SELECTION};
		return names;
	}

	public String[] getRecombinationStrategyNames()
	{
		String[] names = {
				GeneticSearchParameter.SINGLE_CROSSOVER_RECOMBINATION,
				GeneticSearchParameter.MULTI_CROSSOVER_RECOMBINATION,
				GeneticSearchParameter.AI_LAB_RECOMBINATION,
		};
		return names;
	}

	public String[] getMutationStrategyNames()
	{
		String[] names = {GeneticSearchParameter.NO_MUTATION, GeneticSearchParameter.RANDOM_MUTATION};
		return names;
	}

	public String getEliteSlectionName()
	{
		return GeneticSearchParameter.ELITE_SELECTION;
	}

	public String getSingleCrossoverName()
	{
		return GeneticSearchParameter.SINGLE_CROSSOVER_RECOMBINATION;
	}

	public String getNoMutationName()
	{
		return GeneticSearchParameter.NO_MUTATION;
	}

	public String getSelectionStrategyName()
	{
		return localSearchStrategy.getParameters().getSelectionStrategy();
	}

	public String getRecombinationStrategyName()
	{
		return localSearchStrategy.getParameters().getReproductionStrategy();
	}

	public String getMutationStrategyName()
	{
		return localSearchStrategy.getParameters().getMutationStrategy();
	}
}
