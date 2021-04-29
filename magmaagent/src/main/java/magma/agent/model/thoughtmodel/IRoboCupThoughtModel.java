/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel;

import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.logging.PropertyMap;
import java.util.List;
import java.util.SortedSet;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.util.roboviz.RoboVizDraw;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents all information related to thoughts of the agent.<br>
 * While the {@link hso.autonomy.agent.model.worldmodel.IWorldModel} and {@link IAgentModel}
 * represent the measured states based on sensor information, the IThoughtModel
 * is meant to represent a higher level of information abstraction. Furthermore,
 * it is the central model to store all information that comes from inside the
 * agent and cannot be measured.<br>
 * To give a simple example: <i>IFOCalculator</i>:<br>
 * The location information for each player is a measured value and contained in
 * the {@link hso.autonomy.agent.model.worldmodel.IWorldModel}. The question which player is
 * closest to the ball can be calculated based on this information but
 * represents a thought which is not directly related to a sensor value. This
 * "thought" (which player is closest to the ball) can be integrated and
 * buffered in the IThoughtModel, which improves reusability of common
 * calculations and therefore reduces runtime.
 *
 * @author Stefan Glaser
 */
public interface IRoboCupThoughtModel extends IThoughtModel {
	@Override
	IRoboCupAgentModel getAgentModel();

	@Override
	IRoboCupWorldModel getWorldModel();

	RoboVizDraw getRoboVizDraw();

	PropertyMap getProperties();

	Angle getIntendedKickDirection();

	double getIntendedKickDistance();

	SortedSet<KickPositionEstimation> getKickOptions();

	Vector3D getDesiredKickPosition();

	Vector3D getKickOffTargetPosition();

	void setKickPositionProfiler(IKickPositionProfiler kickPositionProfiler);

	void setRoleManager(IRoleManager roleManager);

	IRoleManager getRoleManager();

	/**
	 * @return the current role of the agent
	 */
	IRole getRole();

	boolean shouldBeam();

	boolean shouldReplaceGoalie();

	Pose2D getHomePose();

	/**
	 * @return the opponent that is closest to ball
	 */
	IPlayer getOpponentAtBall();

	/**
	 * Get the team-mate which is closest to the ball
	 *
	 * @return the team-mate that is closest to the ball, null if we see no
	 *         team-mates
	 */
	IPlayer getTeammateAtBall();

	/**
	 * Get the player of the own team (including myself) which is closest to the
	 * ball
	 *
	 * @return the own player that is closest to the ball, ourself if we see no
	 *         team-mates
	 */
	IPlayer getClosestOwnPlayerAtBall();

	boolean isClosestToBall();

	boolean isClosestToOwnGoal();

	boolean isOpponentNearBall();

	boolean shouldActivatePassMode();

	boolean isBallVisible();

	boolean isInSoccerPosition();

	/**
	 * Has to be in thought model since it requires joint and up vector information
	 * @return true if this player is upright with hips bowed
	 */
	boolean isSitting();

	/**
	 * @return the obstacles to avoid
	 */
	List<IVisibleObject> getObstacles();

	/**
	 * Get a list of all players, sorted by distance from the own position
	 *
	 * @return a list of all players sorted by the distance to me
	 */
	List<IPlayer> getPlayersAtMeList();

	/**
	 * Get a list of all opponents, sorted by distance from the own position
	 *
	 * @return a list of all opponents sorted by the distance to me
	 */
	List<IPlayer> getOpponentsAtMeList();

	/**
	 * Get a list of all opponents, sorted by distance from the ball position
	 *
	 * @return a list of all opponents sorted by the distance to ball
	 */
	List<IPlayer> getOpponentsAtBallList();

	/**
	 * Get a list of all team-mates, sorted by distance from the ball position
	 *
	 * @return list of team-mates
	 */
	List<IPlayer> getTeammatesAtBall();

	/**
	 * Get a list of all players, sorted by distance from the ball position
	 *
	 * @return List of players
	 */
	List<IPlayer> getPlayersAtBallList();

	/**
	 * Checks if the ball is in a dribbling position in front of the goal
	 * @param maxXDistanceFromGoal at which x-distance the ball may be from goal
	 * @return true if the ball is in a position in which we should dribble into goal
	 */
	boolean ballIsInGoalDribblingPosition(double maxXDistanceFromGoal);

	/**
	 * @param limitAngle the maximum abs horizontal angle (in deg) we can face
	 * @return true if we are in a position in which we should dribble into goal
	 */
	boolean canDribbleIntoGoal(double limitAngle);

	/**
	 * Calculates target position for dribbling, based on our current position
	 * and orientation.
	 *
	 * @return the target pose for dribbling (usually 10 cm in front of us)
	 */
	Pose2D getDribblePose();

	/**
	 * @return true if the ball is passing in keep short distance from us
	 */
	boolean isBallPassingShort();

	boolean isAllowedToScore();
}
