/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import hso.autonomy.agent.communication.perception.IGlobalPosePerceptor;
import hso.autonomy.agent.communication.perception.ILinePerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.communication.perception.ITimerPerceptor;
import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import hso.autonomy.agent.model.agentmodel.IBodyPart;
import hso.autonomy.agent.model.agentmodel.IGyroRate;
import hso.autonomy.agent.model.worldmodel.IFieldLine;
import hso.autonomy.agent.model.worldmodel.ILandmark;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.agent.model.worldmodel.InformationSource;
import hso.autonomy.agent.model.worldmodel.impl.FieldLine;
import hso.autonomy.agent.model.worldmodel.impl.Landmark;
import hso.autonomy.agent.model.worldmodel.impl.WorldModel;
import hso.autonomy.agent.model.worldmodel.localizer.IFeatureLocalizer;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.agent.model.worldmodel.localizer.ILineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.impl.LineFeatureObservation;
import hso.autonomy.agent.model.worldmodel.localizer.impl.PointFeatureObservation;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import hso.autonomy.util.geometry.positionFilter.IPositionFilter;
import hso.autonomy.util.geometry.positionFilter.LowFrequencyPositionFilter;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.agent.communication.perception.IGameStatePerceptor;
import magma.agent.communication.perception.IHearPerceptor;
import magma.agent.communication.perception.IPlayerPos;
import magma.agent.communication.perception.IRoboCupPerception;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.worldmeta.ILineFeatureConfiguration;
import magma.agent.model.worldmeta.IPointFeatureConfiguration;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import magma.agent.model.worldmeta.impl.RCServerMetaModelV62;
import magma.agent.model.worldmeta.impl.SayMessage;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import magma.common.spark.PlayMode;
import magma.common.spark.PlaySide;
import magma.common.spark.TeamColor;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Container class for all visible objects on the field, for the time and game state.
 */
public class RoboCupWorldModel extends WorldModel implements IRoboCupWorldModel
{
	protected static float TIME_TO_TRUST_HEAR = 0.2f;

	/** the time we keep other players in mind if not seeing them (in seconds) */
	protected static float REMEMBRANCE_TIME = 30;

	/** The soccer field feature map. */
	protected RCSoccerField map;

	/** the ball of the game */
	protected IBall ball;

	/** The position filter for the ball. */
	protected transient IPositionFilter ballPosFilter;

	/** The difference of the ball radius from 2018 to the currently used size */
	protected float ballRadiusDifference;

	/**
	 * A map of all known players over time. As unique key, the team name
	 * appended by the player number is used.
	 */
	protected Map<String, IPlayer> knownPlayers;

	/** the list of all visible players */
	protected List<IPlayer> visiblePlayers;

	/** the time the game is running now (cycles since start) */
	private float gameTime;

	/**
	 * The side which our team is playing. Note: It's important that this value
	 * is initialized with the left side!
	 */
	private PlaySide playSide = PlaySide.LEFT;

	/** color of team. Currently unknown in simspark! */
	private TeamColor teamColor = TeamColor.BLUE;

	/** the playmode the game is running now */
	private PlayMode playmode = PlayMode.BEFORE_KICK_OFF;

	/** the game state, the game is running now */
	protected GameState gameState = GameState.BEFORE_KICK_OFF;

	/** the game state, the game was running previously */
	protected GameState previousGameState;

	private float enteredPassModeTime;

	private float leftPassModeTime;

	/** the global coordinates of the agent */
	protected ThisPlayer thisPlayer;

	/** goals scored by the opponent */
	protected int goalsTheyScored = 0;

	/** goals scored by us */
	protected int goalsWeScored = 0;

	protected IRoboCupWorldMetaModel worldMetaModel;

	/** The version of the soccer server used */
	protected int serverVersion;

	/**
	 * The name of the other team.<br>
	 * <tt>null</tt> as long we haven't seen any other player.
	 */
	protected String otherTeamName;

	protected boolean penalty = false;

	private PenaltyState penaltyState = PenaltyState.NONE;

