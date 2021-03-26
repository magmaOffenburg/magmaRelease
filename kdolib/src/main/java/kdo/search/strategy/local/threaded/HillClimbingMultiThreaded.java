/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.local.threaded;

import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.search.strategy.local.HillClimbing;

public class HillClimbingMultiThreaded extends HillClimbing
{
	/** the thread pool to run the neighborhood exploration */
	private ExecutorService pool;

	/** the size of the thread pool */
	private int threadPoolSize;

	public HillClimbingMultiThreaded(long maxRuntime, long maxCycles, int threadPoolSize)
	{
		super("HillClimbing_MultiThreaded", maxRuntime, maxCycles);
		this.threadPoolSize = threadPoolSize;
	}

	@Override
	protected Iterator<IOperator> exploreNeighborhood(IProblem problem, IProblemState currentState)
	{
		Iterator<IOperator> operatorIterator = neighborhoodIterator(currentState);
		try {
			pool = Executors.newFixedThreadPool(threadPoolSize);
			while (operatorIterator.hasNext()) {
				pool.execute(new Handler(currentState, operatorIterator.next()));
			}
			pool.shutdown();
			pool.awaitTermination(getRemainingRuntime(), TimeUnit.SECONDS);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return operatorIterator;
	}

	private class Handler implements Runnable
	{
		/** operator to create the neighborhood state */
		private IOperator operator;

		/** the state from which to explore */
		private IProblemState currentState;

		public Handler(IProblemState currentState, IOperator operator)
		{
			this.currentState = currentState;
			this.operator = operator;
		}

		@Override
		public void run()
		{
			IProblemState successorState = operator.getSuccessorState(currentState);
			if (successorState != currentState) {
				successorState.calculateUtility();

				synchronized (bestSuccessorState)
				{
					if (bestSuccessorState.isWorseThan(successorState)) {
						bestSuccessorState = successorState;
						bestOperator = operator;
					}
				}
			}
		}
	}
}
