/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IVisibleObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import hso.autonomy.util.properties.PropertyManager;
import java.util.List;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.behavior.IWalkEstimator;
import magma.agent.decision.behavior.IWalkEstimator.WalkMode;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IPositionManager;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Implements a behavior which is able to walk/run to a specified target
 * position
 */
public class WalkToPosition extends RoboCupSingleComplexBehavior
{
	private static final String TURN_TO_DESIRED_DIR_DISTANCE = "behavior.WalkToPosition.turnToDesiredDirDistance";

	private static final String OMNI_DIRECTIONAL_WALK_DISTANCE = "behavior.WalkToPosition.omniDirectionalWalkDistance";

	/** the maximum speed with which to walk forward */
	private double maxSpeedForward;

	protected transient IWalkEstimator walkEstimator;

	/** true if movement to home position */
	private boolean passiveMovement;

	private boolean avoidCollisions;

	private WalkMode currentWalkMode = WalkMode.FORWARD;

	private double slowDownDistance;

	private String paramSetName;

	/** class which calculates optimal path */
	// private OptimalPathCalculation optPathCalc;

	public WalkToPosition(IThoughtModel thoughtModel, BehaviorMap behaviors, IWalkEstimator walkEstimator)
	{
		super(IBehaviorConstants.WALK_TO_POSITION, thoughtModel, behaviors, IBehaviorConstants.WALK);
		maxSpeedForward = 100;
		this.walkEstimator = walkEstimator;
		// angularPrecision = 60;
		// fastestToRunDistance = 1.5;
		avoidCollisions = true;
		slowDownDistance = 0.8;

		// optPathCalc = new OptimalPathCalculation(OptPathVisualizer.VIS_ON);
		// String parameter = System.getenv().get("VARIATION_PARAMETER");
		// if (parameter != null) {
		// fastestToRunDistance = Double.parseDouble(parameter);
		// System.out.println("parameter: " + fastestToRunDistance);
		// }
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		IPositionManager poseManager = thisPlayer.getPositionManager();
		PoseSpeed2D targetPoseSpeed = poseManager.getFinalPositionSpeed();
		IPose2D targetPose = targetPoseSpeed.getPose();
		Vector2D finalPosition = targetPose.getPosition();
		Angle absDirAtPosition = targetPose.getAngle();
		IPose2D thisPlayerPose2D = thisPlayer.getPose2D();

		IPose2D nextPose = avoidCollisions ? avoidCollision(targetPose) : targetPose;

		// clear desired list from any previous collision evasions
		poseManager.clear();
		poseManager.addDesiredPosition(0, targetPoseSpeed);
		double distanceToFinal = thisPlayerPose2D.getDistanceTo(finalPosition);
		PoseSpeed2D nextPoseSpeed = null;
		if (nextPose != targetPose) {
			// System.out.print(";avoidObstacle;" + nextPose);
			// player is avoiding an obstacle so add new position to the list
			nextPoseSpeed = new PoseSpeed2D(nextPose, new Vector2D(1, 0));
			poseManager.addDesiredPosition(0, nextPoseSpeed);
		}

		// dir is the relative direction in which to face
		// when far we want to face to the destination position
		Vector2D nextPosition = nextPose.getPosition();
		Angle nextPositionAngle = thisPlayerPose2D.getAngleTo(nextPosition);

		currentWalkMode = WalkMode.FORWARD;
		if (distanceToFinal < PropertyManager.getFloat(OMNI_DIRECTIONAL_WALK_DISTANCE)) {
			// we limit walking backwards, sidewards or diagonal to limited distance
			currentWalkMode = walkEstimator.getFastestWalkMode(thisPlayerPose2D, poseManager.getDesiredPositions());
		}
		Angle walkModeAngle = getWalkModeAngle(currentWalkMode);
		Angle nextAngle = nextPositionAngle.add(walkModeAngle);

		if (distanceToFinal < PropertyManager.getFloat(TURN_TO_DESIRED_DIR_DISTANCE)) {
			// when close to destination we want to face into desired direction
			// but only if not avoiding obstacles
			if (nextPose == targetPose) {
				Angle relDirAtPosition = absDirAtPosition.subtract(thisPlayerPose2D.getAngle());
				nextAngle = relDirAtPosition;
			}
		}

		// Currently we only support speed in walking direction
		double speedXAtTarget = targetPoseSpeed.getSpeed().getX();
		Vector3D speedThere = new Vector3D(speedXAtTarget, 0, 0);
		if (nextPose != targetPose) {
			// we have an obstacle to avoid
			// if (distanceToNextPose <= 0.8) {
			// // if we run to an intermediate position and if we are almost
			// // there we do not turn
			// nextAngle = walkModeAngle;
			// }
			// double speedX = Geometry.getLinearFuzzyValue(0.2, 0.5, true,
			// distanceToFinal) * (1 - speedXAtTarget) + speedXAtTarget;
			double speedX = nextPoseSpeed.getSpeed().getX();
			if (distanceToFinal < 0.2) {
				speedX = speedXAtTarget;
			}
			speedThere = new Vector3D(speedX, 0, 0);
		}

		// we rotate the position by the rotation we want to perform to avoid
		// that a sidewise movement is done where turning would have sufficed
		Vector2D myPosition = thisPlayerPose2D.getPosition();
		Vector2D virtualPosition = nextPosition;

		// print desired position for printing graph
		// System.out.printf("%4.2f; %4.2f; \n", virtualPosition.getX(), virtualPosition.getY());

		// desired position in forward or backward direction
		IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
		Pose2D walkTo = new Pose2D(virtualPosition.subtract(myPosition), nextAngle);

		double slowDownDist = slowDownDistance;
		// no slow down if we are not facing the final direction or are walking
		// side or backwards
		if (Math.abs(nextAngle.degrees()) > 110) {
			slowDownDist = 0.3;
		} else {
			// if an opponent is close to our destination we slow down later
			IPlayer opponentAtBall = getThoughtModel().getOpponentAtBall();
			if (!worldModel.getGameState().isOwnKick() && opponentAtBall != null) {
				double distance = opponentAtBall.getDistanceToXY(virtualPosition);
				slowDownDist -= Geometry.getLinearFuzzyValue(2, 3, false, distance) * 0.3;
			}
		}

		walk.globalWalk(walkTo, speedThere, distanceToFinal, slowDownDist, maxSpeedForward, paramSetName);
		return walk;

		// Stefan Grossmann Version with optimal path calculation
		// speed is position changes of the last 3 cycles -> 1 cycle(20ms) * 50 =
		// distance in m/s
		// Pose2D walkTo = new Pose2D(virtualPosition/*.subtract(myPosition)*/,
		// nextPose.angle);
		// double startSpeed =
		// Math.sqrt(Math.pow(thisPlayer.getSpeed().getX(),2)+Math.pow(thisPlayer.getSpeed().getY(),2))/4*50;
		// boolean newSit = optPathCalc.newSituation(new
		// Pose2D(thisPlayer.getPosition(), thisPlayer.getHorizontalAngle()),
		// startSpeed, walkTo, speedThere, null);
		// IBehavior myWalk;
		// myWalk = optPathCalc.walkIt(behaviors);
		//
		// return myWalk;
	}

