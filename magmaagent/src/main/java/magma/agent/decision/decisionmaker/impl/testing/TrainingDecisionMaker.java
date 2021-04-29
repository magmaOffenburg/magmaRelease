/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.testing;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.IBeam;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IBall;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Decision maker used to trigger training behaviors. Feel free to change it for
 * your own convenience.
 *
 * @author Klaus Dorer
 */
public class TrainingDecisionMaker extends SoccerDecisionMaker
{
	protected boolean haveBeamed;

	// private boolean doneGetReady;

	public TrainingDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
		haveBeamed = false;
	}

	@Override
	protected String beamHome()
	{
		if (!haveBeamed) {
			haveBeamed = true;
			((IBeam) behaviors.get(IBehaviorConstants.BEAM_TO_POSITION)).setPose(new Pose2D(-2f, 0.0f));
			return IBehaviorConstants.BEAM_TO_POSITION;
		}
		return null;
	}

	@Override
	protected String reactToGameEnd()
	{
		return null;
	}

	@Override
	protected String waitForGameStart()
	{
		return null;
	}

	@Override
	protected String searchBall()
	{
		return null;
	}

	@Override
	protected String getReady()
	{
		return null;
	}

	@Override
	protected String move()
	{
		IBaseWalk behavior = (IBaseWalk) behaviors.get(IBehaviorConstants.IK_WALK);
		if ((numberOfDecisions / 200) % 2 == 0) {
			behavior.setMovement(100, 0, Angle.ZERO);
		} else {
			behavior.setMovement(0, 0, Angle.ZERO);
		}
		return IBehaviorConstants.IK_WALK;

		// return dribbleToBall();
	}

	protected String dribbleToBall()
	{
		IBall ball = getWorldModel().getBall();
		Vector3D goal = getWorldModel().getOtherGoalPosition();
		Angle dirGoal = ball.getDirectionTo(goal);
		Vector3D pos = Geometry.getPointOnLineAbsoluteEnd(goal, ball.getPosition(), -0.1);

		double maxSpeed = 30;
		if (getWorldModel().getThisPlayer().getDistanceToXY(ball) > 0.6) {
			maxSpeed = 90;
		}

		WalkToPosition walk = (WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION);
		walk.setPosition(new PoseSpeed2D(new Pose2D(pos, dirGoal), new Vector2D(0.2, 0)), maxSpeed, true, 0.05);
		return IBehaviorConstants.WALK_TO_POSITION;
	}
}