	/**
	 * Constructor
	 *
	 * @param agentModel Reference to the agent model object
	 * @param localizer the module that calculates the agent's global position
	 * @param worldMetaModel the meta model of the rc server
	 * @param teamname Team name
	 * @param playerNumber Player number
	 */
	public RoboCupWorldModel(IRoboCupAgentModel agentModel, IFeatureLocalizer localizer,
			IRoboCupWorldMetaModel worldMetaModel, String teamname, int playerNumber)
	{
		super(agentModel, localizer);
		this.worldMetaModel = worldMetaModel;
		this.serverVersion = worldMetaModel.getVersion();

		// create objects
		createObjects(worldMetaModel);

		// ballPosFilter = new NoFilter();
		ballPosFilter = new LowFrequencyPositionFilter(5);

		thisPlayer = new ThisPlayer(teamname, playerNumber, agentModel.getCycleTime(), agentModel.getTorsoZUpright());
		thisPlayer.getPositionManager().setDesiredPosition(new PoseSpeed2D(new Pose2D(), Vector2D.ZERO), false);
	}

	/**
	 * Creates all objects. We do not create new objects each time on update, but
	 * update the existing objects
	 */
	private void createObjects(IRoboCupWorldMetaModel metaModel)
	{
		// ball
		ball = new Ball(metaModel.getBallRadius(), metaModel.getBallDecay(), Vector3D.ZERO, IBall.COLLISION_DISTANCE,
				getAgentModel().getCycleTime());
		ballRadiusDifference = metaModel.getBallRadius() - RCServerMetaModelV62.INSTANCE.getBallRadius();

		// landmarks
		if (metaModel.getLandmarks() != null) {
			for (IPointFeatureConfiguration pfc : metaModel.getLandmarks().values()) {
				landmarks.put(pfc.getName(), new Landmark(pfc.getName(), pfc.getType(), pfc.getKnownPosition()));
			}
		}

		// field lines
		if (metaModel.getFieldLines() != null) {
			for (ILineFeatureConfiguration lfc : metaModel.getFieldLines().values()) {
				fieldLines.put(lfc.getName(),
						new FieldLine(lfc.getName(), lfc.getType(), lfc.getKnownPosition1(), lfc.getKnownPosition2()));
			}
		}

		// map
		map = new RCSoccerField(new HashMap<>(landmarks), new HashMap<>(fieldLines),
				(float) metaModel.getFieldDimensions().getX() / 2, (float) metaModel.getFieldDimensions().getY() / 2,
				(float) metaModel.getGoalDimensions().getX(), (float) metaModel.getGoalDimensions().getY() / 2,
				(float) metaModel.getGoalDimensions().getZ(), (float) metaModel.getPenaltyAreaDimensions().getX(),
				(float) metaModel.getPenaltyAreaDimensions().getY() / 2, metaModel.getMiddleCircleRadius());

		// players
		int numberOfPlayersPerTeam = IMagmaConstants.NUMBER_OF_PLAYERS_PER_TEAM;
		knownPlayers = new HashMap<>(numberOfPlayersPerTeam * 2);
		visiblePlayers = new ArrayList<>(numberOfPlayersPerTeam * 2);
	}

	/**
	 * Called once perception is finished parsing a new incoming message
	 * @param perception the object containing the result from parsing
	 */
	@Override
	public boolean update(IPerception perception)
	{
		// update global time
		ITimerPerceptor timerPerceptor = perception.getTime();
		if (timerPerceptor != null) {
			globalTime = timerPerceptor.getTime();
		}

		// update game time, play side and play mode, as well as scored goals
		processGameState((IRoboCupPerception) perception);

		// camera coordinate system is updated to current pose
		updateCameraCoordinateSystem(perception);

		// calculate the new position of the agent (will update landmarks accordingly)
		calculatePosition(perception);

		updateBall(perception);
		updatePlayers((IRoboCupPerception) perception);

		processHear((IRoboCupPerception) perception);

		// now inform observers about changes
		observer.onStateChange(this);
		return false;
	}

