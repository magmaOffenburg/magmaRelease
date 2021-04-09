/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import java.util.ArrayList;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class ReactToGameState extends Role
{
	private final IRoboCupThoughtModel thoughtModel;

	private final Area2D.Float ownCornerKickArea;

	private final Area2D.Float opponentCornerKickArea;

	private final Role defaultRole;

	private final FieldArea fieldArea;

	private float opponentKickOffTime;

	public ReactToGameState(Role defaultRole, IRoboCupThoughtModel thoughtModel, FieldArea fieldArea)
	{
		super(defaultRole.worldModel, defaultRole.name, defaultRole.basePriority, defaultRole.minX, defaultRole.maxX);

		this.thoughtModel = thoughtModel;
		this.defaultRole = defaultRole;
		this.fieldArea = fieldArea;

		ownCornerKickArea = createOwnCornerKickArea(fieldArea);
		opponentCornerKickArea = createOpponentCornerKickArea(fieldArea);
	}

	private Area2D.Float createOwnCornerKickArea(FieldArea fieldArea)
	{
		switch (fieldArea) {
		case UPPER:
			return new Area2D.Float(worldModel.fieldHalfLength() - 4.5, worldModel.fieldHalfLength() - 1.5, 0,
					worldModel.fieldHalfWidth() / 3);
		case LOWER:
			return new Area2D.Float(worldModel.fieldHalfLength() - 4.5, worldModel.fieldHalfLength() - 1.5,
					-worldModel.fieldHalfWidth() / 3, 0);
		}

		throw new IllegalArgumentException("unexpected enum value: " + fieldArea);
	}

	private Area2D.Float createOpponentCornerKickArea(FieldArea fieldArea)
	{
		switch (fieldArea) {
		case UPPER:
			return new Area2D.Float(-worldModel.fieldHalfLength() + 2, -worldModel.fieldHalfLength() + 4.5, 0,
					worldModel.fieldHalfWidth() / 3);
		case LOWER:
			return new Area2D.Float(-worldModel.fieldHalfLength() + 2, -worldModel.fieldHalfLength() + 4.5,
					-worldModel.fieldHalfWidth() / 3, 0);
		}

		throw new IllegalArgumentException("unexpected enum value: " + fieldArea);
	}

	@Override
	protected IPose2D determinePosition()
	{
		GameState gameState = worldModel.getGameState();

		if (gameState == GameState.OPPONENT_KICK_OFF) {
			opponentKickOffTime = worldModel.getGameTime();
		}

		if (opponentKickOffTime != 0 && worldModel.getGameTime() - opponentKickOffTime < 10 &&
				worldModel.getBall().getPosition().getX() > 0) {
			// opponent might trick us to play too offensively at kick off so just wait some time
			return worldModel.getThisPlayer().getPose2D();
		}

		if (gameState == GameState.OWN_CORNER_KICK) {
			return determinePosition(ownCornerKickArea);
		}

		if (gameState == GameState.OPPONENT_CORNER_KICK) {
			return determinePosition(opponentCornerKickArea);
		}

		if (gameState.isOwnKick() || areWeMuchCloserToBallThanOpponent()) {
			// offensive positioning
			return determinePosition(calculateArea(true));
		}

		if (gameState.isOpponentKick()) {
			// defensive positioning
			return determinePosition(calculateArea(false));
		}

		return defaultRole.determinePosition();
	}

	private boolean areWeMuchCloserToBallThanOpponent()
	{
		if (thoughtModel.getOpponentsAtBallList().isEmpty()) {
			return true;
		}

		IBall ball = worldModel.getBall();
		double minOpponentDistanceToBall = thoughtModel.getOpponentsAtBallList().get(0).getDistanceToXY(ball);
		double minOwnDistanceToBall = thoughtModel.getClosestOwnPlayerAtBall().getDistanceToXY(ball);

		return minOpponentDistanceToBall - minOwnDistanceToBall > 2;
	}

	private Area2D.Float calculateArea(boolean ownKick)
	{
		final double ballX = worldModel.getBall().getPosition().getX();
		final double targetX;
		if (ownKick) {
			targetX = (ballX + worldModel.fieldHalfLength()) / 2;
		} else {
			targetX = (ballX - worldModel.fieldHalfLength()) / 2;
		}

		final double minY;
		final double maxY;
		if (fieldArea == FieldArea.UPPER) {
			minY = 0;
			maxY = worldModel.fieldHalfWidth() / 2;
		} else {
			minY = -worldModel.fieldHalfWidth() / 2;
			maxY = 0;
		}

		return new Area2D.Float(targetX - 2, targetX + 2, minY, maxY);
	}

	private Pose2D determinePosition(Area2D.Float area)
	{
		double maxUtility = Double.MIN_VALUE;
		Pose2D targetPos = null;

		for (float x = area.getMinX(); x <= area.getMaxX(); x++) {
			for (float y = area.getMinY(); y <= area.getMaxY(); y++) {
				double utility = calculateUtility(x, y);
				if (utility > maxUtility) {
					maxUtility = utility;
					targetPos = new Pose2D(x, y);
				}
			}
		}
		return targetPos;
	}

	private double calculateUtility(float x, float y)
	{
		double minDistance = Double.MAX_VALUE;
		List<IPlayer> players = worldModel.getVisiblePlayers();
		for (IPlayer player : players) {
			double distance = player.getPosition().distance(new Vector3D(x, y, 0));
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}
}