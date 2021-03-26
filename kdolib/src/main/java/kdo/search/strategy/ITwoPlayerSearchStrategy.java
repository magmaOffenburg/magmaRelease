/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy;

import kdo.domain.ITwoPlayerProblemState;

/**
 * Interface for two player search strategies.
 */
public interface ITwoPlayerSearchStrategy {
	/**
	 * Calculates the state with the highest utility representing the best move
	 * the passed player can make in the state passed.
	 * @param currentState the problem state of the game to solve
	 * @return the next state with the best utility, the problem's currentState
	 *         if a terminal state was reached
	 */
	ITwoPlayerProblemState getBestSuccessorState(ITwoPlayerProblemState currentState);
}
