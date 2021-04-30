/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.SortedSet;
import java.util.TreeSet;
import magma.agent.UglyConstants;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.KickPositionEstimation;
import magma.agent.model.worldmodel.GameState;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author kdorer
 */
public class KickPositionProfilerGoal extends KickPositionProfiler
{
	public static final float GOAL_KICK_DISTANCE = 9;

	protected float goalKickDistance;

	public KickPositionProfilerGoal(IRoboCupThoughtModel thoughtModel)
	{
		super(thoughtModel);
		this.goalKickDistance = GOAL_KICK_DISTANCE;
	}

	@Override
	protected SortedSet<KickPositionEstimation> doEstimate()
	{
		SortedSet<KickPositionEstimation> result = new TreeSet<>();
		Vector3D otherGoalPosition = worldModel.getOtherGoalPosition();
		Vector3D ballPosition = worldModel.getBall().getPosition();
		Vector3D position =
				otherGoalPosition.add(otherGoalPosition.subtract(ballPosition).normalize().scalarMultiply(5));
		Angle ballGoalAngle = VectorUtils.getDirectionTo(ballPosition, otherGoalPosition);
		if (ballGoalAngle.degrees() > 80 || ballGoalAngle.degrees() < -80) {
			// we would likely miss the goal if we try to score from here
			result.add(new KickPositionEstimation(
					UglyConstants.thinClient ? position : otherGoalPosition.add(new Vector3D(-2, 0, 0)), 1));
		} else if (worldModel.getGameState() == GameState.OWN_KICK_OFF) {
			// position at kickoff
			result.add(new KickPositionEstimation(thoughtModel.getKickOffTargetPosition(), 1));
		} else {
			double ballGoalDistance = ballPosition.distance(otherGoalPosition);
			// check if we are in goal kick distance
			if (ballGoalDistance < goalKickDistance && thoughtModel.isAllowedToScore()) {
				// we focus on goal kicks if close to the goal
				Vector3D goalTargetPosition =
						Geometry.getPointOnLineAbsoluteEnd(otherGoalPosition, ballPosition, ballGoalDistance + 3);
				result.add(new KickPositionEstimation(UglyConstants.thinClient ? position : goalTargetPosition, 1));
				goalKick = true;
			} else {
				return super.doEstimate();
			}
		}

		return result;
	}

	@Override
	protected float estimateUtility(Vector3D position, double minDistanceToOwnPlayer, double minDistanceToOpponent)
	{
		float utility = super.estimateUtility(position, minDistanceToOwnPlayer, minDistanceToOpponent);
		utility += getGoalUtility(position);
		return utility;
	}

	protected float getGoalUtility(Vector3D position)
	{
		float fieldHalfLength = worldModel.fieldHalfLength();
		Vector3D targetPos = worldModel.getOtherGoalPosition();
		Vector3D ballPos = worldModel.getBall().getPosition();
		if (ballPos.getX() < -4) {
			// we are in defense
			double newX = ballPos.getX() + 14;
			double newY = worldModel.fieldHalfWidth() - 5;
			if (ballPos.getY() < -0.2) {
				// kick towards the right side
				targetPos = new Vector3D(newX, -newY, 0);
			} else if (ballPos.getY() > 0.2) {
				// kick towards the left side
				targetPos = new Vector3D(newX, newY, 0);
			}
		}
		double distance = Vector3D.distance(targetPos, position);

		return (float) ((fieldHalfLength - distance) * 0.8);
	}

	@Override
	protected float getDistanceUtility(Vector3D position)
	{
		// we prefer short passes for kick
		Vector3D otherGoalPosition = worldModel.getOtherGoalPosition();
		float fieldHalfLength = worldModel.fieldHalfLength();
		Vector3D ballPosition = worldModel.getBall().getPosition();
		double distanceGoal = Vector3D.distance(ballPosition, otherGoalPosition);

		if (distanceGoal < goalKickDistance) {
			double distance = Vector3D.distance(ballPosition, position);
			return (float) ((fieldHalfLength - distance) / fieldHalfLength);
		} else {
			return super.getDistanceUtility(position);
		}
	}
}
