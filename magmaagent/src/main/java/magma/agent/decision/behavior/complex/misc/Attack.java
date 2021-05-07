/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.misc;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IKick;
import magma.agent.decision.behavior.IKickDecider;
import magma.agent.decision.behavior.complex.RoboCupComplexBehavior;
import magma.agent.decision.behavior.complex.walk.Dribbling;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.model.worldmodel.IPassModeConstants;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * This Behavior is the basic behavior for the closest player to the ball. It
 * navigates the robot to the intended kick position, dribbles the ball, or
 * triggers a specific kick, in an appropriate situation.
 *
 * @author Stefan Glaser
 */
public class Attack extends RoboCupComplexBehavior
{
	protected transient List<String> availableKicks;

	protected String defaultKick;

	protected String kickHeadingFor;

	public Attack(IThoughtModel thoughtModel, BehaviorMap behaviors, List<String> availableKicks)
	{
		this(IBehaviorConstants.ATTACK, thoughtModel, behaviors, availableKicks);
	}

	public Attack(String name, IThoughtModel thoughtModel, BehaviorMap behaviors, List<String> availableKicks)
	{
		super(name, thoughtModel, behaviors);

		this.availableKicks = new ArrayList<>(availableKicks);
		this.defaultKick = availableKicks.get(0);
		this.kickHeadingFor = availableKicks.get(0);
	}

	@Override
	public List<IBehavior> decideNextBasicBehaviors()
	{
		List<IBehavior> standardSituationWalk = handleStandardSituation();
		if (standardSituationWalk != null) {
			return standardSituationWalk;
		}

		// Check if we should run over the ball into the goal
		IPose2D kickPose = shouldWeRunIntoGoal();
		if (kickPose != null) {
			return Collections.singletonList(walkToPosition(new PoseSpeed2D(kickPose, new Vector2D(1, 0))));
		}

		// Check kick options
		List<IBehavior> preferredKicks = checkKicks();
		if (preferredKicks != null) {
			return preferredKicks;
		}

		// We neither want to kick nor dribble, so get in some better position to
		// the ball
		Vector3D ballPos = getEstimatedInterceptionPoint();

		PoseSpeed2D kickPoseSpeed = getFastestKickPosition(ballPos);

		return Collections.singletonList(walkToPosition(kickPoseSpeed));
	}

	protected List<IBehavior> checkKicks()
	{
		List<KickExecutability> preferredKicks = getPreferredKicks();
		if (!preferredKicks.isEmpty()) {
			// System.out.println("Kick List: " + preferredKicks);
			return preferredKicks.stream().map(executability -> executability.kick).collect(Collectors.toList());
		}

		return null;
	}

	public List<IBehavior> handleStandardSituation()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();

		IPose2D pose = null;
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		switch (getWorldModel().getGameState()) {
		case OPPONENT_GOAL_KICK:
			// try to block the opponent goalie's goal kick
			pose = new Pose2D(13.05, 0);
			break;
		case OPPONENT_FREE_KICK:
		case OPPONENT_DIRECT_FREE_KICK:
		case OPPONENT_CORNER_KICK:
		case OPPONENT_KICK_IN:
			Vector3D position = Geometry.getPointOnLineAbsoluteEnd(thisPlayer.getPosition(), ballPos, 2.4);
			pose = new Pose2D(position, thisPlayer.getDirectionTo(ballPos));
			break;
		case OPPONENT_PASS:
			position = Geometry.getPointOnLineAbsoluteEnd(
					thisPlayer.getPosition(), ballPos, IPassModeConstants.AREA_RADIUS + 0.2);
			pose = new Pose2D(position, thisPlayer.getDirectionTo(ballPos));
			break;
		}