	protected Angle getWalkModeAngle(WalkMode fastestWalkMode)
	{
		switch (fastestWalkMode) {
		case BACKWARD:
			return Angle.ANGLE_180;
		case LEFT_SIDE:
			return Angle.deg(-90);
		case RIGHT_SIDE:
			return Angle.ANGLE_90;
		case DIAGONAL_LEFT:
			return Angle.deg(-45);
		case DIAGONAL_RIGHT:
			return Angle.deg(45);
		case DIAGONAL_BACK_LEFT:
			return Angle.deg(-135);
		case DIAGONAL_BACK_RIGHT:
			return Angle.deg(135);
		default:
			// forward
			return Angle.ZERO;
		}
	}

	/**
	 * Calculates if obstacles are in our way
	 * @param targetPos the destination position to run to
	 * @return old position (targetPos) if there is nothing in way, a new
	 *         position to avoid collisions otherwise
	 */
	IPose2D avoidCollision(IPose2D targetPos)
	{
		IPose2D result = targetPos;
		IRoboCupWorldModel worldModel = getWorldModel();
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		double targetPosDistance = thisPlayer.getDistanceToXY(targetPos.getPosition());

		List<IVisibleObject> obstacles = getThoughtModel().getObstacles();
		for (IVisibleObject obstacle : obstacles) {
			// check if the obstacle intersects our trajectory
			Vector3D obstaclePos = obstacle.getPosition();
			double distanceToMe = thisPlayer.getDistanceToXY(obstaclePos);
			if (distanceToMe > targetPosDistance) {
				// further target position
				continue;
			}

			// calculate distance to line me -> targetPos
			Vector3D mePos2D = new Vector3D(thisPlayer.getPosition().getX(), thisPlayer.getPosition().getY(), 0);
			Vector3D targetPos2D = new Vector3D(targetPos.getPosition().getX(), targetPos.getPosition().getY(), 0);
			Vector3D meTargetPos2D = targetPos2D.subtract(mePos2D);

			Vector3D obstaclePos2D = new Vector3D(obstaclePos.getX(), obstaclePos.getY(), 0);
			Vector3D meObstaclePos2D = obstaclePos2D.subtract(mePos2D);

			// check if destination position and obstacle position lie in opposite
			// direction
			if (meObstaclePos2D.getNorm() < 0.01) {
				continue; // avoid an exception on normalization
			}
			Angle angle = Angle.rad(Vector3D.angle(meTargetPos2D, meObstaclePos2D));
			if (Math.abs(angle.degrees()) > 90) {
				// obstacle is not in target direction but opposite direction
				continue;
			}

			// get a normalized orthogonal (in 2D space)
			if (meTargetPos2D.getNorm() < 0.01) {
				continue; // avoid an exception on normalization
			}
			meTargetPos2D = meTargetPos2D.normalize();
			Vector3D norm = new Vector3D(-meTargetPos2D.getY(), meTargetPos2D.getX(), 0);
			double distanceToLine = Vector3D.dotProduct(meObstaclePos2D, norm);
			distanceToLine = Math.abs(distanceToLine);

			double criticalDistance = obstacle.getCollisionDistance();
			if (obstacle == worldModel.getBall()) {
				// if we move to home position, we try to avoid to getting too close
				// to the ball.
				if (passiveMovement) {
					criticalDistance = 1.2;
				}
			}

			double delta = criticalDistance - distanceToLine;
			if (delta <= 0) {
				continue;
			}

			// we have to run around the obstacle
			// calculate base for intermediate positions
			double factor = Geometry.getLinearFuzzyValue(0.0, 0.2, true, distanceToMe - delta);

			Vector3D bestIntermediate;
			Vector3D intermediate1;
			Vector3D intermediate2;
			double distance1;
			double distance2;
			if (factor <= 0) {
				// we are inside the obstacle radius
				Vector3D normObstacle = new Vector3D(-meObstaclePos2D.getY(), meObstaclePos2D.getX(), 0);
				intermediate1 = mePos2D.add(criticalDistance, normObstacle);
				intermediate2 = mePos2D.subtract(criticalDistance, normObstacle);
				distance1 = intermediate1.distance(targetPos2D);
				distance2 = intermediate2.distance(targetPos2D);

			} else {
				// we are away from the obstacle
				Vector3D base = mePos2D.add(factor, meObstaclePos2D);
				intermediate1 = base.add(criticalDistance, norm);
				intermediate2 = base.subtract(criticalDistance, norm);
				distance1 = mePos2D.distance(intermediate1);
				distance2 = mePos2D.distance(intermediate2);
			}

			// decide for the shortest path
			bestIntermediate = intermediate1;
			if (distance2 < distance1) {
				bestIntermediate = intermediate2;
			}

			// calculate desired angle at intermediate point
			double desiredAngle = targetPos2D.subtract(bestIntermediate).getAlpha();
			result = new Pose2D(bestIntermediate, Angle.rad(desiredAngle));

			// TODO: do not stop in this case but calculate a path taking all
			// players into account using AStar search
			break;
		}
		return result;
	}

