/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.domain.model;

import kdo.domain.IProblem;
import kdo.domain.ITwoPlayerProblemState;

/**
 * Represents a single state of a problem
 */
public abstract class TwoPlayerProblemState extends ProblemState implements ITwoPlayerProblemState
{
	/** flag for the player who is next to move */
	private final boolean nextPlayerFlag;

	/**
	 * Constructor to create a new state
	 * @param next the player who is next to make a move
	 */
	public TwoPlayerProblemState(IProblem problem, boolean next)
	{
		super(problem);
		nextPlayerFlag = next;
	}

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#getWinnerStatus()
	 */
	public int getWinnerStatus()
	{
		if (isDrawState()) {
			return ITwoPlayerProblemState.STATUS_DRAW;
		} else if (isWinState(true)) {
			return ITwoPlayerProblemState.STATUS_PLAYER1_WON;
		} else if (isWinState(false)) {
			return ITwoPlayerProblemState.STATUS_PLAYER2_WON;
		} else if (nextPlayerFlag) {
			return ITwoPlayerProblemState.STATUS_PLAYER1_NEXT;
		} else {
			return ITwoPlayerProblemState.STATUS_PLAYER2_NEXT;
		}
	}

	/**
	 * Returns true if this is a state in which player isMax wins
	 * @param isMax true if this is player 1, false if player 2
	 * @return true if this is a state in which player isMax wins
	 */
	protected abstract boolean isWinState(boolean isMax);

	/**
	 * Returns true if this is a state where players draw
	 * @return true if this is a state where players draw
	 */
	protected abstract boolean isDrawState();

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#isCutoffState(int, boolean)
	 */
	public boolean isCutoffState(int depth, boolean isMax)
	{
		return checkGoalState();
	}

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#getMaxUtility()
	 */
	public float getMaxUtility()
	{
		return 1.0f;
	}

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#getMinUtility()
	 */
	public float getMinUtility()
	{
		return -1.0f;
	}

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#getNextPlayerFlag()
	 */
	public boolean getNextPlayerFlag()
	{
		return nextPlayerFlag;
	}

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#checkGoalState()
	 */
	@Override
	public boolean checkGoalState()
	{
		if (isWinState(true) || isWinState(false) || isDrawState()) {
			return true;
		}
		return false;
	}

	/**
	 * @see kdo.domain.ITwoPlayerProblemState#getTwoPlayerUtility(boolean)
	 */
	public float getTwoPlayerUtility(boolean isMax)
	{
		if (isWinState(isMax)) {
			return getMaxUtility();
		} else if (isWinState(!isMax)) {
			return getMinUtility();
		}
		return 0.0f;
	}
}
