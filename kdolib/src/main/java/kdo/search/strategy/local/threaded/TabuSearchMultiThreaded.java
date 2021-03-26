/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.threaded;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.local.TabuSearch;

/**
 * Implementation of local search with tabu list for cycle prevention. In order
 * to work you have to make sure that you have implemented a meaningfull equals
 * method for your domain's state class!
 */
public class TabuSearchMultiThreaded extends TabuSearch
{
	/** used to synchronize threads on */
	private final Object synchroObject;

	/** the thread pool to run the neighborhood exploration */
	private ExecutorService pool;

	/** the size of the threadpool */
	private int threadPoolSize;

	/**
	 * Constructor setting the size of the tabu list
	 * @param shortTermTabuSize the size of the short term tabu list
	 * @param maxIterations the maximal number of iterations done before stopping
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 * @param threadPoolSize the size of the thread pool to run with
	 */
	public TabuSearchMultiThreaded(int shortTermTabuSize, long maxIterations, long maxRuntime, int threadPoolSize)
	{
		super("Tabu Search (parallel). shortTermTabuSize: " + shortTermTabuSize + " maxIterations: " + maxIterations +
						" threads: " + threadPoolSize,
				shortTermTabuSize, maxIterations, maxRuntime);
		this.threadPoolSize = threadPoolSize;
		synchroObject = new Object();
	}

	@Override
	protected void exploreNeighborhood(IProblem problem, IProblemState currentState)
	{
		try {
			pool = Executors.newFixedThreadPool(threadPoolSize);
			Iterator<IOperator> operatorIterator = neighborhoodIterator(currentState);
			while (operatorIterator.hasNext()) {
				pool.execute(new Handler(currentState, operatorIterator.next()));
			}
			pool.shutdown();
			pool.awaitTermination(getRemainingRuntime(), TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates and checks for one neighborhood state if it is better than others
	 */
	class Handler implements Runnable
	{
		/** operator to create the neighborhood state */
		private IOperator operator;

		/** the state from which to explore */
		private IProblemState currentState;

		Handler(IProblemState currentState, IOperator operator)
		{
			this.currentState = currentState;
			this.operator = operator;
		}

		@Override
		public void run()
		{
			// System.out.println("checking. " + operator.getName());
			IProblemState successorState = operator.getSuccessorState(currentState);
			if (!isInTabuList(successorState)) {
				// it might be expensive to calculate the utility so we do it
				// before the synchronized block (it will be cached)
				successorState.calculateUtility();
				synchronized (synchroObject)
				{
					if (bestSuccessorState == null || bestSuccessorState.isWorseThan(successorState)) {
						// we have the steepest gradient so far that is not tabu
						bestSuccessorState = successorState;
						bestOperator = operator;
					}
				}
			}
		}
	}
}
