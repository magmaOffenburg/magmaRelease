/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.local.concurrent;

import java.util.Collections;
import java.util.Iterator;
import kdo.domain.IOperator;
import kdo.domain.IProblem;
import kdo.domain.IProblemState;

/**
 * Represents a hill climbing local search where not the full neighborhood is
 * explored, but only one random direction. If that improves the current state,
 * we take it, if not we do not. The search stops if maxCyclesWithoutImprovement
 * cycles did not produce an improvement.
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.3 $
 */
public class HillClimbingRandomCC extends HillClimbingCC
{
	/** the number of cycles that did not produce an improvement */
	private int cyclesWithoutImprovement;

	/** the maximum number of cycles that did not produce an improvement */
	private final int maxCyclesWithoutImprovement;

	/**
	 * Creates an instance that does only a limited amount of cycles without
	 * improvement before it stops.
	 * @param maxCyclesWithoutImprovement the
	 * @param maxRuntime the maximal runtime the search strategy may use for a
	 *        single call to search, -1 if no time restriction
	 */
	public HillClimbingRandomCC(int maxCyclesWithoutImprovement, long maxRuntime, long maxCycles)
	{
		super(maxRuntime, maxCycles);

		cyclesWithoutImprovement = 0;
		this.maxCyclesWithoutImprovement = maxCyclesWithoutImprovement;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.IStrategy#getName()
	 */
	@Override
	public String getName()
	{
		return "HillClimbing Random Operator Concurrent. maxCyclesWithoutImprovement: " + maxCyclesWithoutImprovement;
	}

	/**
	 * Returns a single random operator of the neighborhood. If too many cycles
	 * did not produce an improvement, all operators for normal hill climbing are
	 * returned.
	 *
	 * @param currentState the state of the domain for which to get the operators
	 * @return a single random operator of the neighborhood
	 *
	 * @see kdo.search.strategy.local.HillClimbing#getNeighborhood(kdo.domain.Problem,
	 *      kdo.domain.ProblemState)
	 */
	@Override
	protected Iterator<IOperator> neighborhoodIterator(IProblemState currentState)
	{
		if (cyclesWithoutImprovement > maxCyclesWithoutImprovement) {
			// after too many cycles without improvement we switch to normal hill
			// climbing
			return super.neighborhoodIterator(currentState);
		} else {
			return Collections.singletonList(currentState.getRandomOperator()).iterator();
		}
	}

	/**
	 * Checks if an improvement was made or if the number of non-improving cycles
	 * is low enough
	 *
	 * @param bestSuccessorState the best state of the neighborhood to which to
	 *        go
	 * @param currentState the current state where we are
	 * @return false if an improvement was made or if the number of non-improving
	 *         cycles is low enough
	 *
	 * @see kdo.search.strategy.local.HillClimbing#reachedLocalMaximum(kdo.domain.ProblemState,
	 *      kdo.domain.ProblemState, int)
	 */
	@Override
	protected boolean canStopHillClimbing(IProblem problem, IProblemState bestSuccessorState,
			IProblemState currentState, Iterator<IOperator> neighborhood)
	{
		if (cyclesWithoutImprovement > maxCyclesWithoutImprovement) {
			// too many cycles without improvement, so normal hill climbing
			return super.canStopHillClimbing(problem, bestSuccessorState, currentState, neighborhood);
		}

		if (bestSuccessorState.getUtility() > currentState.getUtility()) {
			// we have an improvement
			cyclesWithoutImprovement = 0;
			return false;
		}
		cyclesWithoutImprovement++;
		return canStop();
	}
}