		if (pose == null) {
			return null;
		}
		double distanceToTarget = thisPlayer.getDistanceToXY(pose.getPosition());
		int speed = distanceToTarget < 1 ? 50 : 100;
		return Collections.singletonList(walkToPosition(new PoseSpeed2D(pose, Vector2D.ZERO), true, speed, 0.8));
	}

	public String getKickHeadingFor()
	{
		return kickHeadingFor;
	}

	protected Angle getIntendedKickDirection()
	{
		return getThoughtModel().getIntendedKickDirection();
	}

	protected double getIntendedKickDistance()
	{
		return getThoughtModel().getIntendedKickDistance();
	}

	protected Vector3D getEstimatedInterceptionPoint()
	{
		IMoveableObject ball = getWorldModel().getBall();

		if (ball.isMoving()) {
			// loses 0.421 - 0.354 in series of 178 games
			/*Vector3D ballPos = ball.getPosition();
			Vector3D ballTargetPos = ball.getFuturePositions(100)[99];
			Vector3D ballDirection = ballTargetPos.subtract(ballPos);

			double angleToBallTarget =
					Math.abs(getWorldModel().getThisPlayer().getDirectionTo(ballDirection).degrees());
			if (ball.getSpeed().getNorm() > 0.05 && angleToBallTarget < 45) {
				Vector3D playerPos = getWorldModel().getThisPlayer().getPosition();

				Vector3D point = Geometry.getClosestPointOnLine(ballPos, ballTargetPos, playerPos);
				if (playerPos.distance(point) > 0.04) {
					return point;
				}
			}*/

			return calculateInterceptionPoint(ball);
		}

		// if the ball is resting, we check if opponent is dribbling
		IPlayer opponent = getThoughtModel().getOpponentAtBall();
		Vector3D interceptionPoint = calculateInterceptionPoint(opponent);
		if (opponent == null || shouldCancelInterception(opponent, interceptionPoint)) {
			return ball.getPosition();
		}

		if (interceptionPoint.getX() < -getWorldModel().fieldHalfLength()) {
			// we would intercept behind the goal, so goal middle
			return new Vector3D(-getWorldModel().fieldHalfLength(), 0, 0);
		}

		return interceptionPoint;
	}

	private Vector3D calculateInterceptionPoint(IMoveableObject moveableObject)
	{
		if (moveableObject == null) {
			return null;
		}

		Vector3D[] futurePositions = moveableObject.getFuturePositions(100);
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		return Geometry.getInterceptionPoint(
				thisPlayer.getPosition(), futurePositions, thisPlayer.getSpeed().getNorm() * 0.7, 100);
	}

	private boolean shouldCancelInterception(IPlayer opponent, Vector3D interceptionPoint)
	{
		IMoveableObject ball = getWorldModel().getBall();

		Angle angleOpponentBall = opponent.getDirectionTo(getWorldModel().getBall());
		if (Math.abs(angleOpponentBall.degrees()) < 120) {
			// only intercept between opponent and own goal
			return true;
		}

		double thisBallDist = getWorldModel().getThisPlayer().getDistanceToXY(ball);
		double opponentBallDist = opponent.getDistanceToXY(ball);
		if (thisBallDist < 2.0 || opponentBallDist > 0.8) {
			return true;
		}

		if (Math.abs(interceptionPoint.getY()) > getWorldModel().fieldHalfWidth()) {
			// we would leave the field
			return true;
		}

		if (Math.abs(interceptionPoint.getX()) > getWorldModel().fieldHalfLength()) {
			// we would leave the field
			return true;
		}

		if (ball.getDistanceToXY(opponent) >= opponent.getDistanceToXY(interceptionPoint)) {
			// interception point would be further than ball
			return true;
		}

		return false;
	}

	/**
	 * Returns the parametrized WalkToPosition behavior.
	 *
	 * @return the WalkToPosition behavior
	 */
	protected IBehavior walkToPosition(PoseSpeed2D target)
	{
		return walkToPosition(target, true, 100, 0.8);
	}

	protected IBehavior walkToPosition(
			PoseSpeed2D target, boolean avoidObstacles, int maxSpeedForward, double slowDownDistance)
	{
		return ((WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION))
				.setPosition(target, maxSpeedForward, avoidObstacles, false, slowDownDistance);
	}

	/**
	 * Check if we should run over the ball into the goal.
	 *
	 * @return the target pose, if we should run over the ball into the goal, or
	 *         null otherwise
	 */
	protected Pose2D shouldWeRunIntoGoal()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		Vector3D ballPos = getWorldModel().getBall().getPosition();
		float goalHalfWidth = getWorldModel().goalHalfWidth() - 0.05f;

		if (ballPos.getX() > (getWorldModel().fieldHalfLength() - 2) && ballPos.getY() < goalHalfWidth &&
				ballPos.getY() > -goalHalfWidth && thisPlayer.isInsideArea(ballPos, Dribbling.DRIBBLEABLE_AREA)) {
			double viewDirection = thisPlayer.getHorizontalAngle().degrees();

			// Ball is very close or inside the opponent's goal
			// Therefore just allow dribblings towards the goal
			if (ballPos.getX() > (getWorldModel().fieldHalfLength() - 0.3)) {
				double limit = 80 - (Math.abs(ballPos.getY()) / goalHalfWidth) * 80;

				if ((ballPos.getY() < 0 && 80 > viewDirection && viewDirection > -limit) ||
						(ballPos.getY() >= 0 && limit > viewDirection && viewDirection > -80)) {
					// 3. Dribble 10cm forwards with full speed
					return getDribblePosition();
				}
			} else {
				IMoveableObject ball = getWorldModel().getBall();
				Vector3D otherGoalPos = getWorldModel().getOtherGoalPosition();

				double ballToOtherUpperPost =
						ball.getDirectionTo(otherGoalPos.add(new Vector3D(0, goalHalfWidth, 0))).degrees();
				double ballToOtherLowerPost =
						ball.getDirectionTo(otherGoalPos.subtract(new Vector3D(0, goalHalfWidth, 0))).degrees();

				if (ballToOtherUpperPost > viewDirection && viewDirection > ballToOtherLowerPost) {
					return getDribblePosition();
				}
			}
		}

		return null;
	}

	/**
	 * Calculates target position for dribbling, based on our current position
	 * and orientation.
	 *
	 * @return the target pose for dribbling (usually 10 cm in front of us)
	 */
	private Pose2D getDribblePosition()
	{
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		Angle viewDirection = thisPlayer.getHorizontalAngle();

		Vector3D ballPos = getWorldModel().getBall().getPosition();
		Vector3D targetPos = Geometry.getPointOnLineAbsoluteEnd(thisPlayer.getPosition(), ballPos, 0.03);
		return new Pose2D(targetPos, viewDirection);
	}

	protected float getKickPower()
	{
		return 1;
	}

	private IKick getKick(String name)
	{
		return (IKick) behaviors.get(name);
	}

	/**
	 * Check which kick is currently possible and as well suitable to the current
	 * situation.
	 *
	 * @return a list of possible, valid kicks, sorted from best to worst
	 */
	private List<KickExecutability> getPreferredKicks()
	{
		float kickPower = getKickPower();
		ArrayList<KickExecutability> preferredKicks = new ArrayList<>();
		double intendedKickDistance = getIntendedKickDistance();
		Angle intendedKickDirection = getIntendedKickDirection();

		double bestApplicability = -1;
		IKick bestApplicableKick = null;
		double bestExecutability = -1;
		IKick bestExecutableKick = null;

		// check each kick and find the one we want most
		for (String kickName : availableKicks) {
			IKick kick = getKick(kickName);
			IKickDecider decider = kick.getKickDecider();
			decider.setIntendedKickDirection(intendedKickDirection);
			decider.setIntendedKickDistance(intendedKickDistance);
			decider.setKickPower(kickPower);

			double applicability = decider.getApplicability();
			if (applicability <= 0) {
				continue;
			}

			if (bestApplicability < applicability) {
				bestApplicability = applicability;
				bestApplicableKick = kick;
			}

			double executability = decider.getExecutability();
			if (executability <= 0) {
				continue;
			}

			if (bestExecutability < executability) {
				bestExecutability = executability;
				bestExecutableKick = kick;
			}
		}

		if (bestExecutableKick == null) {
			// no kick executable
			return preferredKicks;
		}

		if (haveTime() && bestExecutableKick != bestApplicableKick) {
			// we wait for the desired kick to get executable
			// System.out.println("Waiting for desired kick: " + bestApplicableKick.getName());
			return preferredKicks;
		}

		// System.out.println("Want kick: " + bestExecutableKick.getName());
		preferredKicks.add(new KickExecutability(bestExecutableKick, bestExecutability));

		return preferredKicks;
	}

	private boolean haveTime()
	{
		return !getThoughtModel().isOpponentNearBall();
	}

	/**
	 * Calculate the fastest possible kick position
	 *
	 * @return the fastest kick pose
	 */
	private PoseSpeed2D getFastestKickPosition(Vector3D ballPos)
	{
		double intendedKickDistance = getIntendedKickDistance();
		Angle intendedKickDirection = getIntendedKickDirection();
		// Check if the ball is directly in front of the opponent's goal, where
		// choosing the direction to the goal center is a bad choice.
		if (getWorldModel().getBall().getDistanceToXY(getWorldModel().getOtherGoalPosition()) <
				getWorldModel().goalHalfWidth()) {
			// We set the direction just straight towards the goal
			intendedKickDirection = Angle.ZERO;
		}

		float bestApplicability = 0;
		IKick mostApplicable = getKick(defaultKick);

		for (String kickName : availableKicks) {
			IKick kick = getKick(kickName);
			IKickDecider decider = kick.getKickDecider();
			decider.setIntendedKickDistance(intendedKickDistance);
			decider.setIntendedKickDirection(intendedKickDirection);
			decider.setExpectedBallPosition(ballPos);

			float currentApplicability = decider.getApplicability();
			if (currentApplicability > bestApplicability) {
				bestApplicability = currentApplicability;
				mostApplicable = kick;
			}
			//			System.out.println("Team: " + getWorldModel().getThisPlayer().getTeamname() + " Kick: " +
			// kick.getName() + 							   " applicability: " + kick.getApplicability());
		}
		kickHeadingFor = mostApplicable.getName();
		// Decide for which kick we want to walk to the ball
		//		System.out.println(" Kick   : " + mostApplicable.getName() +
		//						   " applicability: " + mostApplicable.getApplicability() +
		//						   "Team: " + getWorldModel().getThisPlayer().getTeamname());
		IPose2D absoluteRunToPose = mostApplicable.getKickDecider().getAbsoluteRunToPose();
		return new PoseSpeed2D(absoluteRunToPose, mostApplicable.getKickDecider().getSpeedAtRunToPose());
	}

	/**
	 * Helper class for storing an executability to a kick.
	 */
	private class KickExecutability
	{
		IKick kick;

		double executability;

		public KickExecutability(IKick kick, double util)
		{
			this.kick = kick;
			this.executability = util;
		}

		@Override
		public String toString()
		{
			return String.format("%s: %.5f", kick.getName(), executability);
		}
	}
}
