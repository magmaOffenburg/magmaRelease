/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.twoPlayer;

import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.ITwoPlayerProblemState;
import kdo.search.strategy.ITwoPlayerSearchStrategy;
import kdo.search.strategy.base.StrategyBase;

/**
 * Implementation of the two players alpha beta search strategy.
 */
public class AlphaBetaSearch extends StrategyBase implements ITwoPlayerSearchStrategy
{
	/**
	 * Default constructor for strategies
	 */
	public AlphaBetaSearch()
	{
		super("Alpha-Beta Search");
	}

	/**
	 * Calculates the state with the highest utility representing the best move
	 * the passed player can make in the state passed.
	 * @param currentState the problem state of the game to solve
	 * @return the next node with the best utility, this node if a terminal state
	 *         was reached
	 */
	@Override
	public ITwoPlayerProblemState getBestSuccessorState(ITwoPlayerProblemState currentState)
	{
		long start = System.currentTimeMillis();

		ITwoPlayerProblemState result;
		result = alphaBetaUtility(currentState, currentState.getNextPlayerFlag(), true, Float.NEGATIVE_INFINITY,
				Float.POSITIVE_INFINITY, 0);

		System.out.println(
				"alpha beta utility: " + result.getUtility() + " time: " + (System.currentTimeMillis() - start));

		return result;
	}

	/**
	 * Calculates recursively the utility of the node passed
	 * @param currentState the state for which to calculate the utility
	 * @param whoPlays true if player1 is next to move, false if player2
	 * @param isMax true, if this node is a max node
	 * @return the node with the highest utility representing the utility of the
	 *         node passed
	 */
	protected ITwoPlayerProblemState alphaBetaUtility(
			ITwoPlayerProblemState currentState, boolean whoPlays, boolean isMax, float alpha, float beta, int depth)
	{
		// check if we reached a cutoff state
		if (currentState.isCutoffState(depth, whoPlays)) {
			// we estimate the utility of this state
			currentState.setUtility(currentState.getTwoPlayerUtility(whoPlays));
			return currentState;
		}

		List<IOperator> operators = currentState.getOperators();
		int size = operators.size();

		// if there are no moves from here we return the utility
		if (size == 0) {
			currentState.setUtility(currentState.getTwoPlayerUtility(whoPlays));
			return currentState;
		}

		// explore the new nodes
		float nextUtility;
		ITwoPlayerProblemState bestState = null;
		ITwoPlayerProblemState nextState;
		IOperator operator;

		// find the node with max/min utility
		for (int i = 0; i < size; i++) {
			operator = operators.get(i);
			// calculate the utility if we go this way
			nextState = (ITwoPlayerProblemState) operator.getSuccessorState(currentState);

			nextUtility = alphaBetaUtility(nextState, whoPlays, !isMax, alpha, beta, depth + 1).getUtility();

			if (isMax) {
				// if better move
				if (nextUtility > alpha) {
					alpha = nextUtility;
					bestState = nextState;
					if (alpha >= beta) {
						// we can cut
						currentState.setUtility(beta);
						return currentState;
					}
				}

			} else {
				if (nextUtility < beta) {
					beta = nextUtility;
					bestState = nextState;
					if (beta <= alpha) {
						// we can cut
						currentState.setUtility(alpha);
						return currentState;
					}
				}
			}
		}

		if (bestState == null) {
			bestState = currentState;
		}
		// we only set the utility of the best node
		if (isMax) {
			bestState.setUtility(alpha);
		} else {
			bestState.setUtility(beta);
		}
		return bestState;
	}
}
