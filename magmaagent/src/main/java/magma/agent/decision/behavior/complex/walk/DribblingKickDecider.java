/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import magma.agent.IHumanoidConstants;
import magma.agent.decision.behavior.base.KickDecider;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 *
 */
public class DribblingKickDecider extends KickDecider
{
	private Angle intendedKickOffset;

	public DribblingKickDecider(KickEstimator kickWalkEstimator, KickDistribution distribution,
			Angle intendedKickOffset, SupportFoot kickingFoot, Pose2D relativeRunToPose, Vector2D speedAtRunToPose,
			Angle relativeKickDirection, Angle kickDirection, double maxKickDistance, double minKickDistance,
			float opponentMinDistance, float opponentMaxDistance, float ballMaxSpeed, float ownMinSpeed,
			float ownMaxSpeed, int ballHitCycles, boolean unstable, float priority)
	{
		super(kickWalkEstimator, distribution, kickingFoot, relativeRunToPose, speedAtRunToPose, relativeKickDirection,
				kickDirection, maxKickDistance, minKickDistance, opponentMinDistance, opponentMaxDistance, ballMaxSpeed,
				ownMinSpeed, ownMaxSpeed, ballHitCycles, unstable, priority);
		this.intendedKickOffset = intendedKickOffset;
	}

	@Override
	public void setIntendedKickDirection(Angle intendedKickDirection)
	{
		// value is ignored by Dribbling
		this.intendedKickDirection = intendedKickOffset;
	}

	@Override
	public IPose2D getAbsoluteRunToPose()
	{
		// dribblings are fixed direction and ignore intended direction
		Vector3D ballPos = getExpectedBallPosition();
		return new Pose2D(ballPos.getX() + getRelativeRunToPose().getX(),
				ballPos.getY() + getRelativeRunToPose().getY(), intendedKickOffset);
	}

	@Override
	public float getExecutability()
	{
		float kickEstimatorExecutability = kickEstimator.getExecutability();
		if (kickEstimatorExecutability < 0) {
			return -1;
		}

		if (getApplicability() < 0) {
			return -1;
		}

		// do not dribble if no opponents
		if (getThoughtModel().getOpponentAtBall() == null) {
			return -1;
		}

		Vector3D ballPosition = getWorldModel().getBall().getPosition();
		int sideFactor = getKickingFoot() == SupportFoot.RIGHT ? 1 : -1;
		Pose2D ballPose = new Pose2D(ballPosition, getKickDirection());
		String otherFootName =
				getKickingFoot() == SupportFoot.RIGHT ? IHumanoidConstants.LFoot : IHumanoidConstants.RFoot;
		Pose3D currentOtherFootPose = getThoughtModel().getAgentModel().getBodyPart(otherFootName).getPose();
		Pose2D globalOtherFootPose = getWorldModel().getThisPlayer().calculateGlobalBodyPose2D(currentOtherFootPose);

		Pose2D diffPose = globalOtherFootPose.applyInverseTo(ballPose);
		diffPose.y += sideFactor * 0.11;

		Vector3D otherGoalPos = getWorldModel().getOtherGoalPosition();
		double distanceToOpponentGoal = getWorldModel().getBall().getDistanceToXY(otherGoalPos);

		double minDeg = -20;
		double maxDeg = 20;

		if (distanceToOpponentGoal < 4) {
			Vector3D leftGoalPost = otherGoalPos.add(new Vector3D(0, getWorldModel().goalHalfWidth(), 0));
			Vector3D rightGoalPost = otherGoalPos.add(new Vector3D(0, -getWorldModel().goalHalfWidth(), 0));

			maxDeg = Angle.rad(leftGoalPost.subtract(ballPosition).getAlpha()).subtract(getKickDirection()).degrees();
			minDeg = Angle.rad(rightGoalPost.subtract(ballPosition).getAlpha()).subtract(getKickDirection()).degrees();

			if (minDeg > -5) {
				minDeg = -5;
			}

			if (maxDeg < 5) {
				maxDeg = 5;
			}
		}

		// Return failure if angle deviation is bigger than 10 degrees
		if (diffPose.getAngle().degrees() < minDeg || diffPose.getAngle().degrees() > maxDeg) {
			return -1;
		}

		// Return failure if y deviation is bigger than 3 cm
		if (Math.abs(diffPose.y) > 0.07) {
			return -1;
		}

		// Return failure if x distance is bigger than 7 cm
		if (diffPose.x > 0.50 || diffPose.x < 0.0) {
			return -1;
		}

		return kickEstimatorExecutability * 10 + (float) (Math.abs(Math.abs(diffPose.x) - 0.11) + Math.abs(diffPose.y));
	}

	@Override
	public float getApplicability()
	{
		IMoveableObject ball = getWorldModel().getBall();
		Vector3D ballPosition = ball.getPosition();
		Angle kickDirection = getKickDirection();
		// do not dribble towards our own goal
		double maxAngle = Geometry.getLinearFuzzyValue(-4, 8, true, ballPosition.getX()) * 40 + 80;
		if (Math.abs(kickDirection.degrees()) > maxAngle) {
			return -1;
		}

		// do not dribble too far away from other goal
		Vector3D otherGoalPosition = getWorldModel().getOtherGoalPosition();
		Angle goalDirection = ball.getDirectionTo(otherGoalPosition);
		double delta = Math.abs((goalDirection.subtract(kickDirection)).degrees());
		if (delta > 90) {
			return -1;
		}

		// do not dribble in vicinity of the other goal
		// (dribbling into goal is unaffected, it is another dribbling routine in Attack)
		IThisPlayer thisPlayer = getWorldModel().getThisPlayer();
		if (thisPlayer.getDistanceToXY(otherGoalPosition) < 6) {
			return -1;
		}

		// do not dribble if lateral ball speed is too high
		Vector2D speed2D = Geometry.getLocalHorizontalSpeed(thisPlayer.getGlobalOrientation(), ball.getSpeed());
		if (Math.abs(speed2D.getY()) > 0.008) {
			// System.out.println("No dribbling, ball lateral speed: " + speed2D);
			return -1;
		}

		// do not dribble if not upright
		double up = thisPlayer.getUpVectorZ();
		if (up < 0.990) {
			// System.out.println("Not upright: " + up);
			return -1.0f;
		}

		// do not dribble into opponent
		// if an opponent is nearer to the ball than our own player we don't want to dribble
		IPlayer opponentAtBall = getThoughtModel().getOpponentAtBall();
		if (opponentAtBall != null) {
			double opponentDistance = opponentAtBall.getDistanceToXY(ballPosition);
			double ownDistance = getWorldModel().getThisPlayer().getDistanceToXY(ballPosition);
			if (opponentDistance <= ownDistance) {
				return -1;
			}
			// if the opponent is in front of our own player, we don't want to dribble
			Angle directionBallMe = ball.getDirectionTo(getWorldModel().getThisPlayer());
			Angle directionBallOpponent = ball.getDirectionTo(opponentAtBall.getPosition());
			double deltaDirection = Math.abs((directionBallMe.subtract(directionBallOpponent)).degrees());
			if (deltaDirection > 150 && opponentDistance <= 1) {
				return -1;
			}
		}

		return kickEstimator.getApplicability();
	}
}
