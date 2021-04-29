/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.genetic.fitness;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kdo.domain.IOptimizationState;
import kdo.domain.IProblemState;

/**
 * @author kdorer
 *
 */
public class ParallelFitnessCalculator implements IFitnessCalculator
{
	/** the size of the thread pool */
	private int threadPoolSize;

	public ParallelFitnessCalculator(int threadPoolSize)
	{
		this.threadPoolSize = threadPoolSize;
	}

	@Override
	public double calculateFitness(List<List<IOptimizationState>> genderStates)
	{
		try {
			ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);

			List<Handler> handler = new ArrayList<>();
			for (List<IOptimizationState> net : genderStates) {
				for (IOptimizationState currentState : net) {
					handler.add(new Handler(currentState));
				}
			}

			for (Handler currentHandler : handler) {
				pool.execute(currentHandler);
			}

			pool.shutdown();
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

			// calculate average
			double sum = 0;
			int n = 0;
			for (List<IOptimizationState> net : genderStates) {
				for (IOptimizationState currentState : net) {
					sum += currentState.getUtility();
					n++;
				}
			}
			return sum / n;

		} catch (InterruptedException e) {
			e.printStackTrace();
			return -1.002;
		}
	}

	/**
	 * Creates and checks for one neighborhood state if it is better than others
	 */
	class Handler implements Runnable
	{
		private IProblemState currentState;

		Handler(IProblemState currentState)
		{
			this.currentState = currentState;
		}

		@Override
		public void run()
		{
			currentState.calculateUtility();
		}
	}
}