	protected void processGameState(IRoboCupPerception perception)
	{
		// update game time
		IGameStatePerceptor perceptor = perception.getGameState();
		if (perceptor == null) {
			return;
		}
		gameTime = perceptor.getTime();

		// update playSide
		String playSideString = perceptor.getTeamSide();
		PlaySide newPlaySide = PlaySide.parsePlaySide(playSideString);

		switch (newPlaySide) {
		case LEFT:
			if (playSide != PlaySide.LEFT) {
				// We switched from right to left side, so remember the new side and mirror the flags
				setPlaySide(PlaySide.LEFT);
			}
			break;
		case RIGHT:
			if (playSide != PlaySide.RIGHT) {
				// We switched from left to right side, so remember the new side and mirror the flags
				setPlaySide(PlaySide.RIGHT);
			}
			break;
		case UNKNOWN:
		default:
			// No playSide?!? Maybe I'm the referee?
			break;
		}

		// update team color
		teamColor = perceptor.getTeamColor();

		// update playMode
		String playModeString = perceptor.getPlaymode();
		previousGameState = gameState;

		playmode = PlayMode.parsePlayMode(playModeString);
		gameState = GameState.determineGameState(playmode, playSide);

		if (previousGameState != null) {
			if (previousGameState != GameState.OWN_PASS && gameState == GameState.OWN_PASS) {
				enteredPassModeTime = gameTime;
			} else if (previousGameState == GameState.OWN_PASS && gameState != GameState.OWN_PASS) {
				leftPassModeTime = gameTime;
			}
		}

		// update scored goals
		updateGoalsScored();

		if (isPenalty()) {
			// Update penalty state
			if (gameState == GameState.OWN_KICK_OFF) {
				penaltyState = PenaltyState.KICK;
			} else if (gameState == GameState.OPPONENT_KICK_OFF) {
				penaltyState = PenaltyState.HOLD;
			}
		}
	}

	/**
	 * Update the number of scored goals
	 */
	private void updateGoalsScored()
	{
		if (gameState == GameState.OWN_GOAL && previousGameState != GameState.OWN_GOAL) {
			goalsWeScored++;
		} else if (gameState == GameState.OPPONENT_GOAL && previousGameState != GameState.OPPONENT_GOAL) {
			goalsTheyScored++;
		}
	}

	private void processHear(IRoboCupPerception perception)
	{
		List<IHearPerceptor> hearList = perception.getHearPerceptors();
		for (IHearPerceptor hear : hearList) {
			String target = hear.getTarget();

			if ("self".equalsIgnoreCase(target)) {
				// message from ourselves
				break;
			}

			if (!hear.getTeam().equals(getTeamname(true))) {
				// message is not from our team
				break;
			}

			SayMessage message = new SayMessage(hear.getMessage());
			Vector3D teammatePosition = message.getTeammatePosition();
			Vector3D opponentPosition = message.getOpponentPosition();

			if (teammatePosition != null) {
				updatePlayerFromHear(message.getTeammateID(), true, teammatePosition);
			}
			if (opponentPosition != null) {
				updatePlayerFromHear(message.getTeammateID(), false, opponentPosition);
			}
		}
	}

	public void updateBallFromHear(Vector3D ballPos, Vector3D ballSpeed)
	{
		if (ball.getAge(globalTime) < TIME_TO_TRUST_HEAR && ball.getInformationSource() == InformationSource.VISION) {
			// We have recently seen the ball so no update
			return;
		}

		Vector3D localPos = thisPlayer.calculateLocalPosition(ballPos);
		ball.updateFromAudio(localPos, ballPos, ballSpeed, globalTime - 0.04f);
	}

	/**
	 * Updates the local and global position of the corresponding player instance
	 * according to the given global position.
	 *
	 * @param playerID the id of the player
	 * @param ownTeam true if the player to update is of the own team
	 * @param playerPos the new global position
	 */
	private void updatePlayerFromHear(int playerID, boolean ownTeam, Vector3D playerPos)
	{
		String teamName = getTeamname(ownTeam);
		if (teamName == null) {
			// As long we don't know the other team's name, we aren't able to
			// create proper player instances
			return;
		}

		Player player = (Player) knownPlayers.get(teamName + playerID);
		if (player == null) {
			// We hear from a player that we have never seen before
			player = new Player(playerID, teamName, ownTeam, getAgentModel().getCycleTime());
			knownPlayers.put(teamName + playerID, player);
			if (!visiblePlayers.contains(player)) {
				visiblePlayers.add(player);
			}
		}

		if (player.getAge(globalTime) < TIME_TO_TRUST_HEAR &&
				player.getInformationSource() == InformationSource.VISION) {
			// We have recently seen the player so no update
			return;
		}

		// Update player information
		Vector3D localPos = thisPlayer.calculateLocalPosition(playerPos);
		player.updateFromAudio(localPos, playerPos, globalTime - 0.04f);
	}

	/**
	 * If ownTeam is true we return the team name of thisPlayer. If false we
	 * return the name of the first seen other player ever. If we haven't seen
	 * any other player yet, this method returns <tt>null</tt>.
	 * @param ownTeam true if from own team, false if from other team
	 * @return the name of the team, if known, null if not
	 */
	public String getTeamname(boolean ownTeam)
	{
		if (ownTeam) {
			return thisPlayer.getTeamname();
		} else {
			return otherTeamName;
		}
	}

