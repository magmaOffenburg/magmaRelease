/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.basic;

import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.pose.PoseInterpolator;
import hso.autonomy.util.geometry.interpolation.progress.CosineProgress;
import hso.autonomy.util.geometry.interpolation.value.LinearValueInterpolator;
import magma.agent.decision.behavior.ikMovement.IKMovementBase;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class IKCosineMovement extends IKMovementBase
{
	/** The target pose of the left foot */
	protected Pose6D leftFootTargetPose;

	/** The target pose of the right foot */
	protected Pose6D rightFootTargetPose;

	public IKCosineMovement(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles)
	{
		this(name, thoughtModel, movementCycles, holdCycles, new Pose6D(), new Pose6D(), Vector3D.PLUS_K);
	}

	public IKCosineMovement(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles,
			Pose6D leftFootTargetPose, Pose6D rightFootTargetPose, Vector3D targetLeaning)
	{
		this(name, thoughtModel, movementCycles, holdCycles, leftFootTargetPose, rightFootTargetPose, targetLeaning,
				false);
	}

	public IKCosineMovement(String name, IRoboCupThoughtModel thoughtModel, int movementCycles, int holdCycles,
			Pose6D leftFootTargetPose, Pose6D rightFootTargetPose, Vector3D targetLeaning, boolean isStatic)
	{
		super(name, thoughtModel, movementCycles, holdCycles);

		this.leftFootTargetPose = leftFootTargetPose;
		this.rightFootTargetPose = rightFootTargetPose;
		this.intendedLeaningVector = targetLeaning;

		supportFoot = SupportFoot.LEFT;
	}

	@Override
	protected void createTrajectoryInterpolators()
	{
		super.createTrajectoryInterpolators();

		leftFootInterpolator = new PoseInterpolator(new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()));

		rightFootInterpolator = new PoseInterpolator(new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()), new LinearValueInterpolator(new CosineProgress()),
				new LinearValueInterpolator(new CosineProgress()));
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		interpolateMovement(leftFootTargetPose, rightFootTargetPose);
	}
}
