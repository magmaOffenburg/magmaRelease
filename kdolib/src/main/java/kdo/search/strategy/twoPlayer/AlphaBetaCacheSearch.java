/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.twoPlayer;

import java.util.HashMap;
import kdo.domain.ITwoPlayerProblemState;

/**
 * Implementation of the two players alpha beta search strategy. Extended with a
 * cache for already evaluated states that if duplicated in the search tree may
 * be pruned.
 */
public class AlphaBetaCacheSearch extends AlphaBetaSearch
{
	/** buffer to avoid evaluation of same states */
	private HashMap<ITwoPlayerProblemState, ITwoPlayerProblemState> evaluatedStates;

	/** debug only number of buffer hits */
	private long bufferHits;

	/**
	 * Creates the buffer for already evaluated states
	 * @param size the initial size of the cache (in number of states)
	 */
	public AlphaBetaCacheSearch(int size)
	{
		evaluatedStates = new HashMap<ITwoPlayerProblemState, ITwoPlayerProblemState>(size);
		bufferHits = 0;
	}

	/**
	 * Calculates the state with the highest utility representing the best move
	 * the passed player can make in the state passed. Initializes the state
	 * cache.
	 *
	 * @param currentState the problem state of the game to solve
	 * @return the next node with the best utility, this node if a terminal state
	 *         was reached
	 */
	@Override
	public ITwoPlayerProblemState getBestSuccessorState(ITwoPlayerProblemState currentState)
	{
		long start = System.currentTimeMillis();
		evaluatedStates.clear();
		bufferHits = 0;

		ITwoPlayerProblemState result;
		result = alphaBetaUtility(currentState, currentState.getNextPlayerFlag(), true, Float.NEGATIVE_INFINITY,
				Float.POSITIVE_INFINITY, 0);

		System.out.println("alpha beta cache utility: " + result.getUtility() +
						   " time: " + (System.currentTimeMillis() - start) + " cache hits: " + bufferHits);

		return result;
	}

	/**
	 * Calculates recursively the utility of the node passed
	 * @param problem the domain problem for which the search is done
	 * @param currentState the state for which to calculate the utility
	 * @param whoPlays true if player1 is next to move, false if player2
	 * @param isMax true, if this node is a max node
	 * @return the node with the highest utility representing the utility of the
	 *         node passed
	 */
	@Override
	protected ITwoPlayerProblemState alphaBetaUtility(
			ITwoPlayerProblemState currentState, boolean whoPlays, boolean isMax, float alpha, float beta, int depth)
	{
		// check if this is a state we already evaluated
		if (depth < 6 && evaluatedStates.containsKey(currentState)) {
			bufferHits++;
			ITwoPlayerProblemState other = evaluatedStates.get(currentState);
			currentState.setUtility(other.getUtility());
			// System.out.println("cache hit depth: " + depth + " kdo.util: " +
			// currentState.getUtility());
			return currentState;
		}

		// call alpha beta
		ITwoPlayerProblemState bestState;
		bestState = super.alphaBetaUtility(currentState, whoPlays, isMax, alpha, beta, depth);

		// remember that we did this state
		if (depth < 6 && currentState != bestState) {
			currentState.setUtility(bestState.getUtility());
			evaluatedStates.put(currentState, currentState);
		}

		return bestState;
	}

	/**
	 * Returns the number of hits where a state was already in the buffer
	 * @return the number of hits where a state was already in the buffer
	 */
	protected long getBufferHits()
	{
		return bufferHits;
	}
}
