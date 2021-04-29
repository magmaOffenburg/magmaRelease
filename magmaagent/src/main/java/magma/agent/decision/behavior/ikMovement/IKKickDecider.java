/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.decision.behavior.base.KickDecider;
import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.base.KickEstimator;
import magma.agent.model.agentmodel.SupportFoot;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 *
 */
public class IKKickDecider extends KickDecider
{
	private float minXOffset;
	private IPose2D targetPose;

	public IKKickDecider(KickEstimator kickWalkEstimator, KickDistribution distribution, float minXOffset,
			SupportFoot kickingFoot, Pose2D relativeRunToPose, Vector2D speedAtRunToPose, Angle relativeKickDirection,
			Angle kickDirection, double maxKickDistance, double minKickDistance, float opponentMinDistance,
			float opponentMaxDistance, float ballMaxSpeed, float ownMinSpeed, float ownMaxSpeed, int ballHitCycles,
			boolean unstable, float priority)
	{
		super(kickWalkEstimator, distribution, kickingFoot, relativeRunToPose, speedAtRunToPose, relativeKickDirection,
				kickDirection, maxKickDistance, minKickDistance, opponentMinDistance, opponentMaxDistance, ballMaxSpeed,
				ownMinSpeed, ownMaxSpeed, ballHitCycles, unstable, priority);
		this.minXOffset = minXOffset;
		targetPose = new Pose2D();
	}

	public void setTargetPose(IPose2D targetPose)
	{
		this.targetPose = targetPose;
	}

	@Override
	public float getExecutability()
	{
		float kickEstimatorExecutability = kickEstimator.getExecutability();
		if (kickEstimatorExecutability < 0) {
			return -1;
		}

		final int MIN_ANGLE = 5;
		final int MAX_ANGLE = 45;
		boolean isKickingFootLeft = getKickingFoot() == SupportFoot.LEFT;
		int maxAngle = isKickingFootLeft ? MIN_ANGLE : MAX_ANGLE;
		int minAngle = isKickingFootLeft ? -MAX_ANGLE : -MIN_ANGLE;

		if (targetPose.getAngle().degrees() > maxAngle || targetPose.getAngle().degrees() < minAngle) {
			// If the turn degrees are outside the allowed range, we can't reach
			// the preferred position exactly so we can't kick here
			return -1;
		}

		// Create a polygon describing the area of reachable positions based on
		// the intended turn angle
		double MAX_HALF_STEP_WIDTH = 0.03;
		double MAX_STEP_LENGTH = 0.06;
		int sideFactor = isKickingFootLeft ? 1 : -1;
		double angleFactor = Math.abs(targetPose.getAngle().degrees()) / MAX_ANGLE;

		float minX = (float) ((sideFactor * minXOffset) - MAX_HALF_STEP_WIDTH);
		float maxX = (float) ((sideFactor * minXOffset) + MAX_HALF_STEP_WIDTH);
		float minY = (float) -MAX_STEP_LENGTH;
		float maxY = (float) (minY + Math.abs(minY) * (2 - angleFactor));

		Area2D.Float kickableArea = new Area2D.Float(minX, maxX, minY, maxY);
		if (kickableArea.contains(targetPose.getPosition())) {
			// If the last step is within the range we think is accurate and
			// stable, return the sum of absolute distances to the optimal last
			// step as utility value (the rotation is weighted by 0.1 times the
			// radian angle deviation; this weights a 60 degrees angle deviation
			// roughly equal to a 10 cm position deviation)
			return kickEstimatorExecutability * 100 +
					(float) (Math.abs(Math.abs(targetPose.getX()) - 0.11) + Math.abs(targetPose.getY()) +
							 Math.abs(targetPose.getAngle().radians() / 10));
		}

		// if it wasn't possible to kick the ball until here, it'll never be the
		// case and thus return a negative value
		return -1;
	}
}