	/**
	 * @param perception the object containing the result from parsing
	 */
	protected void calculatePosition(IPerception perception)
	{
		IGlobalPosePerceptor globalPose = perception.getGlobalPose();
		if (globalPose != null) {
			calculatePositionFromGlobalPose(globalPose);
			return;
		}

		Rotation orientationEstimation = null;
		IRoboCupAgentModel agentModel = getAgentModel();

		// If we have a gyro sensor in the root body, use it to estimate the current orientation
		IGyroRate gyro = agentModel.getRootBodyPart().getSensor(IGyroRate.class);
		if (gyro != null) {
			Rotation globalGyro =
					Geometry.zTransformRotation(gyro.getOrientationChange(agentModel.getCycleTime()), -Math.PI / 2);
			orientationEstimation = localizer.getState().getLocalizedOrientation().applyTo(globalGyro);
		}

		// Use odometry information to predict the current position
		IPose3D odometry = new Pose3D(thisPlayer.getIntendedGlobalSpeed());
		if (localizer.predict(globalTime, odometry, orientationEstimation, 0)) {
			thisPlayer.updateFromOdometry(localizer.getState().getLocalizedPose());
		}

		// Extract visible point features of the current cycle
		List<IPointFeatureObservation> visibleLandmarks = new ArrayList<>();
		for (ILandmark l : landmarks.values()) {
			IVisibleObjectPerceptor voPerceptor = perception.getVisibleObject(l.getName());
			if (voPerceptor != null) {
				visibleLandmarks.add(new PointFeatureObservation(globalTime, l.getType(),
						toRootVisionSystem(voPerceptor.getPosition()), voPerceptor.hasDepth(), l.getName()));
			}
		}

		// Extract visible line features of the current cycle
		List<ILinePerceptor> visibleLines = perception.getVisibleLines();
		List<ILineFeatureObservation> visibleFieldLines = new ArrayList<>(visibleLines.size());
		for (ILinePerceptor pl : visibleLines) {
			visibleFieldLines.add(new LineFeatureObservation(globalTime, "line", toRootVisionSystem(pl.getPosition()),
					toRootVisionSystem(pl.getPosition2()), pl.hasDepth()));
		}

		// Reset visibility of landmarks and field lines
		fieldLines.values().forEach(l -> l.setVisible(false));
		landmarks.values().forEach(l -> l.setVisible(false));

		if (visibleLandmarks.size() + visibleFieldLines.size() > 0) {
			// Use vision information to correct the current position
			if (localizer.correct(globalTime, map, visibleLandmarks, visibleFieldLines, orientationEstimation, 0)) {
				thisPlayer.updateFromVision(localizer.getState().getLocalizedPose(), globalTime);
			}

			updateLandmarks(visibleLandmarks, visibleFieldLines);
		}
	}

	protected void updateLandmarks(
			List<IPointFeatureObservation> visibleLandmarks, List<ILineFeatureObservation> visibleFieldLines)
	{
		// Update landmarks
		if (visibleLandmarks != null) {
			for (IPointFeatureObservation o : visibleLandmarks) {
				if (o.isAssigned()) {
					Vector3D globalPos = thisPlayer.calculateGlobalPosition(o.getObservedPosition());

					landmarks.get(o.getName())
							.updateFromVision(Vector3D.PLUS_I, o.getObservedPosition(), globalPos, globalTime);
				}
			}
		}

		// Update field lines
		if (visibleFieldLines != null) {
			for (ILineFeatureObservation o : visibleFieldLines) {
				if (o.isAssigned()) {
					Vector3D localPos1 = o.getObservedPosition1();
					Vector3D localPos2 = o.getObservedPosition2();
					Vector3D globalPos1 = thisPlayer.calculateGlobalPosition(localPos1);
					Vector3D globalPos2 = thisPlayer.calculateGlobalPosition(localPos2);

					((FieldLine) fieldLines.get(o.getName()))
							.updatePositions(localPos1, localPos2, globalPos1, globalPos2, globalTime);
				}
			}
		}
	}

