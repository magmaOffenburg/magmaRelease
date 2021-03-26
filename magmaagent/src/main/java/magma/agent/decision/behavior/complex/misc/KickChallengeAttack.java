/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.misc;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.List;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.complex.walk.WalkToPosition;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickChallengeAttack extends Attack
{
	public KickChallengeAttack(IThoughtModel thoughtModel, BehaviorMap behaviors, List<String> availableKicks)
	{
		super(IBehaviorConstants.KICK_CHALLENGE_ATTACK, thoughtModel, behaviors, availableKicks);
	}

	@Override
	protected double getIntendedKickDistance()
	{
		return getWorldModel().getBall().getDistanceToXY(Vector3D.ZERO);
	}

	@Override
	protected Angle getIntendedKickDirection()
	{
		return getWorldModel().getBall().getDirectionTo(Vector3D.ZERO);
	}

	@Override
	protected float getKickPower()
	{
		double distance = getWorldModel().getBall().getDistanceToXY(Vector3D.ZERO);
		if (distance <= 3.5) {
			return 0.43f;
		} else if (distance <= 4.5) {
			return 0.50f;
		} else if (distance <= 5.5) {
			return 0.57f;
		} else if (distance <= 6.5) {
			return 0.70f;
		} else if (distance <= 7.5) {
			return 0.77f;
		}
		return super.getKickPower();
	}

	@Override
	protected IBehavior walkToPosition(
			PoseSpeed2D target, boolean avoidObstacles, int maxSpeedForward, double slowDownDistance)
	{
		return ((WalkToPosition) behaviors.get(IBehaviorConstants.WALK_TO_POSITION))
				.setPosition(target, 40, avoidObstacles, slowDownDistance);
	}
}
