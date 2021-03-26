/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.impl;

import kdo.domain.IGeneticProblem;
import kdo.domain.IOptimizationState;
import kdo.domain.IProblemState;
import kdo.search.strategy.base.RandomStrategy;
import kdo.search.strategy.local.genetic.IMutation;
import kdo.search.strategy.local.genetic.IOptimizationStateGroup;
import kdo.search.strategy.local.genetic.IPopulation;
import kdo.search.strategy.local.genetic.IReproduction;
import kdo.search.strategy.local.genetic.ISearch;
import kdo.search.strategy.local.genetic.ISelection;
import kdo.search.strategy.local.genetic.fitness.IFitnessCalculator;
import kdo.search.strategy.local.genetic.representation.Population;
import kdo.util.observer.IObserver;
import kdo.util.observer.IPublishSubscribe;

/**
 * @author klaus
 *
 */
public class GeneticSearch extends RandomStrategy implements ISearch, IObserver<IOptimizationStateGroup>
{
	private final GeneticSearchParameter parameters;

	private Population population;

	/** the observer manager to be called on model changes */
	private IPublishSubscribe<IPopulation> observerManager;

	/** true if the next call to start should restart optimization */
	private boolean restart;

	public GeneticSearch(GeneticSearchParameter parameterObject)
	{
		super("GeneticSearch", parameterObject.getRandomSource());
		parameters = parameterObject;
		restart = false;
		createNewPopulation();
	}

	private IPopulation createNewPopulation()
	{
		ISelection selection = parameters.createSelectionStrategy();
		IReproduction reproduction = parameters.createRecombinationStrategy();
		IMutation mutation = parameters.createMutationStrategy();
		IFitnessCalculator fitnessCalculator = parameters.createFitnessCalculationStrategy();
		population = new Population((IGeneticProblem) parameters.getDomain(), parameters.getGenders(),
				parameters.getPopulationSize(), selection, reproduction, mutation, parameters.getOldToNew(),
				parameters.getParentsPerIndividuum(), parameters.isExpensiveUtilityCalculation(), fitnessCalculator,
				(IObserver<IOptimizationStateGroup>) this, null);

		// the real population size depends on the number of genders
		parameters.setPopulationSize(population.getSize());
		if (!parameters.isExpensiveUtilityCalculation()) {
			population.calculateFitness();
		}
		return population;
	}

	@Override
	public IProblemState search(IProblemState initialState)
	{
		// We do not make use of the provided initial State
		// TODO we could use the initial state to create random variations of it
		// for the initial population
		return start();
	}

	/**
	 * Starts or continues the optimisation.
	 */
	@Override
	public IProblemState start()
	{
		if (restart) {
			createNewPopulation();
			restart = false;
		}
		return population.breed(parameters.getGenerations(), parameters.getMaxRuntime());
	}

	/**
	 * Breeds a single generation.
	 */
	@Override
	public IProblemState step()
	{
		if (restart) {
			createNewPopulation();
			restart = false;
		}
		IOptimizationState breed = population.breed(1, parameters.getMaxRuntime());
		restart = false;
		return breed;
	}

	/**
	 * Call to interrupt the search for better solutions. Kept for downward
	 * compatibility.
	 */
	@Override
	public void pause()
	{
		population.interrupt();
	}

	/**
	 * Call to interrupt the search for better solutions. Kept for downward
	 * compatibility.
	 */
	@Override
	public void stop()
	{
		population.interrupt();
		restart = true;
	}

	/**
	 * Restart the optimization with a new population.
	 */
	@Override
	public void rewind()
	{
		IPopulation newPopulation = createNewPopulation();
		update(newPopulation);
		restart = false;
	}

	/**
	 * Call to interrupt the search for better solutions. Kept for downward
	 * compatibility.
	 */
	@Override
	public void interrupt()
	{
		pause();
	}

	@Override
	public String getName()
	{
		StringBuffer result = new StringBuffer(500);
		result.append(super.getName());
		result.append(" generations: ").append(parameters.getGenerations());
		result.append(" population: ").append(population);
		result.append(" " + parameters.getReproductionStrategy());
		result.append(" " + parameters.getSelectionStrategy());
		result.append(" " + parameters.getMutationStrategy());
		return result.toString();
	}

	public void setObserverManager(IPublishSubscribe<IPopulation> observer)
	{
		observerManager = observer;
		observerManager.onStateChange(population);
	}

	/**
	 * @return the observerManager
	 */
	public IPublishSubscribe<IPopulation> getObserverManager()
	{
		return observerManager;
	}

	/**
	 * @return the number of generations the population has been breeded
	 */
	public int getCurrentGeneration()
	{
		return population.getGenerations();
	}

	/**
	 * @param reproductionStrategy
	 * @see
	 *         kdo.search.strategy.local.genetic.IPopulation#setReproductionStrategy(kdo.search.strategy.local.genetic.IReproduction)
	 */
	public void setReproductionStrategy(IReproduction reproductionStrategy)
	{
		population.setReproductionStrategy(reproductionStrategy);
	}

	/**
	 * @param mutationStrategy
	 * @see
	 *         kdo.search.strategy.local.genetic.IPopulation#setMutationStrategy(kdo.search.strategy.local.genetic.IMutation)
	 */
	public void setMutationStrategy(IMutation mutationStrategy)
	{
		population.setMutationStrategy(mutationStrategy);
	}

	/**
	 * @param selectionStrategy
	 * @see
	 *         kdo.search.strategy.local.genetic.IPopulation#setSelectionStrategy(kdo.search.strategy.local.genetic.ISelection)
	 */
	public void setSelectionStrategy(ISelection selectionStrategy)
	{
		population.setSelectionStrategy(selectionStrategy);
	}

	/**
	 * @return the parameters
	 */
	public GeneticSearchParameter getParameters()
	{
		return parameters;
	}

	public void setIndividuumMutationProbability(float value)
	{
		parameters.setIndividuumMutationProbability(value);
		population.setIndividuumMutationProbability(value);
	}

	public void setGeneMutationProbability(float value)
	{
		parameters.setGeneMutationProbability(value);
		population.setGeneMutationProbability(value);
	}

	public void setEliteSelectionRatio(float value)
	{
		parameters.setEliteSelectionRatio(value);
	}

	public void setOldToNew(float value)
	{
		parameters.setOldToNew(value);
		population.setOldToNew(value);
	}

	/**
	 * @return the population
	 */
	@Override
	public IPopulation getPopulation()
	{
		return population;
	}

	/**
	 * Called to indicate a state/utility change
	 */
	@Override
	public void update(IOptimizationStateGroup population)
	{
		if (population instanceof IPopulation) {
			if (getObserverManager() != null) {
				getObserverManager().onStateChange((IPopulation) population);
			}
		}
	}

	/**
	 * @return the number of generations the population has been breeded
	 */
	@Override
	public int getCurrentIteration()
	{
		return population.getGenerations();
	}
}
