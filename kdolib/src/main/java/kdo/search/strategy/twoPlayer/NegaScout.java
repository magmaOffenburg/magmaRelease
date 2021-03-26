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
 * Implementation of the two players negascout search strategy. Not yet
 * finished!!
 */
public class NegaScout extends StrategyBase implements ITwoPlayerSearchStrategy
{
	/**
	 * Default constructor for strategies
	 */
	public NegaScout()
	{
		super("NegaScout Search");
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
		result = negaScoutUtility(currentState, currentState.getNextPlayerFlag(), true, Float.NEGATIVE_INFINITY,
				Float.POSITIVE_INFINITY, 0);

		System.out.println("nega scout utility: " + result.getUtility());
		System.out.println("time: " + (System.currentTimeMillis() - start));

		return result;
	}

	// this is not nega scout yet !!!

	/**
	 * Calculates recursively the utility of the node passed
	 * @param currentNode the node to calculate the utility for
	 * @param whoPlays true if player1 is next to move, false if player2
	 * @param isMax true, if this node is a max node
	 * @return the node with the highest utility representing the utility of the
	 *         node passed
	 */
	private ITwoPlayerProblemState negaScoutUtility(
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
		float gamma = beta;
		// float player = whoPlays ?1.0f : -1.0f;
		float nextUtility;
		ITwoPlayerProblemState bestState = null;
		ITwoPlayerProblemState nextState;
		IOperator operator;

		// find the node with max/min utility
		for (int i = 0; i < size; i++) {
			operator = operators.get(i);
			// calculate the utility if we go this way
			nextState = (ITwoPlayerProblemState) operator.getSuccessorState(currentState);

			nextUtility = negaScoutUtility(nextState, whoPlays, !isMax, gamma, alpha, depth + 1).getUtility();

			if (nextUtility > alpha && nextUtility < beta && i > 0) {
				nextUtility = negaScoutUtility(nextState, whoPlays, !isMax, beta, nextUtility, depth + 1).getUtility();

				if (isMax) {
					// if better move
					if (nextUtility > alpha) {
						alpha = nextUtility;
						bestState = nextState;
						if (alpha >= beta) {
							// we can cut
							bestState.setUtility(beta);
							return bestState;
						}
					}
					gamma = alpha + 1;

				} else {
					if (nextUtility < beta) {
						beta = nextUtility;
						bestState = nextState;
						if (beta <= alpha) {
							// we can cut
							bestState.setUtility(alpha);
							return bestState;
						}
					}
					gamma = alpha - 1;
				}
			}
			if (depth < 5)
				System.out.println("depth: " + depth + " kdo.util: " + nextUtility);
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
	/*
	 * public double negaScout (Color, Alpha, Beta, Depth, MaxDepth : Integer) :
	 * Integer; var Value : Integer; Gamma : Integer;
	 *
	 * begin if Depth = MaxDepth then NegaScout := EvaluatePosition (Color)
	 *
	 * end else begin GenerateMoves(Color, MoveList);
	 *
	 * Gamma := Beta;
	 *
	 * For Each Move in MoveList do begin MoveForward (Move);
	 *
	 * Value := NegaScout (-Color, Gamma, Alpha, Depth +1, MaxDepth);
	 *
	 * if ( (Color *Value > Color *Alpha) and (Color *Value < Color *Beta) and
	 * (Depth < MaxDepth -1) and (Move <> First (MoveList)) ) then
	 *
	 * Value := NegaScout (-Color, Beta, Value, Depth +1, MaxDepth);
	 *
	 * if Color *Value > Color *Alpha then Alpha := Value;
	 *
	 * MoveBack (Move);
	 *
	 * if Color *Alpha >= Color *Beta then Return Alpha;
	 *
	 * Gamma := Alpha +Color; end;
	 *
	 * NegaScout := Alpha; end; end;
	 */
}
