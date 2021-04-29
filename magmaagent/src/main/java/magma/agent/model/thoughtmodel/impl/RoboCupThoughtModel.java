/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.communication.action.IAction;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.ITruthValue;
import hso.autonomy.agent.model.thoughtmodel.impl.ThoughtModel;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.logging.PropertyMap;
import hso.autonomy.util.misc.FuzzyCompare;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import magma.agent.IHumanoidJoints;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.thoughtmodel.IKickPositionProfiler;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.KickPositionEstimation;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.IRoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.OpponentProfiler;
import magma.agent.model.thoughtmodel.strategy.impl.roles.DummyRole;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.StrategyConfigurationHelper;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPassModeConstants;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel.BallPassing;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.util.roboviz.RoboVizDraw;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class RoboCupThoughtModel extends ThoughtModel implements IRoboCupThoughtModel
{
	/** Maximum time we believe we know where the ball is. In seconds */
	private static final float LAST_SEEN_THRESHOLD = 3.0f;

	/** the indexical functional object calculator. */
	private transient IFOCalculator ifoCalculator;

	/** list of all obstacles to avoid sorted by the distance to me. */
	protected List<IVisibleObject> obstacles;

	/** list of all team-mates sorted by the distance to the ball. */
	private List<IPlayer> teamMateAtBallList;

	/** list of all own players sorted by the distance to the goal. */
	private List<IPlayer> ownPlayersAtOwnGoalList;

	/** list of all opponents sorted by the distance to the ball. */
	private List<IPlayer> opponentsAtBallList;

	/** list of all players sorted by the distance to the ball. */
	private List<IPlayer> playersAtBallList;

	/** list of all players sorted by the distance to me. */
	private List<IPlayer> playersAtMeList;

	/** list of all opponents sorted by the distance to me. */
	private List<IPlayer> opponentsAtMeList;

	protected transient IKickPositionProfiler kickPositionProfiler;

	protected transient OpponentProfiler opponentProfiler;

	protected transient IRoleManager roleManager;

	/** The current role of the agent. */
	protected transient IRole role;

	protected transient final RoboVizDraw roboVizDraw;

	protected PropertyMap properties;

	private boolean inSoccerPosition = false;

	private IPlayer playerThatActivatedPassMode;

	public RoboCupThoughtModel(IAgentModel agentModel, IRoboCupWorldModel worldModel, final RoboVizDraw roboVizDraw)
	{
		super(agentModel, worldModel);
		this.roboVizDraw = roboVizDraw;
		this.properties = new PropertyMap();

		ifoCalculator = new IFOCalculator(worldModel);
		kickPositionProfiler = null;
		opponentProfiler = new OpponentProfiler();

		roleManager = null;
		role = null;

		// functional Object list
		obstacles = new ArrayList<>(0);
		teamMateAtBallList = new ArrayList<>(0);
		ownPlayersAtOwnGoalList = new ArrayList<>(0);
		opponentsAtBallList = new ArrayList<>(0);
		playersAtBallList = new ArrayList<>(0);
		playersAtMeList = new ArrayList<>(0);
		opponentsAtMeList = new ArrayList<>(0);
	}

	@Override
	public IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) super.getAgentModel();
	}

	@Override
	public IRoboCupWorldModel getWorldModel()
	{
		return (IRoboCupWorldModel) super.getWorldModel();
	}

	@Override
	public RoboVizDraw getRoboVizDraw()
	{
		return roboVizDraw;
	}

	@Override
	public PropertyMap getProperties()
	{
		return properties;
	}

	@Override
	public boolean update(IPerception perception)
	{
		if (properties != null) {
			properties.update();
		}

		boolean result = false;
		if (perception != null) {
			// Trigger agent model to update all internal sensor values
			result = getAgentModel().update(perception);

			// Trigger world model to process vision information
			result = getWorldModel().update(perception) || result;

			// update all truth values with state
			for (ITruthValue value : truthValues.values()) {
				value.update(this);
			}
		}

		updatePlayerThatActivatedPassMode();

		// Recalculate indexical functional objects
		calculateIndexicalFunctionalObjects();

		// recalculate kick directions
		refreshKickProfiler();

		opponentProfiler.determineOpponentTeam(opponentsAtBallList);

		updateTeamStrategy();

		// determine new roles
		determineRole();

		roboVizDraw.setPlaySide(getWorldModel().getPlaySide());

		return result;
	}

	private void updatePlayerThatActivatedPassMode()
	{
		if (getWorldModel().getEnteredPassModeTime() == getWorldModel().getGameTime()) {
			// we assume our closest player activated pass mode
			playerThatActivatedPassMode = getClosestOwnPlayerAtBall();
		}
	}

	protected void refreshKickProfiler()
	{
		kickPositionProfiler.resetProfile();
	}

	protected void determineRole()
	{
		role = roleManager.determineRole(getClosestOwnPlayerAtBall(), getWorldModel().getThisPlayer().getID());
	}

	protected void updateTeamStrategy()
	{
		String strategyName = opponentProfiler.getTeamStrategyName();
		if (!strategyName.equals(roleManager.getStrategy().getName())) {
			roleManager.setStrategy(StrategyConfigurationHelper.STRATEGIES.get(strategyName).create(this));
		}
	}

	@Override
	public void mapStateToAction(IAction action, boolean remoteControlled)
	{
		IRoboCupAgentModel agentModel = getAgentModel();
		agentModel.reflectTargetStateToAction(action);
	}

	/**
	 * Calculates the lists of players sorted by different criteria.
	 */
	protected void calculateIndexicalFunctionalObjects()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		List<IPlayer> visiblePlayers = worldModel.getVisiblePlayers();
		if (worldModel.isBallInCriticalArea()) {
			teamMateAtBallList = ifoCalculator.getTeammatesAtBallWithGoalie(visiblePlayers);
		} else {
			teamMateAtBallList = ifoCalculator.getTeammatesAtBall(visiblePlayers);
		}
		List<IPlayer> allPlayers = new ArrayList<>(visiblePlayers);
		allPlayers.add(worldModel.getThisPlayer());
		ownPlayersAtOwnGoalList = ifoCalculator.getOwnPlayersAtOwnGoal(allPlayers);
		opponentsAtBallList = ifoCalculator.getOpponentsAtBall(visiblePlayers);
		playersAtBallList = ifoCalculator.getPlayersAtBall(visiblePlayers);
		playersAtMeList = ifoCalculator.getPlayersAtMe(visiblePlayers);
		opponentsAtMeList = ifoCalculator.getOpponentsAtMe(playersAtMeList);
		obstacles = ifoCalculator.getObstacles(visiblePlayers, worldModel.getBall());
	}

	// --------------------------------------------------
	// IFOs
	@Override
	public List<IVisibleObject> getObstacles()
	{
		return obstacles;
	}

	@Override
	public List<IPlayer> getOpponentsAtMeList()
	{
		return opponentsAtMeList;
	}

	@Override
	public List<IPlayer> getPlayersAtMeList()
	{
		return playersAtMeList;
	}

	@Override
	public List<IPlayer> getTeammatesAtBall()
	{
		return teamMateAtBallList;
	}

	@Override
	public IPlayer getTeammateAtBall()
	{
		if (teamMateAtBallList.isEmpty()) {
			return null;
		}
		return teamMateAtBallList.get(0);
	}

	@Override
	public List<IPlayer> getOpponentsAtBallList()
	{
		return opponentsAtBallList;
	}

	@Override
	public IPlayer getOpponentAtBall()
	{
		if (opponentsAtBallList.isEmpty()) {
			return null;
		}
		return opponentsAtBallList.get(0);
	}

	@Override
	public List<IPlayer> getPlayersAtBallList()
	{
		return playersAtBallList;
	}

	@Override
	public IPlayer getClosestOwnPlayerAtBall()
	{
		List<IPlayer> playersAtBall = getTeammatesAtBall();
		List<IPlayer> newSort = new ArrayList<>(playersAtBall);
		// we add ourselves to have the same calculation for all
		newSort.add(getWorldModel().getThisPlayer());

		return Collections.min(newSort, new DistanceToBallComparator(getWorldModel()));
	}

	@Override
	public boolean isClosestToBall()
	{
		return getWorldModel().getThisPlayer() == getClosestOwnPlayerAtBall();
	}

	@Override
	public boolean isClosestToOwnGoal()
	{
		return getWorldModel().getThisPlayer() == ownPlayersAtOwnGoalList.get(0);
	}

	@Override
	public boolean isOpponentNearBall()
	{
		if (getOpponentAtBall() == null) {
			return false;
		}

		Vector3D ballPosition = getWorldModel().getBall().getPosition();
		return getOpponentAtBall().getDistanceToXY(ballPosition) < 1;
	}

	@Override
	public boolean shouldActivatePassMode()
	{
		if (!isClosestToBall() || getOpponentAtBall() == null) {
			return false;
		}

		double distanceToOpponentGoal =
				getWorldModel().getThisPlayer().getDistanceToXY(getWorldModel().getOtherGoalPosition());
		if (distanceToOpponentGoal < KickPositionProfilerGoal.GOAL_KICK_DISTANCE + 0.5) {
			return false;
		}

		double opponentDistanceToBall = getOpponentAtBall().getDistanceToXY(getWorldModel().getBall());
		return opponentDistanceToBall >= IPassModeConstants.AREA_RADIUS &&
				opponentDistanceToBall <= IPassModeConstants.AREA_RADIUS + 0.5;
	}

	@Override
	public boolean isBallVisible()
	{
		return getWorldModel().getBall().getAge(getWorldModel().getGlobalTime()) < LAST_SEEN_THRESHOLD;
	}

	@Override
	public boolean isInSoccerPosition()
	{
		if (getAxis(IHumanoidJoints.LHipPitch) > 65 || getAxis(IHumanoidJoints.RHipPitch) > 65) {
			inSoccerPosition = false; // sitting position
		}

		if (axesEqual(IHumanoidJoints.LKneePitch, IHumanoidJoints.RKneePitch, 0, 0.5f)) {
			inSoccerPosition = false;
		}

		IRoboCupAgentModel agentModel = getAgentModel();
		if (!axesEqual(IHumanoidJoints.LKneePitch, IHumanoidJoints.RKneePitch, agentModel.getSoccerPositionKneeAngle(),
					5f)) {
			return inSoccerPosition;
		}

		float desiredHipAngle = agentModel.getSoccerPositionHipAngle();
		if (desiredHipAngle < -180) {
			inSoccerPosition = true;
		} else if (axesEqual(IHumanoidJoints.LHipRoll, IHumanoidJoints.RHipRoll, desiredHipAngle, 3f)) {
			inSoccerPosition = true;
		}

		return inSoccerPosition;
	}

	@Override
	public boolean isSitting()
	{
		if (getAxis(IHumanoidJoints.LHipPitch) > 65 && getAxis(IHumanoidJoints.RHipPitch) > 65 &&
				getAxis(IHumanoidJoints.LKneePitch) > -30 && getAxis(IHumanoidJoints.RKneePitch) > -30 &&
				getWorldModel().getThisPlayer().getUpVectorZ() > 0.5) {
			return true;
		}
		return false;
	}

	private boolean axesEqual(String leftJoint, String rightJoint, float desired, float range)
	{
		return FuzzyCompare.eq(getAxis(leftJoint), desired, range) &&
				FuzzyCompare.eq(getAxis(rightJoint), desired, range);
	}

	private float getAxis(String jointName)
	{
		return getAgentModel().getHJ(jointName).getAngle();
	}

	// --------------------------------------------------
	// Kick profile and estimation
	@Override
	public Angle getIntendedKickDirection()
	{
		return kickPositionProfiler.getIntendedKickDirection();
	}

	@Override
	public double getIntendedKickDistance()
	{
		return kickPositionProfiler.getIntendedKickDistance();
	}

	@Override
	public SortedSet<KickPositionEstimation> getKickOptions()
	{
		return kickPositionProfiler.getEvaluatedPositions();
	}

	@Override
	public void setKickPositionProfiler(IKickPositionProfiler kickPositionProfiler)
	{
		this.kickPositionProfiler = kickPositionProfiler;
	}

	// --------------------------------------------------
	// Strategy and Role management

	@Override
	public IRoleManager getRoleManager()
	{
		return roleManager;
	}

	@Override
	public IRole getRole()
	{
		return role;
	}

	@Override
	public void setRoleManager(IRoleManager manager)
	{
		roleManager = manager;
		this.role = DummyRole.INSTANCE;
	}

	@Override
	public boolean shouldBeam()
	{
		if (getWorldModel().getGameState().isBeamingAllowed()) {
			double distance = getWorldModel().getThisPlayer().getDistanceToXY(getHomePose().getPosition());
			if (distance > 0.3) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldReplaceGoalie()
	{
		final int goalieAwayDistance = 3;

		Optional<IPlayer> goalie = ownPlayersAtOwnGoalList.stream().filter(IPlayer::isGoalie).findFirst();

		boolean goalieExistsAndAwayFromGoal =
				goalie.filter(player
							  -> player.getDistanceToXY(getWorldModel().getOwnGoalPosition()) > goalieAwayDistance)
						.isPresent();

		return goalieExistsAndAwayFromGoal && isClosestToOwnGoal() &&
				// don't walk away from the ball if we are about to attack and are not in the critical area
				(!isClosestToBall() || getWorldModel().isInCriticalArea(getWorldModel().getThisPlayer().getPosition()));
	}

	@Override
	public Pose2D getHomePose()
	{
		return roleManager.getStrategy().getFormation().getPlayerPose(getWorldModel().getThisPlayer().getID());
	}

	// --------------------------------------------------
	// Other stuff
	/**
	 * @return true if the ball is in a position in which we should dribble into goal
	 */
	@Override
	public boolean ballIsInGoalDribblingPosition(double maxXDistanceFromGoal)
	{
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		float goalHalfWidth = getWorldModel().goalHalfWidth() - 0.05f;

		if (ballPos.getX() > (getWorldModel().fieldHalfLength() - maxXDistanceFromGoal) &&
				ballPos.getY() < goalHalfWidth && ballPos.getY() > -goalHalfWidth) {
			return true;
		}

		return false;
	}

	/**
	 * @param limitAngle the maximum abs horizontal angle (in deg) we can face
	 * @return true if we are in a position in which we should dribble into goal
	 */
	@Override
	public boolean canDribbleIntoGoal(double limitAngle)
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		float goalHalfWidth = getWorldModel().goalHalfWidth() - 0.05f;

		double viewDirection = thisPlayer.getHorizontalAngle().degrees();

		// Ball is very close or inside the opponent's goal
		// Therefore just allow dribblings towards the goal
		if (ballPos.getX() > (getWorldModel().fieldHalfLength() - 0.3)) {
			double limit = limitAngle - (Math.abs(ballPos.getY()) / goalHalfWidth) * limitAngle;

			if ((ballPos.getY() < 0 && limitAngle > viewDirection && viewDirection > -limit) ||
					(ballPos.getY() >= 0 && limit > viewDirection && viewDirection > -limitAngle)) {
				// 3. Dribble 10cm forwards with full speed
				return true;
			}
		} else {
			IMoveableObject ball = getWorldModel().getBall();
			Vector3D otherGoalPos = getWorldModel().getOtherGoalPosition();

			double ballToOtherUpperPost =
					ball.getDirectionTo(otherGoalPos.add(new Vector3D(0, goalHalfWidth, 0))).degrees();
			double ballToOtherLowerPost =
					ball.getDirectionTo(otherGoalPos.subtract(new Vector3D(0, goalHalfWidth, 0))).degrees();

			if (ballToOtherUpperPost > viewDirection && viewDirection > ballToOtherLowerPost) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculates target position for dribbling, based on our current position
	 * and orientation.
	 *
	 * @return the target pose for dribbling (usually 10 cm in front of us)
	 */
	@Override
	public Pose2D getDribblePose()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		Angle viewDirection = thisPlayer.getHorizontalAngle();

		Vector3D targetPos = thisPlayer.getPosition().add(new Vector3D(0.1, new Vector3D(viewDirection.radians(), 0)));
		return new Pose2D(targetPos, viewDirection);
	}

	@Override
	public Vector3D getDesiredKickPosition()
	{
		return kickPositionProfiler.getIntendedKickPosition();
	}

	@Override
	public Vector3D getKickOffTargetPosition()
	{
		return opponentProfiler.getKickOffTargetPosition();
	}

	@Override
	public boolean isBallPassingShort()
	{
		IBall ball = getWorldModel().getBall();
		if (ball.isMoving()) {
			Vector3D futureBallPosition = ball.getFuturePosition(200);
			Vector3D otherGoalPosition = getWorldModel().getOtherGoalPosition();
			if (Vector3D.distance(futureBallPosition, otherGoalPosition) <
					Vector3D.distance(ball.getPosition(), otherGoalPosition)) {
				// ball is moving towards other goal
				return false;
			}

			BallPassing ballIsPassing = getWorldModel().ballIsPassing(futureBallPosition);
			switch (ballIsPassing) {
			case SHORT_LEFT:
			case SHORT_RIGHT:
				return true;
			case CENTER:
			case FAR_LEFT:
			case FAR_RIGHT:
			case UNREACHABLE:
			default:
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean isAllowedToScore()
	{
		float leftPassModeTime = getWorldModel().getLeftPassModeTime();
		float gameTime = getWorldModel().getGameTime();

		return leftPassModeTime == 0 || gameTime - leftPassModeTime > IPassModeConstants.SCORE_WAIT_TIME ||
				!getWorldModel().getThisPlayer().equals(playerThatActivatedPassMode);
	}
}
