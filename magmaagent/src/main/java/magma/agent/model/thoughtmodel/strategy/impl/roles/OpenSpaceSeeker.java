/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import hso.autonomy.util.misc.ValueUtil;
import java.util.ArrayList;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class OpenSpaceSeeker extends Role
{
	private final Role defaultRole;

	private IRoboCupThoughtModel thoughtModel;

	public OpenSpaceSeeker(Role defaultRole, IRoboCupThoughtModel thoughtModel)
	{
		super(defaultRole.worldModel, defaultRole.name + "[OpenSpaceSeeker]", defaultRole.basePriority,
				defaultRole.minX, defaultRole.maxX);

		this.defaultRole = defaultRole;
		this.thoughtModel = thoughtModel;
	}

	@Override
	protected IPose2D determinePosition()
	{
		IPose2D defaultPosition = defaultRole.determinePosition();

		if (worldModel.getGameState().isOpponentKick()) {
			return defaultPosition;
		}

		Vector2D defaultRolePosition = defaultPosition.getPosition();

		Vector2D ballPosition2D = VectorUtils.to2D(worldModel.getBall().getPosition());

		List<IPlayer> opponents = thoughtModel.getOpponentsAtMeList();

		boolean opponenInTheWay = opponents.stream().anyMatch(opponent -> {
			Vector2D opponnentPosition2D = VectorUtils.to2D(opponent.getPosition());
			return defaultRolePosition.distance(ballPosition2D) > defaultRolePosition.distance(opponnentPosition2D) &&
					Geometry.getDistanceToLine(defaultRolePosition, ballPosition2D, opponnentPosition2D) < 0.5;
		});

		if (opponenInTheWay) {
			List<Vector2D> potentialPositions = new ArrayList<>();
			for (int i = 1; i < 4; i++) {
				potentialPositions.add(new Vector2D(defaultRolePosition.getX(),
						ValueUtil.limitAbs(defaultRolePosition.getY() + i, worldModel.fieldHalfWidth())));
				potentialPositions.add(new Vector2D(defaultRolePosition.getX(),
						ValueUtil.limitAbs(defaultRolePosition.getY() - i, worldModel.fieldHalfWidth())));
			}
			double minUtility = Double.MAX_VALUE;
			Vector2D bestPosition = defaultRolePosition;
			for (Vector2D position : potentialPositions) {
				double utility = calculateUtility(position);
				if (utility < minUtility) {
					minUtility = utility;
					bestPosition = position;
				}
			}
			return new Pose2D(bestPosition);
		}
		return defaultPosition;
	}

	private double calculateUtility(Vector2D position)
	{
		double distanceToGoal = worldModel.getOtherGoalPosition().distance(VectorUtils.to3D(position));

		double minDistanceToOpponent = Double.MAX_VALUE;
		for (IPlayer player : thoughtModel.getOpponentsAtMeList()) {
			double distance = player.getDistanceToXY(position);
			if (distance < minDistanceToOpponent) {
				minDistanceToOpponent = distance;
			}
		}

		return distanceToGoal - minDistanceToOpponent;
	}
}
