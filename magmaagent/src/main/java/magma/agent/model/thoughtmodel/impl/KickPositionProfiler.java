/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.KickPositionEstimation;
import magma.agent.model.worldmodel.IPlayer;
import magma.agent.model.worldmodel.IRCSoccerField;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author kdorer
 */
public class KickPositionProfiler extends KickPositionProfilerBase
{
	public static final float DEFAULT_AREA_BORDER = 2;

	protected Area2D.Float playableArea;

	protected final float maxEvaluationDistance;

	protected final double opponentBlockDistance;

	protected boolean goalKick;

	public KickPositionProfiler(IRoboCupThoughtModel thoughtModel)
	{
		this(thoughtModel, 16, DEFAULT_AREA_BORDER, 0.5);
	}

	public KickPositionProfiler(IRoboCupThoughtModel thoughtModel, float maxEvaluationDistance,
			float kickableAreaBorder, double opponentBlockDistance)
	{
		super(thoughtModel);
		IRCSoccerField soccerPitch = worldModel.getMap();
		playableArea = new Area2D
							   .Float(-soccerPitch.fieldHalfLength(), soccerPitch.fieldHalfLength(),
									   -soccerPitch.fieldHalfWidth(), soccerPitch.fieldHalfWidth())
							   .applyBorder(kickableAreaBorder);
		this.maxEvaluationDistance = maxEvaluationDistance;
		this.opponentBlockDistance = opponentBlockDistance;
	}

	public void setPlayableArea(Area2D.Float playableArea)
	{
		this.playableArea = playableArea;
	}

	@Override
	public SortedSet<KickPositionEstimation> evaluatePositions()
	{
		SortedSet<KickPositionEstimation> result = doEstimate();

		if (result.isEmpty()) {
			result.add(new KickPositionEstimation(Geometry.getPointOnLineAbsoluteEnd(worldModel.getOtherGoalPosition(),
														  worldModel.getBall().getPosition(), 1),
					1));
		}

		return result;
	}

	protected SortedSet<KickPositionEstimation> doEstimate()
	{
		SortedSet<KickPositionEstimation> result = new TreeSet<>();
		goalKick = false;

		IRoboCupWorldModel worldModel = thoughtModel.getWorldModel();
		Vector3D ballPosition = worldModel.getBall().getPosition();
		Vector2D ballPosition2D = VectorUtils.to2D(ballPosition);

		final int angleStep = 5;
		final float initialDistance = 1;
		final float maxDistance = getMaxEvaluationDistance();
		final float distanceStep = 1;

		for (int angle = -90; angle < 90; angle += angleStep) {
			if (initialDistance > maxDistance) {
				continue;
			}

			final Vector3D directionVector = new Vector3D(Angle.deg(angle).radians(), 0);

			for (float distance = initialDistance; distance < maxDistance; distance += distanceStep) {
				Vector3D position = ballPosition.add(directionVector.scalarMultiply(distance));

				double minDistanceToOpponent = calculateDistanceToNearestPlayer(position, false);
				if (minDistanceToOpponent < opponentBlockDistance) {
					// the point should not be added nor further points on the ray
					break;
				}

				double minDistanceToOwnPlayer = calculateDistanceToNearestPlayer(position, true);
				if (minDistanceToOpponent < minDistanceToOwnPlayer) {
					// the point should not be added
					continue;
				}

				if (!playableArea.contains(position)) {
					Vector2D intersection =
							worldModel.goalLineIntersection(ballPosition2D, VectorUtils.to2D(position), 0.1);
					if (intersection == null || ballPosition.getX() > position.getX()) {
						// we left the playable area
						if (!playableArea.contains(ballPosition)) {
							continue;
						} else {
							// if the ball is in playable area, all further points are not
							break;
						}
					}
				}

				float utility = estimateUtility(position, minDistanceToOwnPlayer, minDistanceToOpponent);
				KickPositionEstimation estimate = new KickPositionEstimation(position, utility);
				result.add(estimate);
			}
		}

		return result;
	}

	private double calculateDistanceToNearestPlayer(Vector3D position, boolean ownTeam)
	{
		double minDistance = Double.MAX_VALUE;
		List<IPlayer> allPlayers = new ArrayList<>(worldModel.getVisiblePlayers());
		allPlayers.add(worldModel.getThisPlayer());
		for (IPlayer player : allPlayers) {
			if (player.isOwnTeam() == ownTeam) {
				double distance = player.getDistanceToXY(position);
				if (distance < minDistance) {
					minDistance = distance;
				}
			}
		}
		return minDistance;
	}

	protected float getMaxEvaluationDistance()
	{
		if (thoughtModel.getOpponentsAtMeList().isEmpty()) {
			// No opponents around...
			return maxEvaluationDistance;
		}

		// If we have opponents
		Vector3D closestOpponentPos = thoughtModel.getOpponentsAtMeList().get(0).getPosition();
		double distanceToOpponent = worldModel.getThisPlayer().getDistanceToXY(closestOpponentPos);

		// evaluate far if no opponent close or we have a kick
		if (distanceToOpponent < 1.5 && !worldModel.getGameState().isOwnKick()) {
			return maxEvaluationDistance / 2;
		}
		return maxEvaluationDistance;
	}

	protected float estimateUtility(Vector3D position, double minDistanceToOwnPlayer, double minDistanceToOpponent)
	{
		float utility = 0;
		utility += getOwnPlayerUtility(minDistanceToOwnPlayer);
		utility += getOpponentPlayerUtility(minDistanceToOpponent);
		utility += getDistanceUtility(position);
		return utility;
	}

	protected float getOwnPlayerUtility(double minDistance)
	{
		int maxEvaluation = 3;
		if (minDistance > maxEvaluation) {
			return -10;
		}
		if (minDistance < 1) {
			return maxEvaluation;
		}
		return (float) (maxEvaluation - minDistance + 1);
	}

	protected float getOpponentPlayerUtility(double minDistance)
	{
		int maxEvaluation = 5;
		if (minDistance > maxEvaluation) {
			return 0;
		}
		return (float) -(maxEvaluation - minDistance);
	}

	protected float getDistanceUtility(Vector3D position)
	{
		// we prefer long passes over short passes
		IMoveableObject ball = thoughtModel.getWorldModel().getBall();
		float fieldHalfLength = thoughtModel.getWorldModel().fieldHalfLength();
		double distance = Vector3D.distance(ball.getPosition(), position);
		if (distance < (1 + fieldHalfLength / 4)) {
			return -5;
		}
		return (float) (distance / fieldHalfLength);
	}
}