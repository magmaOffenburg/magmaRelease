/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class Allrounder extends Role
{
	private final int deviation;

	private final IRoboCupThoughtModel thoughtModel;

	private final Vector2D defaultPosition;

	public Allrounder(String name, IRoboCupThoughtModel thoughtModel, Vector2D defaultPosition, float priority,
			double minX, double maxX)
	{
		super(thoughtModel.getWorldModel(), name, priority, minX, maxX);
		this.deviation = (int) (worldModel.fieldHalfWidth() / 2);
		this.thoughtModel = thoughtModel;
		this.defaultPosition = defaultPosition;
	}

	@Override
	protected IPose2D determinePosition()
	{
		double ballX = worldModel.getBall().getPosition().getX();

		Vector2D position = new Vector2D(keepXLimits(defaultPosition.getX() + ballX), defaultPosition.getY());
		return determinePosition(position);
	}

	private Pose2D determinePosition(Vector2D defaultPosition)
	{
		List<Vector2D> potentialPositions = new ArrayList<>();
		potentialPositions.add(defaultPosition);

		for (int i = 1; i < deviation; i++) {
			potentialPositions.add(new Vector2D(defaultPosition.getX(), defaultPosition.getY() + i));
			potentialPositions.add(new Vector2D(defaultPosition.getX(), defaultPosition.getY() - i));
		}

		double maxUtility = Double.NEGATIVE_INFINITY;
		Pose2D targetPos = null;

		for (Vector2D position : potentialPositions) {
			double utility = calculateUtility(position);
			if (utility > maxUtility) {
				maxUtility = utility;
				targetPos = new Pose2D(position);
			}
		}

		return targetPos;
	}

	private double calculateUtility(Vector2D position)
	{
		double distanceToGoal = worldModel.getOtherGoalPosition().distance(VectorUtils.to3D(position));

		List<IPlayer> opponents = thoughtModel.getOpponentsAtBallList();
		IBall ball = worldModel.getBall();

		double distanceToBall = ball.getDistanceToXY(position);

		double minDistanceToOpponent = Double.POSITIVE_INFINITY;

		for (IPlayer player : opponents) {
			double distance = player.getDistanceToXY(position);
			if (distance < minDistanceToOpponent) {
				minDistanceToOpponent = distance;
			}
		}

		if (minDistanceToOpponent > 5) {
			minDistanceToOpponent = 5;
		}

		if (distanceToBall > 10) {
			distanceToBall = 10;
		}

		if (distanceToGoal > 5) {
			distanceToGoal = 5;
		}

		boolean opponenInTheWay = opponents.stream().anyMatch(opponent
				-> ball.getDistanceToXY(position) > opponent.getDistanceToXY(position) &&
						   Geometry.getDistanceToLine(position, VectorUtils.to2D(ball.getPosition()),
								   VectorUtils.to2D(opponent.getPosition())) < 0.5);

		return -distanceToGoal - (opponenInTheWay ? 10 : 0) + minDistanceToOpponent - distanceToBall;
	}
}
