/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.misc.ValueUtil;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class DefensiveMidfielder extends Role
{
	public DefensiveMidfielder(String name, IRoboCupWorldModel worldModel, float safetyDistanceOffset, float priority)
	{
		super(worldModel, name, priority, -worldModel.fieldHalfLength(), 0, safetyDistanceOffset);
	}

	@Override
	protected IPose2D determinePosition()
	{
		Vector3D ballPos = worldModel.getBall().getPosition();
		Vector3D ownGoalPos = worldModel.getOwnGoalPosition();

		double targetX = (ownGoalPos.getX() + ballPos.getX()) / 2;
		double targetY = ballPos.getY() - 1;
		targetX = keepXLimits(targetX);
		targetY = ValueUtil.limitAbs(targetY, worldModel.fieldHalfWidth() - 6);

		Vector3D target = new Vector3D(targetX, targetY, 0);
		Angle ballDirection = calculateBallDirection(target);

		return new Pose2D(target, ballDirection);
	}

	@Override
	protected IPose2D avoidGoal(IPose2D target)
	{
		return keepMinDistanceToGoal(target, 4);
	}
}
