/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.goalie;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.base.KeepEstimator;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.agent.model.worldmodel.IThisPlayer;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Pret
 */
public class GoaliePositioning extends RoboCupSingleComplexBehavior
{
	public static final int CYCLES_BEFORE_STAND_STILL = 50;

	private final KeepEstimator keepEstimator;

	private int cycles;

	public GoaliePositioning(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.GOALIE_POSITIONING, thoughtModel, behaviors);
		keepEstimator = new KeepEstimator(thoughtModel);
		cycles = 0;
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IRoboCupWorldModel worldModel = getWorldModel();
		Vector3D ownGoal = worldModel.getOwnGoalPosition();
		IThisPlayer thisPlayer = worldModel.getThisPlayer();

		Vector3D ball = worldModel.getBall().getPosition();
		boolean isPossibleGoal = keepEstimator.isPossibleGoal();

		if (isPossibleGoal) {
			Vector3D[] futureBallPositions =
					worldModel.getBall().getFuturePositions(getThoughtModel().getAgentModel().getGoalPredictionTime());
			for (Vector3D position : futureBallPositions) {
				float length = worldModel.fieldHalfLength();
				if (position.getX() >= -length + 0.3 && position.getX() <= -length + 0.6) {
					ball = position;
					break;
				}
			}
		}

		double distance = worldModel.goalHalfWidth() - 0.3;

		Pose2D result = Geometry.getPointInterpolation(ownGoal, ball, distance);

		float fieldHalfLength = worldModel.fieldHalfLength();
		double xOffsetFromGoal = 0.2;

		result.x = (result.getX() + fieldHalfLength) * 0.5 - fieldHalfLength + xOffsetFromGoal;

		Angle directionToBall = thisPlayer.getBodyDirectionTo(ball);

		if (thisPlayer.getPosition().distance(new Vector3D(result.x, result.y, 0)) < 0.4 &&
				Math.abs(directionToBall.degrees()) < 5) {
			cycles++;
		} else {
			cycles = 0;
		}

		if (cycles > CYCLES_BEFORE_STAND_STILL) {
			return behaviors.get(IBehaviorConstants.GET_READY);
		}

		double slowDownDistance = isPossibleGoal ? 0.2 : 0.8;
		WalkToPosition walkToPosition = (WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION);
		return walkToPosition.setPosition(new PoseSpeed2D(result, Vector2D.ZERO), 90, true, slowDownDistance);
	}
}
