/*
 * Copyright (c) 2001-2004 Living Systems(R) GmbH, Germany.
 * All rights reserved.
 * Original Author: kdorer
 *
 * $Source: /cvs/Research/lecture/AI/src/domain/ITwoPlayerProblemState.java,v $
 * $Date: 2007/01/04 09:33:10 $
 */
package kdo.domain;

public interface ITwoPlayerProblemState extends IProblemState {
	// Konstanten fuer den Spielstatus
	int STATUS_DRAW = 0;

	int STATUS_PLAYER1_WON = 1;

	int STATUS_PLAYER2_WON = 2;

	int STATUS_PLAYER1_NEXT = 3;

	int STATUS_PLAYER2_NEXT = 4;

	int STATUS_PLAYER1_WILL_WIN = 5;

	int STATUS_PLAYER2_WILL_WIN = 6;

	int STATUS_INVALID = 7;

	/**
	 * Returns information on who won the game or who is next.
	 * @return returns ProblemState.STATUS_DRAW in case of the game ended in a
	 *         draw <b> ProblemState.STATUS_PLAYER1_WON if player 1 is the winner
	 *         <b> ProblemState.STATUS_PLAYER2_WON if player 2 is the winner <b>
	 *         ProblemState.STATUS_PLAYER1_NEXT if player 1 is next to move <b>
	 *         ProblemState.STATUS_PLAYER2_NEXT if player 2 is next to move <b>
	 */
	int getWinnerStatus();

	/**
	 * Checks if search can be stopped at this state. The default implementation
	 * just checks if this is a goal state.
	 * @param depth the depth in search tree
	 * @param isMax true if this is player 1, false if player 2
	 * @return true if search can be stopped at this state
	 */
	boolean isCutoffState(int depth, boolean isMax);

	/**
	 * Returns the highest utility achievable in case of a win
	 * @return the highest utility achievable in case of a win
	 */
	float getMaxUtility();

	/**
	 * Returns the lowest utility achievable (in case of a loss)
	 * @return the lowest utility achievable (in case of a loss)
	 */
	float getMinUtility();

	/**
	 * Returns the player who is next to move (true if first player)
	 * @return the player who is next to move (true if first player)
	 */
	boolean getNextPlayerFlag();

	/**
	 * Checks if this state is a goal state of this problem. This is true if
	 * either player1 or player2 has won or if there is a draw
	 * @return true if this state is a goal state of this problem
	 */
	boolean checkGoalState();

	/**
	 * Returns the utility of this state for the agent passed
	 * @param isMax true if this is player 1, false if player 2
	 * @return the utility of this state. The default implementation returns 0.0
	 */
	float getTwoPlayerUtility(boolean isMax);
}