	protected void calculatePositionFromGlobalPose(IGlobalPosePerceptor globalPose)
	{
		Pose3D localizeInfo = globalPose.getGlobalPose();
		Vector3D position = localizeInfo.getPosition();
		thisPlayer.updateFromVision(Vector3D.ZERO, Vector3D.ZERO, position, globalTime);
		thisPlayer.setGlobalOrientation(localizeInfo.getOrientation());
	}

	/**
	 * @param perception the object containing the result from parsing
	 */
	protected void updateBall(IPerception perception)
	{
		IVisibleObjectPerceptor ballVision = perception.getVisibleObject(IRoboCupPerception.BALL);
		if (ballVision == null) {
			// the ball is currently not visible. This will be true every 2 out of
			// 3 cycles without camera image
			ball.setVisible(false);
			// ball.updateNoVision(globalTime);
			return;
		}

		Vector3D seenPosition = ballVision.getPosition();
		if (!ballVision.hasDepth()) {
			IRoboCupAgentModel agentModel = getAgentModel();
			IBodyPart cameraBodyPart = agentModel.getBodyPartContainingCamera();
			if (cameraBodyPart == null) {
				ball.setVisible(false);
				return;
			}
			// Pose3D cameraPose = cameraBodyPart.getPose();
			Pose3D cameraPose = agentModel.getCameraPose();
			seenPosition = estimateDepth(thisPlayer.getPose(), cameraPose, seenPosition, ball.getRadius());
			if (seenPosition == null) {
				ball.setVisible(false);
				return;
			}
		}

		// if our last update is older than 5 seconds, reset the ball position filter
		if (ball.getAge(globalTime) > 5.0f) {
			ballPosFilter.reset();
		}

		// filter ball position and recalculate local position
		Vector3D localPos = toRootVisionSystem(seenPosition);
		Vector3D globalPos = thisPlayer.calculateGlobalPosition(localPos);
		globalPos = ballPosFilter.filterPosition(globalPos, globalPos);
		localPos = thisPlayer.calculateLocalPosition(globalPos);

		ball.updateFromVision(seenPosition, localPos, globalPos, globalTime);
	}

	protected Vector3D estimateDepth(
			Pose3D thisPlayerPose, Pose3D cameraPose, Vector3D position, float heightAboveGround)
	{
		// transform camera to global coordinate system
		Pose3D rotationVision = new Pose3D(
				Vector3D.ZERO, new Rotation(Vector3D.PLUS_K, Math.PI / 2, RotationConvention.VECTOR_OPERATOR));
		Pose3D rotationTorso = new Pose3D(
				Vector3D.ZERO, new Rotation(Vector3D.PLUS_K, -Math.PI / 2, RotationConvention.VECTOR_OPERATOR));
		Pose3D globalPose = thisPlayerPose.applyTo(rotationTorso).applyTo(cameraPose).applyTo(rotationVision);

		// intersect ray to ball with field plane
		Plane objectPlane = new Plane(new Vector3D(0, 0, heightAboveGround), Vector3D.PLUS_K, 0.001);
		Vector3D p1 = globalPose.getPosition();
		Vector3D p2 = globalPose.applyTo(position);
		Line line = new Line(p1, p2, 0.001);
		Vector3D intersection = objectPlane.intersection(line);
		if (intersection == null) {
			return null;
		}

		// if the intersection is behind us, we do not accept that it is the ball
		// if (intersection.getX() < 0) {
		// // wrong ball or not on ground
		// // System.out.println("wrong");
		// return null;
		// }

		// convert back into camera coordinate system
		double distance = Vector3D.distance(p1, intersection);
		if (position.getNorm() < 0.00001) {
			position = Vector3D.PLUS_I;
		}
		return position.normalize().scalarMultiply(distance);
	}

