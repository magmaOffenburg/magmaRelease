/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.impl;

import kdo.domain.IOptimizationProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.ILocalSearchStrategy;
import kdo.search.strategy.base.RandomStrategy;
import kdo.search.strategy.local.genetic.IOptimizationStateGroup;
import kdo.search.strategy.local.genetic.ISwarm;
import kdo.search.strategy.local.genetic.representation.Swarm;
import kdo.util.observer.IObserver;
import kdo.util.observer.IPublishSubscribe;

/**
 * @author klaus
 *
 */
public class PSOSearch extends RandomStrategy implements ILocalSearchStrategy, IObserver<ISwarm>
{
	protected final OptimizationSearchParameter parameters;

	protected ISwarm population;

	/** the observer manager to be called on model changes */
	protected IPublishSubscribe<ISwarm> observerManager;

	/** true if the next call to start should restart optimization */
	protected boolean restart;

	public PSOSearch(OptimizationSearchParameter parameterObject)
	{
		super("PSOSearch", parameterObject.getRandomSource());
		parameters = parameterObject;
		restart = true;
		createNewPopulation();
	}

	protected IOptimizationStateGroup createNewPopulation()
	{
		// TODO: change Population
		// create multiple Nets of Individuums
		// Set random start Position
		// Set random start Velocity

		population = new Swarm((IOptimizationProblem) parameters.getDomain(), parameters.getGroups(),
				parameters.getPopulationSize(), parameters.isExpensiveUtilityCalculation(),
				parameters.createFitnessCalculationStrategy(), (IObserver<ISwarm>) this, null);

		parameters.setPopulationSize(population.getSize());
		population.initializeParticles();
		return population;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.ILocalSearchStrategy#search(domain.IProblemState)
	 */
	@Override
	public IProblemState search(IProblemState initialState)
	{
		// We do not make use of the provided initial State
		// TODO we could use the initial state to create random variations of it
		// for the initial population
		createNewPopulation();
		return population.search(parameters.getIterations(), parameters.getMaxRuntime());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.local.genetic.StrategyBase#getName()
	 */
	@Override
	public String getName()
	{
		StringBuffer result = new StringBuffer(500);
		result.append(super.getName());
		result.append(" generations: ").append(parameters.getIterations());
		result.append(" population: ").append(population);
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.IStrategy#setObserverManager(kdo.util.observer.
	 * IPublishSubscribe)
	 */
	public void setObserverManager(IPublishSubscribe<ISwarm> observer)
	{
		observerManager = observer;
		observerManager.onStateChange(population);
	}

	/**
	 * @return the observerManager
	 */
	public IPublishSubscribe<ISwarm> getObserverManager()
	{
		return observerManager;
	}

	@Override
	public void update(ISwarm content)
	{
		if (getObserverManager() != null) {
			getObserverManager().onStateChange(content);
		}
	}
}
