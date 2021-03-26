/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.base;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.awt.geom.Line2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.agentmodel.IRoboCupAgentModel;
import magma.agent.model.worldmodel.IBall;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IRoboCupWorldModel.BallPassing;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KeepEstimator
{
	private final IRoboCupWorldModel worldModel;

	private final IRoboCupAgentModel agentModel;

	public KeepEstimator(IThoughtModel thoughtModel)
	{
		worldModel = (IRoboCupWorldModel) thoughtModel.getWorldModel();
		agentModel = (IRoboCupAgentModel) thoughtModel.getAgentModel();
	}

	public String decideKeepBehavior()
	{
		// don't try to keep if the ball will just bounce over us - better chances if we
		// stand still
		IBall ball = worldModel.getBall();
		if (ball.isBouncing(worldModel.getGlobalTime())) {
			return null;
		}

		Vector3D[] futurePositions = ball.getFuturePositions(agentModel.getGoalPredictionTime());
		Vector3D goalPosition = worldModel.getOwnGoalPosition();
		Vector3D closestFutureBallPositionToGoal = ball.getPosition();

		for (Vector3D futurePosition : futurePositions) {
			if (futurePosition.distance(goalPosition) < closestFutureBallPositionToGoal.distance(goalPosition)) {
				closestFutureBallPositionToGoal = futurePosition;
			} else {
				break;
			}
		}

		if (!isPossibleGoal(closestFutureBallPositionToGoal)) {
			return null;
		}

		return decideKeepBehavior(closestFutureBallPositionToGoal);
	}

	private String decideKeepBehavior(Vector3D futureBallPosition)
	{
		BallPassing ballIsPassing = worldModel.ballIsPassing(futureBallPosition);
		if (futureBallPosition.getZ() > 0.2) {
			return null;
		}

		// TODO add a new case
		switch (ballIsPassing) {
		case CENTER:
		case SHORT_LEFT:
		case SHORT_RIGHT:
			return IBehaviorConstants.KEEP_CENTER;
		case FAR_LEFT:
			return IBehaviorConstants.KEEP_SIDE.LEFT;
		case FAR_RIGHT:
			return IBehaviorConstants.KEEP_SIDE.RIGHT;
		case UNREACHABLE:
		default:
			return null;
		}
	}

	public boolean isBallKeepable()
	{
		Vector3D futureBallPosition = worldModel.getBall().getFuturePosition(agentModel.getGoalPredictionTime());

		return decideKeepBehavior(futureBallPosition) != null;
	}

	public boolean isPossibleGoal()
	{
		Vector3D futureBallPosition = worldModel.getBall().getFuturePosition(agentModel.getGoalPredictionTime());
		return isPossibleGoal(futureBallPosition);
	}

	private boolean isPossibleGoal(Vector3D futureBallPosition)
	{
		Vector3D ballPosition = worldModel.getBall().getPosition();

		Line2D ballLine = new Line2D.Double(
				futureBallPosition.getX(), futureBallPosition.getY(), ballPosition.getX(), ballPosition.getY());

		double x1 = -worldModel.fieldHalfLength();
		double y1 = worldModel.goalHalfWidth();

		double x2 = -worldModel.fieldHalfLength();
		double y2 = -worldModel.goalHalfWidth();

		final double X_EXTENSION = 0.3;
		final double Y_EXTENSION = 0.4;
		Line2D goalLine = new Line2D.Double(x1 + X_EXTENSION, y1 + Y_EXTENSION, x2 + X_EXTENSION, y2 - Y_EXTENSION);

		return goalLine.intersectsLine(ballLine);
	}
}
