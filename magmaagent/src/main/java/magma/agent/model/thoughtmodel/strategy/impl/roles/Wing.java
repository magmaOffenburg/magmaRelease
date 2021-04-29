/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import hso.autonomy.util.geometry.Pose2D;
import java.util.Optional;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stephan Kammerer
 */
public class Wing extends Role implements IWaitForOpponentGoalKick
{
	// how far in front of the ball
	private final float xOffset;

	public Wing(String name, IRoboCupWorldModel worldModel, float xOffset, float priority)
	{
		super(worldModel, name, priority,			 //
				-1.5,								 // minX
				worldModel.fieldHalfLength() - 2.8); // maxX

		this.xOffset = xOffset;
	}

	@Override
	protected Pose2D determinePosition()
	{
		Optional<Vector3D> waitForOpponentGoalKickPosition = waitForOpponentGoalKick(worldModel, false);
		if (waitForOpponentGoalKickPosition.isPresent()) {
			return new Pose2D(waitForOpponentGoalKickPosition.get(),
					calculateBallDirection(waitForOpponentGoalKickPosition.get()));
		}

		Vector3D ballPos = worldModel.getBall().getPosition();

		// we try to be positioned relative to the ball goal distance x
		double targetX = ballPos.getX() + xOffset;
		double targetY = 0;

		double value = 0;
		double ballPosX = ballPos.getX();
		if (ballPosX < -4) {
			targetX += 2;
			value = 4;
		} else if (ballPosX < 2) {
			value = 2;
		} else {
			value = 0;
		}

		double ballPosY = ballPos.getY();

		if (ballPosY < -0.2) {
			targetY = -value;
		} else if (ballPosY > 0.2) {
			targetY = value;
		}

		targetX = keepXLimits(targetX);
		Vector3D target = new Vector3D(targetX, targetY, 0);

		return new Pose2D(target);
	}
}
