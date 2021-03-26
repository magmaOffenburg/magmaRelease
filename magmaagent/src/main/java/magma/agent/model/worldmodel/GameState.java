/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import java.util.Arrays;
import magma.common.spark.PlayMode;
import magma.common.spark.PlaySide;

/**
 * Game States enumeration. This enum represents the states of the soccer game
 * of our point of view. This means we provide a separation between OWN_ and
 * OPPONENT_ states where necessary.
 *
 * @author Stefan Glaser
 */
public enum GameState {
	/** The game has not started yet */
	BEFORE_KICK_OFF,

	/** The ball is in the center, and our team is allowed to kick it first */
	OWN_KICK_OFF,

	/** The ball is in the center, and the opponent team is allowed to kick it */
	OPPONENT_KICK_OFF,

	/** The game is processing normally, no special rules are in place */
	PLAY_ON,

	/**
	 * The ball left the playfield, our team is allowed to kick it back into the
	 * field
	 */
	OWN_KICK_IN,

	/**
	 * The ball left the playfield, the opponent team is allowed to kick it back
	 * into the field
	 */
	OPPONENT_KICK_IN,

	/**
	 * The ball left the playfield, our team is allowed to kick it back into the
	 * field from a corner
	 */
	OWN_CORNER_KICK,

	/**
	 * The ball left the playfield, the opponent team is allowed to kick it back
	 * into the field from a corner
	 */
	OPPONENT_CORNER_KICK,

	/**
	 * The ball left the playfield while it was near the own goal, and our team
	 * is allowed to kick it first
	 */
	OWN_GOAL_KICK,

	/**
	 * The ball left the playfield while it was near the opponent goal, and the
	 * opponent team is allowed to kick it first
	 */
	OPPONENT_GOAL_KICK,

	/**
	 * The opponent team violated the offside rule and our team is allowed to
	 * kick the ball first
	 */
	OWN_OFFSIDE,

	/**
	 * Our team violated the offside rule and the opponent team is allowed to
	 * kick the ball first
	 */
	OPPONENT_OFFSIDE,

	/** The game is over */
	GAME_OVER,

	/** A goal was counted for our team */
	OWN_GOAL,

	/** A goal was counted for the opponent team */
	OPPONENT_GOAL,

	/**
	 * Our team got a free kick (probably because the referee decided to do so)
	 * and may kick the ball first
	 */
	OWN_FREE_KICK,

	/**
	 * The opponent right team got a free kick (probably because the referee
	 * decided to do so) and may kick the ball first
	 */
	OPPONENT_FREE_KICK,

	OWN_DIRECT_FREE_KICK,

	OPPONENT_DIRECT_FREE_KICK,

	OWN_PASS,

	OPPONENT_PASS,

	/** No gameState or an unknown one (should not be used!) */
	NONE,

	/** humanoid game states */
	FREEZE,

	OWN_PENALTY_KICK,

	OPPONENT_PENALTY_KICK,

	OWN_PENALTY_SHOOT,

	OPPONENT_PENALTY_SHOOT;

	private static GameState checkSide(PlaySide playSide, GameState leftState, GameState rightState)
	{
		return playSide == PlaySide.LEFT ? leftState : rightState;
	}

