/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.common.humanoid;

import java.util.Arrays;

/**
 * Enum providing different game phases (the primary game state of the humanoid league).
 *
 * @author Stefan Glaser
 */
public enum GamePhase {
	/** Initial game phase. */
	INITIAL,

	/** Ready game phase. The ready game phase can be used by the robot to prepare internally for the current task. */
	READY,

	/**
	 * Set game phase. This game phase corresponds to a setup phase. It is used by the robots to get to their desired
	 * positions for the current task.
	 */
	SET,

	/**
	 * Playing game phase. The playing game phase signals that the game is running and the current task should be
	 * actively performed.
	 */
	PLAYING,

	/** Finished game phase. THe finished game phase signals the end of the current task. */
	FINISHED;

	public static GamePhase fromId(int id)
	{
		if (id >= 0 && id < values().length) {
			return values()[id];
		}

		return INITIAL;
	}

	/**
	 * Check if this GamePhase is amongst the given list of game phases.
	 *
	 * @param phases the list of game phases to compare against
	 * @return true, if this game state is among the given game phases, false otherwise
	 */
	public boolean isAmongst(GamePhase... phases)
	{
		return Arrays.stream(phases).anyMatch(p -> this == p);
	}

	/**
	 * Check if this game phase is a positioning game phase.
	 *
	 * @return true, if this game phase is a positioning phase, false otherwise
	 */
	public boolean isPositioningPhase()
	{
		return this == READY;
	}

	/**
	 * Check if this game phase is a playing game phase.
	 *
	 * @return true, if this game phase is a playing phase, false otherwise
	 */
	public boolean isPlayingPhase()
	{
		return this == PLAYING;
	}
}
