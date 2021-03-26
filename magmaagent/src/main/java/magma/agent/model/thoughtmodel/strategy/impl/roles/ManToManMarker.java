/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class ManToManMarker extends Role
{
	private static final float DISTANCE_TO_OPPONENT = 1.5f;

	private static final float PLAYER_IS_MARKED_DISTANCE = 2.0f;

	private static final float MIN_GOAL_DISTANCE = 1.0f;

	/**
	 * Offset to the right/left of ball to goal line when player inside the penalty area
	 */
	private static final float ORTHOGONAL_OFFSET = 0.7f;

	private final Role defaultRole;

	private final Area2D.Float criticalArea;

	public ManToManMarker(Role defaultRole, FieldArea fieldArea)
	{
		super(defaultRole.worldModel, defaultRole.name + "[" + fieldArea + "-MARKER]", defaultRole.basePriority,
				defaultRole.minX, defaultRole.maxX);

		this.defaultRole = defaultRole;
		criticalArea = createCriticalArea(fieldArea);
	}

	private Area2D.Float createCriticalArea(FieldArea fieldArea)
	{
		switch (fieldArea) {
		case UPPER:
			return new Area2D.Float(-worldModel.fieldHalfLength(), 1, 0, worldModel.fieldHalfWidth());
		case LOWER:
			return new Area2D.Float(-worldModel.fieldHalfLength(), 1, -worldModel.fieldHalfWidth(), 0);
		default:
			throw new IllegalArgumentException("unexpected enum value: " + fieldArea);
		}
	}

	@Override
	protected IPose2D determinePosition()
	{
		List<IPlayer> ownPlayers = new ArrayList<>();
		List<IPlayer> opposingPlayers = new ArrayList<>();

		for (IPlayer visiblePlayer : worldModel.getVisiblePlayers()) {
			if (visiblePlayer.isOwnTeam()) {
				// ignore the goalie
				if (!visiblePlayer.isGoalie()) {
					ownPlayers.add(visiblePlayer);
				}
			} else {
				opposingPlayers.add(visiblePlayer);
			}
		}

		double minDistanceToGoal = Double.MAX_VALUE;
		IPlayer opponentToMark = null;
		Vector3D ownGoalPosition = worldModel.getOwnGoalPosition();

		for (IPlayer opposingPlayer : opposingPlayers) {
			double distanceToGoal = opposingPlayer.getDistanceToXY(ownGoalPosition);
			if (criticalArea.contains(opposingPlayer.getPosition()) &&
					ownPlayers.stream().noneMatch(ownPlayer
							-> ownPlayer.getDistanceToXY(opposingPlayer) < PLAYER_IS_MARKED_DISTANCE &&
									   ownPlayer.getDistanceToXY(ownGoalPosition) < distanceToGoal)) {
				if (distanceToGoal < minDistanceToGoal) {
					opponentToMark = opposingPlayer;
					minDistanceToGoal = distanceToGoal;
				}
			}
		}

		if (opponentToMark != null) {
			// calculate position between goal and opponent with DISTANCE_TO_OPPONENT
			Vector3D position = Geometry.getPointOnLineAbsoluteEnd(
					ownGoalPosition, opponentToMark.getPosition(), DISTANCE_TO_OPPONENT);

			// set min goal distance if player is closer than MIN_GOAL_DISTANCE
			if (position.distance(ownGoalPosition) < MIN_GOAL_DISTANCE) {
				position = setMinDistanceToGoal(opponentToMark.getPosition(), ownGoalPosition, MIN_GOAL_DISTANCE);
			}
			// position player according to ball position if inside penalty area
			// UNCOMMENT FOR ISSUE #122
			position = calculatePositionIfInPenaltyArea(ownGoalPosition, position);

			Angle ballDirection = calculateBallDirection(position);
			return new Pose2D(position, ballDirection);
		}

		// no players to mark, use default role
		return defaultRole.determinePosition();
	}

	private Vector3D calculatePositionIfInPenaltyArea(Vector3D ownGoalPosition, Vector3D position)
	{
		float penaltyLineY = worldModel.penaltyHalfLength() + worldModel.goalHalfWidth();
		float penaltyLineX = -worldModel.fieldHalfLength() + worldModel.penaltyWidth();

		// if player is inside penalty area
		if (Math.abs(position.getY()) < penaltyLineY && position.getX() < penaltyLineX) {
			Vector3D ballPosition = worldModel.getBall().getPosition();
			Angle goalBallAngle = VectorUtils.getDirectionTo(worldModel.getOwnGoalPosition(), ballPosition);
			double orthogonalOffset =
					Geometry.getLinearFuzzyValue(0, 60, false, Math.abs(goalBallAngle.degrees())) * ORTHOGONAL_OFFSET;

			// min x-distance from field line
			double fieldLineOffset = worldModel.fieldHalfLength() - 0.3f;

			// if ball in upper field half negate offset
			if (ballPosition.getY() > 0) {
				orthogonalOffset = -orthogonalOffset;
			}

			position = Geometry.getPointOnOrthogonal2D(ballPosition, ownGoalPosition, 2, orthogonalOffset);

			// if calculated position is outside field
			if (Math.abs(position.getX()) > fieldLineOffset) {
				position = new Pose2D(-fieldLineOffset, position.getY()).getPosition3D();
			}
		}
		return position;
	}

	private Vector3D setMinDistanceToGoal(Vector3D referencePosition, Vector3D ownGoalPosition, float minDistanceToGoal)
	{
		return Geometry.getPointOnLineAbsoluteEnd(referencePosition, ownGoalPosition, minDistanceToGoal);
	}

	@Override
	protected IPose2D avoidGoal(IPose2D target)
	{
		// only UPPER marker enters penalty area
		//      if (fieldArea != FieldArea.UPPER) {
		//          return super.avoidGoal(target);
		//      }

		// follow opponent into penalty area if necessary
		return target;
	}
}