	/**
	 * Set new target position
	 *
	 * @param target New target Position, null, if current position should be
	 *        used
	 */
	public WalkToPosition setPosition(PoseSpeed2D target, double maxSpeedForward)
	{
		// the default is to avoid obstacles
		return setPosition(target, maxSpeedForward, true, 0.8);
	}

	public WalkToPosition setPosition(
			PoseSpeed2D target, double maxSpeedForward, boolean avoidCollisions, double slowDownDistance)
	{
		return setPosition(
				target, maxSpeedForward, false, avoidCollisions, slowDownDistance, IKDynamicWalkMovement.NAME_STABLE);
	}

	public WalkToPosition setPosition(PoseSpeed2D target, double maxSpeedForward, boolean avoidCollisions,
			boolean avoidTeammate, double slowDownDistance)
	{
		return setPosition(target, maxSpeedForward, false, avoidCollisions, avoidTeammate, slowDownDistance,
				IKDynamicWalkMovement.NAME_STABLE);
	}

	public WalkToPosition setPosition(PoseSpeed2D target, double maxSpeedForward, boolean passiveMovement,
			boolean avoidCollisions, double slowDownDistance, String paramSetName)
	{
		return setPosition(
				target, maxSpeedForward, passiveMovement, avoidCollisions, true, slowDownDistance, paramSetName);
	}

