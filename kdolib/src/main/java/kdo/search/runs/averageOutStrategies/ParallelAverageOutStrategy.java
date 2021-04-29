/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.runs.averageOutStrategies;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import kdo.domain.IUtilityCalculator;

/**
 * Does a serial run of all trials, expecting that the server is already
 * running.
 * @author kdorer
 */
public class ParallelAverageOutStrategy extends AverageOutStrategyBase
{
	/** used to synchronize threads on */
	private Object synchroObject;

	public ParallelAverageOutStrategy(int averageOutRuns, boolean verbose, IDomainVisitor visitor)
	{
		super(averageOutRuns, verbose, visitor);
		synchroObject = new Object();
	}

	@Override
	public double getAverageUtility(AverageOutState state)
	{
		try {
			// we make the thread pool as large as the number of oversampling runs
			// how many of them can run in parallel is controlled by the domain
			ExecutorService pool = Executors.newFixedThreadPool(averageOutRuns);

			Handler[] handler = new Handler[averageOutRuns];
			for (int i = 0; i < averageOutRuns; i++) {
				handler[i] = new Handler(i, state);
			}

			for (int i = 0; i < averageOutRuns; i++) {
				pool.execute(handler[i]);
			}

			pool.shutdown();
			pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
			synchronized (this)
			{
				// keep printing results synchronous
				handler[0].utilityCalculator.printParams();
				finishedCalculation(state);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return state.averageUtility;
	}

	/**
	 * Creates and checks for one neighborhood state if it is better than others
	 */
	class Handler implements Runnable
	{
		private int id;

		IUtilityCalculator utilityCalculator;

		private AverageOutState state;

		Handler(int id, AverageOutState state)
		{
			this.id = id;
			this.state = state;
		}

		@Override
		public void run()
		{
			double util = 0.0f;
			int repeatCount = 0;
			do {
				utilityCalculator = state.getUtilityCalculator(createUCParameters(id));
				util = utilityCalculator.singleRunUtility(id, averageOutRuns, repeatCount);
				repeatCount++;

				// we do not count invalid measurement runs
			} while (util == Double.NEGATIVE_INFINITY);

			// measures for this individuum
			synchronized (synchroObject)
			{
				addValues(utilityCalculator.getProperties(), util, state);
			}
		}
	}
}
