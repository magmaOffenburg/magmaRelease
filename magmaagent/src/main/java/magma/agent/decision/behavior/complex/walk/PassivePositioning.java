/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.RoboCupSingleComplexBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Behavior we use when we're not actively running to the ball with Attack.
 *
 * @author Klaus Dorer
 */
public class PassivePositioning extends RoboCupSingleComplexBehavior
{
	public PassivePositioning(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.PASSIVE_POSITIONING, thoughtModel, behaviors);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IPose2D pose = getThoughtModel().getRole().getTargetPose();
		Angle direction = pose.getAngle();
		Vector2D newPosition = pose.getPosition();

		WalkToPosition walkToPosition = (WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION);
		Pose2D newPose = new Pose2D(newPosition, direction);
		return walkToPosition.setPosition(
				new PoseSpeed2D(newPose, Vector2D.ZERO), 90, true, true, 0.8, IKDynamicWalkMovement.NAME_LOW_ACC);
	}
}