	/**
	 * Updates the player information according to the new visual perception.
	 *
	 * @param perception the object containing the result from vision parsing
	 */
	protected void updatePlayers(IRoboCupPerception perception)
	{
		// We only act when vision information should be present
		if (!perception.containsVision()) {
			return;
		}

		// Clear visible players
		visiblePlayers.clear();

		// Process player visions
		List<IPlayerPos> playersVision = perception.getVisiblePlayers();
		Player currentPlayer;
		boolean isOwnTeam;
		IRoboCupAgentModel agentModel = getAgentModel();

		for (IPlayerPos playerVision : playersVision) {
			isOwnTeam = thisPlayer.getTeamname().equals(playerVision.getTeamname());

			// Don't process myself as a teammate
			if (isOwnTeam && playerVision.getId() == thisPlayer.getID()) {
				updatePlayer(thisPlayer, playerVision);
				continue;
			}

			// Fetch a player instance
			if (playerVision.getTeamname() == null ||
					IMagmaConstants.UNKNOWN_PLAYER_TEAMNAME.equals(playerVision.getTeamname()) ||
					playerVision.getId() == IMagmaConstants.UNKNOWN_PLAYER_NUMBER) {
				// If the player vision information is not complete, create just a
				// temporary player instance to hold the perceived information and
				// add it to the visiblePlayers array
				// TODO: Instead of simply adding an additional player by default,
				// one can perform some kind of lookup in the known players map and
				// try to match the player to an previously known player near by
				currentPlayer = new Player(
						playerVision.getId(), playerVision.getTeamname(), isOwnTeam, agentModel.getCycleTime());
				visiblePlayers.add(currentPlayer);
			} else {
				// If the player vision information is complete, fetch or create a
				// permanent instance for him
				currentPlayer = (Player) knownPlayers.get(playerVision.getTeamname() + playerVision.getId());
				if (currentPlayer == null) {
					currentPlayer = new Player(
							playerVision.getId(), playerVision.getTeamname(), isOwnTeam, agentModel.getCycleTime());
					knownPlayers.put(currentPlayer.getTeamname() + currentPlayer.getID(), currentPlayer);

					// Remember the name of the other team once we see the first
					// other player
					if (otherTeamName == null && !isOwnTeam) {
						otherTeamName = playerVision.getTeamname();
					}
				}
			}

			// Update the player information
			updatePlayer(currentPlayer, playerVision);
		}

		// Extract all recently seen players from known players map and mark the
		// others as not visible
		for (IPlayer player : knownPlayers.values()) {
			if (player.getAge(globalTime) < REMEMBRANCE_TIME) {
				// TODO: check if the player is in the visible cone and should have
				// been seen therefore

				visiblePlayers.add(player);
			} else {
				player.setVisible(false);
			}
		}
	}

	/**
	 * Updates a player's body parts as well as its local and global position
	 *
	 * @param player the player to update
	 * @param playerVision the update information
	 */
	protected void updatePlayer(Player player, IPlayerPos playerVision)
	{
		Vector3D localPos;
		Vector3D globalPos;

		// Transform body parts of player
		Map<String, Vector3D> bodyParts = new HashMap<>();
		Map<String, Vector3D> allBodyParts = playerVision.getAllBodyParts();
		for (String name : allBodyParts.keySet()) {
			localPos = toRootVisionSystem(allBodyParts.get(name));
			globalPos = thisPlayer.calculateGlobalPosition(localPos);

			bodyParts.put(name, globalPos);
		}

		if (!thisPlayer.equals(player)) {
			// Transform player positions
			localPos = toRootVisionSystem(playerVision.getPosition());
			globalPos = thisPlayer.calculateGlobalPosition(localPos);

			// Update positions and body parts of player
			player.update(playerVision.getPosition(), localPos, globalPos, bodyParts, globalTime);
		} else {
			thisPlayer.setBodyPartsVision(bodyParts, allBodyParts);
		}
	}

	/**
	 * @param id the player's number (1-11)
	 * @param ownTeam true if we want the player of our own team
	 * @return the referenced player, null if not in list
	 */
	@Override
	public IPlayer getVisiblePlayer(int id, boolean ownTeam)
	{
		if (id < 1) {
			return null;
		}
		for (IPlayer player : visiblePlayers) {
			if (id == player.getID() && ownTeam == player.isOwnTeam()) {
				return player;
			}
		}
		return null;
	}

	@Override
	public void resetLocalizer(IPose3D initialPose)
	{
		super.resetLocalizer(initialPose);

		// set localized pose after resetting the localizer
		thisPlayer.setGlobalPosition(localizer.getState().getLocalizedPosition(), globalTime);
		thisPlayer.setGlobalOrientation(localizer.getState().getLocalizedOrientation());
	}

	// --------------------------------------------------
	// General environment and game information
	@Override
	public RCSoccerField getMap()
	{
		return map;
	}

	@Override
	public float getGameTime()
	{
		return gameTime;
	}

	@Override
	public int getGoalsTheyScored()
	{
		return goalsTheyScored;
	}

	@Override
	public int getGoalsWeScored()
	{
		return goalsWeScored;
	}

