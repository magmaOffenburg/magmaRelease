/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.impl;

import kdo.domain.IProblem;
import kdo.domain.IProblemState;
import kdo.domain.ITwoPlayerProblemState;
import kdo.search.strategy.ITwoPlayerSearchStrategy;
import kdo.search.strategy.base.StrategyBase;

/**
 * Contains general search functionality.
 */
public class GeneralTwoPlayerSearch extends StrategyBase
{
	/**
	 * Default constructor for strategies
	 * @param name the name of this strategy
	 */
	public GeneralTwoPlayerSearch(String name)
	{
		super(name);
	}

	/**
	 * Plays the passed two players game by making moves for both players.
	 * @param problem the domain problem for which the search is done
	 * @param strategy1 the two player search strategy to be used for player1
	 * @param strategy2 the two player search strategy to be used for player2
	 * @return 1 if player 1 won, 2 if player 2 won, 0 if none won
	 */
	public static int playGame(IProblem problem, ITwoPlayerSearchStrategy strategy1, ITwoPlayerSearchStrategy strategy2)
	{
		// create the root node and pass it to the search strategy
		ITwoPlayerProblemState currentState = (ITwoPlayerProblemState) problem.getInitialState();
		ITwoPlayerProblemState bestState;
		boolean finished = false;
		boolean whoPlays = currentState.getNextPlayerFlag();

		// display the current state
		// currentState.display();

		// play until game is over
		while (!finished) {
			// get the successor state with the best utility
			if (whoPlays) {
				bestState = strategy1.getBestSuccessorState(currentState);
			} else {
				bestState = strategy2.getBestSuccessorState(currentState);
			}

			// if we get the same state back, the game is over
			if (bestState == currentState) {
				finished = true;
			} else {
				// perform the best move
				currentState = bestState;
				whoPlays = !whoPlays;
				// inform the problem of its new state (not really necessary here)
				problem.setCurrentState(currentState);
				// display the current state
				currentState.display();
			}
		}

		// display the current state
		currentState.display();

		// return the winner info
		return getGameStatus(currentState);
	}

	/**
	 * Plays a single move in the passed two players game by making moves for the
	 * player passed.
	 * @param problem the domain problem for which the search is done
	 * @param strategy the two player search strategy to be used
	 * @return the next state after the move
	 */
	public static IProblemState playMove(IProblem problem, ITwoPlayerSearchStrategy strategy)
	{
		// create the root node and pass it to the search strategy
		ITwoPlayerProblemState bestState;

		// get the successor node with the best utility
		ITwoPlayerProblemState currentState = (ITwoPlayerProblemState) problem.getCurrentState();
		bestState = strategy.getBestSuccessorState(currentState);

		// display the current state
		bestState.display();

		// return the state we want to get
		return bestState;
	}

	/**
	 * Returns the status of the game as defined in TwoPlayerProblemState.
	 * @param state the current game state
	 * @return the status of the game as defined in TwoPlayerProblemState.
	 */
	public static int getGameStatus(ITwoPlayerProblemState state)
	{
		boolean whoPlays = state.getNextPlayerFlag();
		int result = state.getWinnerStatus();
		if (result != ITwoPlayerProblemState.STATUS_DRAW && result != ITwoPlayerProblemState.STATUS_PLAYER1_WON &&
				result != ITwoPlayerProblemState.STATUS_PLAYER2_WON) {
			// check if the computer already knows who will win
			// TODO: does no longer work since player only passes column to drop
			// into
			if (state.getUtility() == state.getMaxUtility()) {
				if (whoPlays == false) {
					result = ITwoPlayerProblemState.STATUS_PLAYER1_WILL_WIN;
				} else {
					result = ITwoPlayerProblemState.STATUS_PLAYER2_WILL_WIN;
				}
			} else if (state.getUtility() == state.getMinUtility()) {
				if (whoPlays) {
					result = ITwoPlayerProblemState.STATUS_PLAYER1_WILL_WIN;
				} else {
					result = ITwoPlayerProblemState.STATUS_PLAYER2_WILL_WIN;
				}
			}
		}
		return result;
	}
}
