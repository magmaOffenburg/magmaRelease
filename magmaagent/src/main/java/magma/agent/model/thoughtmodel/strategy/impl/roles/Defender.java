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

/**
 * This role primarily protects our goal. We try to be positioned relative to
 * the ball but our defensive player stays in the back.
 *
 * @author Stephan Kammerer
 */
public class Defender extends Role
{
	public Defender(String name, IRoboCupWorldModel worldModel, float priority)
	{
		super(worldModel, name, priority,			//
				-worldModel.fieldHalfLength(),		// minX
				-worldModel.fieldHalfLength() + 5); // maxX
	}

	@Override
	protected IPose2D determinePosition()
	{
		Vector3D ownGoalPos = worldModel.getOwnGoalPosition();
		Vector3D ballPos = worldModel.getBall().getPosition();

		double deltaBallGoalX = Math.abs(ownGoalPos.getX() - ballPos.getX());

		double targetY = ballPos.getY() / 3;
		targetY = ValueUtil.limitAbs(targetY, 3.4);

		double targetX = keepXLimits(minX + 0.3 * deltaBallGoalX);

		Vector3D target = new Vector3D(targetX, targetY, 0);
		Angle ballDirection = calculateBallDirection(target);
		return new Pose2D(target, ballDirection);
	}

	@Override
	protected IPose2D avoidGoal(IPose2D target)
	{
		return keepMinDistanceToGoal(target, 1.9);
	}
}
