/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.util.geometry.Angle;
import java.util.SortedSet;
import java.util.TreeSet;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.KickPositionEstimation;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PenaltyKickPositionProfiler extends KickPositionProfilerBase
{
	public PenaltyKickPositionProfiler(IRoboCupThoughtModel thoughtModel)
	{
		super(thoughtModel);
	}

	@Override
	protected SortedSet<KickPositionEstimation> evaluatePositions()
	{
		SortedSet<KickPositionEstimation> evaluatedPositions = new TreeSet<>();
		Vector3D target = getWiderSector();
		evaluatedPositions.add(new KickPositionEstimation(target, 1));

		return evaluatedPositions;
	}

	public Vector3D getWiderSector()
	{
		IBall ball = worldModel.getBall();
		Vector3D otherGoalPosition = worldModel.getOtherGoalPosition();
		float goalHalfWidth = worldModel.goalHalfWidth() - 0.05f;
		Vector3D leftGoalPost = otherGoalPosition.add(new Vector3D(0, goalHalfWidth, 0));
		Vector3D rightGoalPost = otherGoalPosition.subtract(new Vector3D(0, goalHalfWidth, 0));

		IPlayer opponentGoalie = thoughtModel.getOpponentAtBall();
		if (opponentGoalie == null) {
			// no goalie there
			return otherGoalPosition;
		}

		Angle ballGoalieAngle = ball.getDirectionTo(opponentGoalie.getPosition());
		Angle leftPostAngle = ball.getDirectionTo(leftGoalPost);
		Angle rightPostAngle = ball.getDirectionTo(rightGoalPost);
		if (ballGoalieAngle.isLeftOf(leftPostAngle) || ballGoalieAngle.isRightOf(rightPostAngle)) {
			// goalie does not block goal
			return otherGoalPosition;
		}
		Angle deltaRight = ballGoalieAngle.subtract(rightPostAngle);
		Angle deltaLeft = leftPostAngle.subtract(ballGoalieAngle);

		if (Math.abs(deltaRight.degrees()) > Math.abs(deltaLeft.degrees())) {
			return otherGoalPosition.add(new Vector3D(0, -0.5, 0));
		} else {
			return otherGoalPosition.add(new Vector3D(0, 0.5, 0));
		}
	}
}