	/**
	 * Currently only known for Humanoid League!
	 * @return the color of our team
	 */
	@Override
	public TeamColor getTeamColor()
	{
		return teamColor;
	}

	/**
	 * @return the current mode of the game as String
	 */
	@Override
	public PlayMode getPlaymode()
	{
		return playmode;
	}

	@Override
	public PlaySide getPlaySide()
	{
		return playSide;
	}

	private void setPlaySide(PlaySide playSide)
	{
		this.playSide = playSide;

		// mirror landmarks
		Map<String, ILandmark> mirroredLandmarks = new HashMap<>();
		Map<String, IFieldLine> mirroredFieldLines = new HashMap<>();

		for (ILandmark l : landmarks.values()) {
			mirroredLandmarks.put(l.getName(), new Landmark(l, true));
		}

		for (IFieldLine l : fieldLines.values()) {
			mirroredFieldLines.put(l.getName(), new FieldLine(l, true));
		}

		landmarks.clear();
		landmarks.putAll(mirroredLandmarks);
		fieldLines.clear();
		fieldLines.putAll(mirroredFieldLines);
		map.setPointFeatures(new HashMap<String, IPointFeature>(landmarks));
		map.setLineFeatures(new HashMap<String, ILineFeature>(fieldLines));
	}

	@Override
	public GameState getGameState()
	{
		return gameState;
	}

	// --------------------------------------------------
	// Visual object getters
	@Override
	public IBall getBall()
	{
		return ball;
	}

	@Override
	public float getBallRadiusDifference()
	{
		return ballRadiusDifference;
	}

	public void setBall(Ball ball)
	{
		this.ball = ball;
	}

	@Override
	public List<IVisibleObject> getGoalPostObstacles()
	{
		// the positions of the goal posts are slightly outside the field to allow
		// a bigger radius for avoidance
		List<IVisibleObject> result = new ArrayList<>(4);
		for (ILandmark lm : getLandmarks()) {
			double shiftOutside = 0.5;
			if (lm.getName().contains("G")) {
				Vector3D knownPos = lm.getKnownPosition();

				if (knownPos.getX() < 0) {
					shiftOutside = -shiftOutside;
				}
				result.add(new Landmark(lm.getName() + "Obstacle", lm.getType(),
						new Vector3D(knownPos.getX() + shiftOutside, knownPos.getY(), 0)));
			}
		}
		return result;
	}

	@Override
	public Vector2D goalLineIntersection(Vector2D point1, Vector2D point2, double offset)
	{
		Vector2D otherLeftGoalPost = new Vector2D(fieldHalfLength(), goalHalfWidth());
		Vector2D otherRightGoalPost = new Vector2D(fieldHalfLength(), -goalHalfWidth());
		org.apache.commons.math3.geometry.euclidean.twod.Line goalLine =
				new org.apache.commons.math3.geometry.euclidean.twod.Line(
						otherLeftGoalPost, otherRightGoalPost, 0.00001);
		org.apache.commons.math3.geometry.euclidean.twod.Line line =
				new org.apache.commons.math3.geometry.euclidean.twod.Line(point1, point2, 0.00001);
		Vector2D intersection = line.intersection(goalLine);
		if (intersection != null) {
			if (Math.abs(intersection.getY()) < goalHalfWidth() - offset) {
				return intersection;
			}
		}
		return null;
	}

	@Override
	public List<IPlayer> getVisiblePlayers()
	{
		return Collections.unmodifiableList(visiblePlayers);
	}

	public void setVisiblePlayers(List<IPlayer> players)
	{
		this.visiblePlayers = players;
	}

	@Override
	public IThisPlayer getThisPlayer()
	{
		return thisPlayer;
	}

	// --------------------------------------------------
	// EnvironmentModel Methods

	// --------------------------------------------------
	// Server version and soccer pitch description delegates
	@Override
	public int getServerVersion()
	{
		return serverVersion;
	}

	@Override
	public float fieldHalfLength()
	{
		return map.fieldHalfLength;
	}

	@Override
	public float fieldHalfWidth()
	{
		return map.fieldHalfWidth;
	}

	@Override
	public float penaltyHalfLength()
	{
		return map.penaltyHalfLength;
	}

	@Override
	public float penaltyWidth()
	{
		return map.penaltyWidth;
	}

	@Override
	public float goalHalfWidth()
	{
		return map.goalHalfWidth;
	}

	@Override
	public float goalHeight()
	{
		return map.goalHeight;
	}

