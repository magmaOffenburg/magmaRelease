/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.util.List;
import magma.common.spark.PlayMode;
import magma.common.spark.PlaySide;
import magma.common.spark.TeamColor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents all environment data available to the agent
 */
public interface IRoboCupWorldModel extends IWorldModel {
	enum BallPassing
	{
		CENTER,
		SHORT_LEFT,
		SHORT_RIGHT,
		FAR_LEFT,
		FAR_RIGHT,
		UNREACHABLE
	}

	/**
	 * Represents the current task to be executed by our team in a penalty shootout
	 */
	enum PenaltyState
	{
		NONE,
		KICK,
		HOLD,
	}

	/**
	 * Get the reference to the soccer field map.
	 *
	 * @return The soccer field map
	 */
	IRCSoccerField getMap();

	/**
	 * Get a reference to the ball object
	 *
	 * @return The ball object
	 */
	IBall getBall();

	/**
	 * @return the difference of the ball radius from 2018 to the currently used size
	 */
	float getBallRadiusDifference();

	/**
	 * @return the goal post landmarks used as obstacles (do not exactly match
	 *         goal posts)
	 */
	List<IVisibleObject> getGoalPostObstacles();

	/**
	 * Get a list of all visible players
	 *
	 * @return An unmodifiable list of the currently visible players
	 */
	List<IPlayer> getVisiblePlayers();

	/**
	 * Get a reference to the player object of this player
	 *
	 * @return This players information of position etc
	 */
	IThisPlayer getThisPlayer();

	/**
	 * Get the running time of the current game
	 *
	 * @return the time the game is running now
	 */
	float getGameTime();

	TeamColor getTeamColor();

	PlayMode getPlaymode();

	PlaySide getPlaySide();

	/**
	 * Get the current play mode
	 * @return the current mode of the game as String
	 */
	GameState getGameState();

	float getEnteredPassModeTime();

	float getLeftPassModeTime();

	/**
	 * Get the number of goals our own team scored
	 *
	 * @return number of goals scored by us
	 */
	int getGoalsWeScored();

	/**
	 * Get the number of goals the opponent team scored
	 *
	 * @return number of goals scored by opponent
	 */
	int getGoalsTheyScored();

	/**
	 * @return the serverVersion used for playing
	 */
	int getServerVersion();

	/**
	 * @param id the player's number (1-11)
	 * @param ownTeam true if we want the player of our own team
	 * @return the referenced player, null if not in list
	 */
	IPlayer getVisiblePlayer(int id, boolean ownTeam);

	/**
	 * Checks whether the ball is dangerously close to the own goal. If this is
	 * the case, the goalie runs to the ball.
	 */
	boolean isBallInCriticalArea();

	boolean isInCriticalArea(Vector3D position);

	/**
	 * Checks if the ball is passing through us, slightly next to us or reachable with arms
	 * @param futureBallPosition the position where we expect the ball to be
	 * @return different conditions how the ball may pass us
	 */
	BallPassing ballIsPassing(Vector3D futureBallPosition);

	/**
	 * Checks if a line between the two passed points intersects the goal line between the goal posts
	 * @param point1 first point of the line to check
	 * @param point2 second point of the line to check
	 * @param offset distance to stay away from goal post
	 * @return the intersection point, null if they do not intersect
	 */
	Vector2D goalLineIntersection(Vector2D point1, Vector2D point2, double offset);

	/** @see IRCSoccerField#fieldHalfLength() */
	float fieldHalfLength();

	/** @see IRCSoccerField#fieldHalfWidth() */
	float fieldHalfWidth();

	/** @see IRCSoccerField#penaltyHalfLength() */
	float penaltyHalfLength();

	/** @see IRCSoccerField#penaltyWidth() */
	float penaltyWidth();

	/** @see IRCSoccerField#goalHalfWidth() */
	float goalHalfWidth();

	/** @see IRCSoccerField#goalHeight() */
	float goalHeight();

	/** @see IRCSoccerField#goalDepth() */
	float goalDepth();

	/** @see IRCSoccerField#centerCircleRadius() */
	float centerCircleRadius();

	/** @see IRCSoccerField#getOwnGoalPosition() */
	Vector3D getOwnGoalPosition();

	/** @see IRCSoccerField#getOtherGoalPosition() */
	Vector3D getOtherGoalPosition();

	boolean isPenalty();

	PenaltyState getPenaltyState();
}