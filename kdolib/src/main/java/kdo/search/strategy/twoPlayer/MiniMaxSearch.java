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
 * Implementation of the two players miniMax search strategy.
 */
public class MiniMaxSearch extends StrategyBase implements ITwoPlayerSearchStrategy
{
	/**
	 * Default constructor for strategies
	 */
	public MiniMaxSearch()
	{
		super("MiniMax Search");
	}

	/**
	 * Calculates the state with the highest utility representing the best move
	 * the passed player can make in the state passed.
	 * @param problem the problem (game) to solve
	 * @return the best successor state, this state if a terminal state was
	 *         reached
	 */
	@Override
	public ITwoPlayerProblemState getBestSuccessorState(ITwoPlayerProblemState currentState)
	{
		long start = System.currentTimeMillis();

		ITwoPlayerProblemState result;
		result = miniMaxUtility(currentState, currentState.getNextPlayerFlag(), true, 0);
		System.out.println(
				"minimax utility: " + result.getUtility() + " time: " + (System.currentTimeMillis() - start));

		return result;
	}

	/**
	 * Calculates recursively the utility of the node passed
	 * @param currentNode the node to calculate the utility for
	 * @param whoPlays true if player 1 is next to move, false if player 2
	 * @param isMax true, if this node is a max node
	 * @param depth the current depth in the search tree
	 * @return the node with the highest utility representing the utility of the
	 *         node passed
	 */
	private ITwoPlayerProblemState miniMaxUtility(
			ITwoPlayerProblemState state, boolean whoPlays, boolean isMax, int depth)
	{
		// check if we reached a goal state
		if (state.isCutoffState(depth, whoPlays)) {
			// we found a solution
			state.setUtility(state.getTwoPlayerUtility(whoPlays));
			return state;
		}

		List<IOperator> operators = state.getOperators();
		int size = operators.size();

		// if there are no moves from here we return the utility
		if (size == 0) {
			state.setUtility(state.getTwoPlayerUtility(whoPlays));
			return state;
		}

		// explore the new nodes
		float nextUtility;
		ITwoPlayerProblemState bestState = null;
		ITwoPlayerProblemState nextState;
		IOperator operator;
		float resultUtility;
		if (isMax) {
			resultUtility = Float.NEGATIVE_INFINITY;
		} else {
			resultUtility = Float.POSITIVE_INFINITY;
		}

		// find the state with max/min utility
		for (int i = 0; i < size; i++) {
			operator = operators.get(i);
			// calculate the utility if we go this way
			nextState = (ITwoPlayerProblemState) operator.getSuccessorState(state);
			nextUtility = miniMaxUtility(nextState, whoPlays, !isMax, depth + 1).getUtility();
			if (isMax) {
				if (nextUtility > resultUtility) {
					resultUtility = nextUtility;
					bestState = nextState;
				}
			} else {
				if (nextUtility < resultUtility) {
					resultUtility = nextUtility;
					bestState = nextState;
				}
			}
		}
		// we only set the utility of the best state
		bestState.setUtility(resultUtility);
		return bestState;
	}
}