	/**
	 * Set new target position
	 * @param target New target Position, null, if current position should be used
	 * @param passiveMovement true if movement to home position
	 * @param avoidCollisions true to use obstacle avoidance
	 * @param avoidTeammate true to avoid walking to a teammate
	 * @param slowDownDistance the distance to the target at which to start slow down
	 * @param paramSetName the walk parameter set to use
	 */
	public WalkToPosition setPosition(PoseSpeed2D target, double maxSpeedForward, boolean passiveMovement,
			boolean avoidCollisions, boolean avoidTeammate, double slowDownDistance, String paramSetName)
	{
		this.maxSpeedForward = maxSpeedForward;
		this.passiveMovement = passiveMovement;
		this.avoidCollisions = avoidCollisions;
		this.slowDownDistance = slowDownDistance;
		this.paramSetName = paramSetName;

		IRoboCupWorldModel worldModel = getWorldModel();
		IThisPlayer thisPlayer = worldModel.getThisPlayer();
		if (target == null) {
			target = new PoseSpeed2D(
					new Pose2D(thisPlayer.getPosition(), thisPlayer.getHorizontalAngle()), Vector2D.ZERO);
		} else {
			Vector3D targetPosition;
			if (avoidTeammate) {
				targetPosition = avoidTeammate(target.getPose().getPosition3D());
			} else {
				targetPosition = target.getPose().getPosition3D();
			}

			target = new PoseSpeed2D(new Pose2D(targetPosition, target.getPose().getAngle()), target.getSpeed());
		}
		IPositionManager posManager = thisPlayer.getPositionManager();
		posManager.setDesiredPosition(target, false);

		return this;
	}

	/**
	 * We calculate whether teammates stop us from moving to a position
	 */
	Vector3D avoidTeammate(Vector3D targetPosition)
	{
		IPlayer thisPlayer = getWorldModel().getThisPlayer();

		for (IPlayer player : getThoughtModel().getTeammatesAtBall()) {
			// a teammate is close to our target position
			if (player.getDistanceToXY(targetPosition) < 0.5) {
				// we are close to the target position or our teammate so we stop to avoid a collision
				if (thisPlayer.getDistanceToXY(targetPosition) < 1 || thisPlayer.getDistanceToXY(player) < 1) {
					return thisPlayer.getPosition();
				}
			}
		}

		// If no teammates are near to the new position just go there
		return targetPosition;
	}

	public WalkMode getCurrentWalkMode()
	{
		return currentWalkMode;
	}

	public IWalkEstimator getWalkEstimator()
	{
		return walkEstimator;
	}
}
