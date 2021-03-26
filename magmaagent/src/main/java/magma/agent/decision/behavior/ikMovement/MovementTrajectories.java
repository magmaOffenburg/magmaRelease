/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.util.geometry.Pose6D;
import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class MovementTrajectories implements Serializable
{
	/** the target poses to the left foot in the CoM-System */
	Pose6D[] leftFootTrajectory;

	/** the target poses to the right foot in the CoM-System */
	Pose6D[] rightFootTrajectory;

	/** the target poses to the left arm in the torso-System */
	Pose6D[] leftArmTrajectory;

	/** the target poses to the right arm in the torso-System */
	Pose6D[] rightArmTrajectory;

	/** the target adjustment factors */
	Vector2D[] adjustmentFactorTrajectory;

	public MovementTrajectories(int movementCycles)
	{
		leftFootTrajectory = new Pose6D[movementCycles];
		leftArmTrajectory = new Pose6D[movementCycles];
		rightFootTrajectory = new Pose6D[movementCycles];
		rightArmTrajectory = new Pose6D[movementCycles];
		adjustmentFactorTrajectory = new Vector2D[movementCycles];
	}

	public Pose6D[] getLeftFootTrajectory()
	{
		return leftFootTrajectory;
	}

	public Pose6D[] getRightFootTrajectory()
	{
		return rightFootTrajectory;
	}

	public Pose6D[] getLeftArmTrajectory()
	{
		return leftArmTrajectory;
	}

	public Pose6D[] getRightArmTrajectory()
	{
		return rightArmTrajectory;
	}

	public Vector2D[] getAdjustmentFactorTrajectory()
	{
		return adjustmentFactorTrajectory;
	}

	public void set(MovementTrajectories movementTrajectories)
	{
		leftFootTrajectory = movementTrajectories.leftFootTrajectory;
		leftArmTrajectory = movementTrajectories.leftArmTrajectory;
		rightFootTrajectory = movementTrajectories.rightFootTrajectory;
		rightArmTrajectory = movementTrajectories.rightArmTrajectory;
		adjustmentFactorTrajectory = movementTrajectories.adjustmentFactorTrajectory;
	}
}