	@Override
	public float goalDepth()
	{
		return map.goalDepth;
	}

	@Override
	public float centerCircleRadius()
	{
		return map.centerCircleRadius;
	}

	@Override
	public Vector3D getOwnGoalPosition()
	{
		return map.ownGoalPosition;
	}

	@Override
	public Vector3D getOtherGoalPosition()
	{
		return map.otherGoalPosition;
	}

	@Override
	public boolean isBallInCriticalArea()
	{
		Vector3D ballPos = getBall().getPosition();

		return isInCriticalArea(ballPos);
	}

	@Override
	public boolean isInCriticalArea(Vector3D position)
	{
		return position.getX() < -fieldHalfLength() + penaltyWidth() * 2 - 0.3 &&
				Math.abs(position.getY()) < penaltyHalfLength() + goalHalfWidth() * 2 - 0.3;
	}

	@Override
	public BallPassing ballIsPassing(Vector3D futureBallPosition)
	{
		Vector3D ballPosition = getBall().getPosition();
		Line2D ballLine = new Line2D.Double(
				futureBallPosition.getX(), futureBallPosition.getY(), ballPosition.getX(), ballPosition.getY());

		Angle playerAngle = getThisPlayer().getHorizontalAngle();

		Vector3D direction = new Vector3D(playerAngle.add(Math.PI / 2).radians(), 0).normalize();
		double length = 0.15;

		Vector3D playerPosition = getThisPlayer().getPosition();
		Vector3D centerLeft = playerPosition.add(direction.scalarMultiply(length));
		Vector3D centerRight = playerPosition.subtract(direction.scalarMultiply(length));
		Line2D playerDiameterCenter =
				new Line2D.Double(centerLeft.getX(), centerLeft.getY(), centerRight.getX(), centerRight.getY());
		if (playerDiameterCenter.intersectsLine(ballLine)) {
			return BallPassing.CENTER;
		}

		length = getAgentModel().getHeight() * 0.4;
		centerLeft = playerPosition.add(direction.scalarMultiply(length));
		Line2D leftLine =
				new Line2D.Double(centerLeft.getX(), centerLeft.getY(), playerPosition.getX(), playerPosition.getY());
		if (leftLine.intersectsLine(ballLine)) {
			return BallPassing.SHORT_LEFT;
		}

		centerRight = playerPosition.subtract(direction.scalarMultiply(length));
		Line2D rightLine =
				new Line2D.Double(centerRight.getX(), centerRight.getY(), playerPosition.getX(), playerPosition.getY());
		if (rightLine.intersectsLine(ballLine)) {
			return BallPassing.SHORT_RIGHT;
		}

		Vector3D left = playerPosition.add(direction.scalarMultiply(length * 3));
		Line2D playerDiameterLeft =
				new Line2D.Double(playerPosition.getX(), playerPosition.getY(), left.getX(), left.getY());
		if (playerDiameterLeft.intersectsLine(ballLine)) {
			return BallPassing.FAR_LEFT;
		}

		Vector3D right = playerPosition.subtract(direction.scalarMultiply(length * 3));
		Line2D playerDiameterRight =
				new Line2D.Double(playerPosition.getX(), playerPosition.getY(), right.getX(), right.getY());
		if (playerDiameterRight.intersectsLine(ballLine)) {
			return BallPassing.FAR_RIGHT;
		}

		// the ball is outside of the player's reach
		return BallPassing.UNREACHABLE;
	}

	// --------------------------------------------------
	// Basic object methods
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof RoboCupWorldModel)) {
			return false;
		}
		RoboCupWorldModel other = (RoboCupWorldModel) o;

		if (!ball.equals(other.ball)) {
			return false;
		}

		if (!landmarks.equals(other.landmarks)) {
			return false;
		}

		return visiblePlayers.equals(other.visiblePlayers);
	}

	@Override
	protected IRoboCupAgentModel getAgentModel()
	{
		return (IRoboCupAgentModel) super.getAgentModel();
	}

	@Override
	public float getEnteredPassModeTime()
	{
		return enteredPassModeTime;
	}

	@Override
	public float getLeftPassModeTime()
	{
		return leftPassModeTime;
	}

	public void setPenalty(boolean penalty)
	{
		this.penalty = penalty;
	}

	@Override
	public boolean isPenalty()
	{
		return penalty;
	}

	@Override
	public PenaltyState getPenaltyState()
	{
		return penaltyState;
	}
}
