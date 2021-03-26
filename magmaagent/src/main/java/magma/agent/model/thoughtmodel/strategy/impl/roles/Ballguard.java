/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This role primarily guards the ball, trying to be between the ball and our
 * goal. Each of our guards has its own offsets to the ball.
 *
 * @author Stephan Kammerer
 */
public class Ballguard extends Role
{
	private final BallguardPosition ballguardPosition;

	private static final int BALL_PREDICTION_TIME = 100;

	/** Offset in x direction behind the ball */
	private final float xBallOffset;

	public Ballguard(String name, IRoboCupWorldModel worldModel, BallguardPosition ballGuardPosition, float xBallOffset,
			float safetyDistanceOffset, float priority)
	{
		super(worldModel, name, priority,								 //
				-worldModel.fieldHalfLength(),							 // minX
				worldModel.fieldHalfLength() - 3, safetyDistanceOffset); // maxX

		this.ballguardPosition = ballGuardPosition;
		this.xBallOffset = xBallOffset;
	}

	@Override
	public IPose2D getIndependentTargetPose()
	{
		return calculatePosition(false);
	}

	@Override
	protected IPose2D determinePosition()
	{
		return calculatePosition(true);
	}

	private IPose2D calculatePosition(boolean dependentOnPlayer)
	{
		Vector3D ballPos = worldModel.getBall().getFuturePosition(BALL_PREDICTION_TIME);
		Vector3D ownGoalPos = worldModel.getOwnGoalPosition();

		Vector3D pos;

		float yOffset = 1.3f;
		if (xBallOffset > 4) {
			// back defender
			yOffset = 2.0f;
		}

		double offset = xBallOffset;
		if (dependentOnPlayer &&
				ballPos.distance(ownGoalPos) < worldModel.getThisPlayer().getDistanceToXY(ownGoalPos)) {
			offset = ballPos.distance(ownGoalPos) / 2;
		}

		switch (ballguardPosition) {
		case LEFT:
			pos = Geometry.getPointOnOrthogonal2D(ownGoalPos, ballPos, offset, -yOffset);
			break;
		case CENTER:
			pos = Geometry.getPointOnLineAbsoluteEnd(ownGoalPos, ballPos, offset);
			break;
		case RIGHT:
			// +0.2 to prevent both running to the ball
			pos = Geometry.getPointOnOrthogonal2D(ownGoalPos, ballPos, offset, yOffset + 0.2);
			break;
		default:
			throw new IllegalArgumentException("unexpected enum value: " + ballguardPosition);
		}

		// keep positions inside field
		double targetY = ValueUtil.limitAbs(pos.getY(), worldModel.fieldHalfWidth() - 0.5);
		double targetX = keepXLimits(pos.getX());

		Vector3D target = new Vector3D(targetX, targetY, 0);
		Angle ballDirection = calculateBallDirection(target);

		return new Pose2D(target, ballDirection);
	}

	public enum BallguardPosition { LEFT, CENTER, RIGHT }
}