	/**
	 * Determine the current game state from play mode and field side
	 *
	 * @param playMode Current play mode
	 * @param playSide Current field side the team is playing on
	 * @return Current game state
	 */
	public static GameState determineGameState(PlayMode playMode, PlaySide playSide)
	{
		switch (playMode) {
		case BEFORE_KICK_OFF:
			return GameState.BEFORE_KICK_OFF;

		case PLAY_ON:
			return GameState.PLAY_ON;

		case GAME_OVER:
			return GameState.GAME_OVER;

		case KICK_OFF_LEFT:
			return checkSide(playSide, OWN_KICK_OFF, OPPONENT_KICK_OFF);

		case KICK_OFF_RIGHT:
			return checkSide(playSide, OPPONENT_KICK_OFF, OWN_KICK_OFF);

		case KICK_IN_LEFT:
			return checkSide(playSide, OWN_KICK_IN, OPPONENT_KICK_IN);

		case KICK_IN_RIGHT:
			return checkSide(playSide, OPPONENT_KICK_IN, OWN_KICK_IN);

		case CORNER_KICK_LEFT:
			return checkSide(playSide, OWN_CORNER_KICK, OPPONENT_CORNER_KICK);

		case CORNER_KICK_RIGHT:
			return checkSide(playSide, OPPONENT_CORNER_KICK, OWN_CORNER_KICK);

		case FREE_KICK_LEFT:
			return checkSide(playSide, OWN_FREE_KICK, OPPONENT_FREE_KICK);

		case FREE_KICK_RIGHT:
			return checkSide(playSide, OPPONENT_FREE_KICK, OWN_FREE_KICK);

		case DIRECT_FREE_KICK_LEFT:
			return checkSide(playSide, OWN_DIRECT_FREE_KICK, OPPONENT_DIRECT_FREE_KICK);

		case DIRECT_FREE_KICK_RIGHT:
			return checkSide(playSide, OPPONENT_DIRECT_FREE_KICK, OWN_DIRECT_FREE_KICK);

		case GOAL_KICK_LEFT:
			return checkSide(playSide, OWN_GOAL_KICK, OPPONENT_GOAL_KICK);

		case GOAL_KICK_RIGHT:
			return checkSide(playSide, OPPONENT_GOAL_KICK, OWN_GOAL_KICK);

		case OFFSIDE_LEFT:
			return checkSide(playSide, OWN_OFFSIDE, OPPONENT_OFFSIDE);

		case OFFSIDE_RIGHT:
			return checkSide(playSide, OPPONENT_OFFSIDE, OWN_OFFSIDE);

		case GOAL_LEFT:
			return checkSide(playSide, OWN_GOAL, OPPONENT_GOAL);

		case GOAL_RIGHT:
			return checkSide(playSide, OPPONENT_GOAL, OWN_GOAL);

		case FREEZE:
			return GameState.FREEZE;

		case PENALTY_KICK_LEFT:
			return checkSide(playSide, OWN_PENALTY_KICK, OPPONENT_PENALTY_KICK);

		case PENALTY_KICK_RIGHT:
			return checkSide(playSide, OPPONENT_PENALTY_KICK, OWN_PENALTY_KICK);

		case PENALTY_SHOOT_LEFT:
			return checkSide(playSide, OWN_PENALTY_SHOOT, OPPONENT_PENALTY_SHOOT);

		case PENALTY_SHOOT_RIGHT:
			return checkSide(playSide, OPPONENT_PENALTY_SHOOT, OWN_PENALTY_SHOOT);

		case PASS_LEFT:
			return checkSide(playSide, OWN_PASS, OPPONENT_PASS);

		case PASS_RIGHT:
			return checkSide(playSide, OPPONENT_PASS, OWN_PASS);

		default:
			return GameState.NONE;
		}
	}

	public boolean isAmongst(GameState... states)
	{
		return Arrays.stream(states).anyMatch(s -> this == s);
	}

	public boolean isGameRunning()
	{
		return this == PLAY_ON || this == OWN_KICK_OFF || this == OPPONENT_KICK_OFF || this == OWN_GOAL_KICK ||
				this == OPPONENT_GOAL_KICK || this == OWN_FREE_KICK || this == OPPONENT_FREE_KICK ||
				this == OWN_DIRECT_FREE_KICK || this == OPPONENT_DIRECT_FREE_KICK || this == OWN_KICK_IN ||
				this == OPPONENT_KICK_IN || this == OWN_CORNER_KICK || this == OPPONENT_CORNER_KICK ||
				this == OWN_PASS || this == OPPONENT_PASS || this == OWN_PENALTY_KICK ||
				this == OPPONENT_PENALTY_KICK || this == OWN_PENALTY_SHOOT || this == OPPONENT_PENALTY_SHOOT;
	}

	public boolean isOwnKick()
	{
		return this == OWN_CORNER_KICK || this == OWN_FREE_KICK || this == OWN_DIRECT_FREE_KICK ||
				this == OWN_GOAL_KICK || this == OWN_KICK_IN || this == OWN_KICK_OFF || this == OWN_PASS ||
				this == OWN_PENALTY_KICK || this == OWN_PENALTY_SHOOT;
	}

	public boolean isOpponentKick()
	{
		return this == OPPONENT_CORNER_KICK || this == OPPONENT_FREE_KICK || this == OPPONENT_DIRECT_FREE_KICK ||
				this == OPPONENT_GOAL_KICK || this == OPPONENT_KICK_IN || this == OPPONENT_KICK_OFF ||
				this == OPPONENT_PASS || this == OPPONENT_PENALTY_KICK || this == OPPONENT_PENALTY_SHOOT;
	}

	public boolean isBeamingAllowed()
	{
		return this == OWN_GOAL || this == OPPONENT_GOAL || this == BEFORE_KICK_OFF;
	}
}
