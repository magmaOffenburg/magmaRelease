/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.geometry.interpolation.pose.PoseInterpolator;
import hso.autonomy.util.geometry.interpolation.progress.CosineProgress;
import hso.autonomy.util.geometry.interpolation.progress.SineHalfProgress;
import hso.autonomy.util.geometry.interpolation.value.LinearValueInterpolator;
import kdo.util.parameter.IParameterList;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class IKGetOnLegStepMovement extends IKStepMovementBase
{
	/**
	 * The target position of the support foot, if the support foot is the left
	 * foot. If this position is used for the right foot, the x-value has to be
	 * negated.
	 */
	private static final Vector3D supportFootTargetPosition = new Vector3D(-0.01, 0.02, -0.015);

	/**
	 * The target position of the free foot, if the free foot is the left foot.
	 * If this position is used for the right foot, the x-value has to be
	 * negated.
	 */
	private static final Vector3D freeFootTargetPosition = new Vector3D(-0.075, -0.06, 0.04);

	/** The target pitch angle of the free foot. */
	private static final double freeFootTargetXAngle = -30;

	public IKGetOnLegStepMovement(IRoboCupThoughtModel thoughtModel, IParameterList params)
	{
		super("GetOnLegStepMovement", thoughtModel, params);

		setMovementCycles(12);
		// holdCycles = 30;

		isStatic = false;
	}

	@Override
	protected void createTrajectoryInterpolators()
	{
		super.createTrajectoryInterpolators();

		supportFootTrajectoryInterpolator = new PoseInterpolator();

		freeFootTrajectoryInterpolator = new PoseInterpolator(new LinearValueInterpolator(new CosineProgress()), //
				new LinearValueInterpolator(new CosineProgress()),												 //
				new LinearValueInterpolator(new SineHalfProgress()),											 //
				new LinearValueInterpolator(),																	 //
				new LinearValueInterpolator(),																	 //
				new LinearValueInterpolator());
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		Pose6D leftFootTargetPose = new Pose6D();
		Pose6D rightFootTargetPose = new Pose6D();

		if (supportFoot == SupportFoot.LEFT) {
			leftFootTargetPose.x = supportFootTargetPosition.getX();
			leftFootTargetPose.y = supportFootTargetPosition.getY();
			leftFootTargetPose.z = params.getWalkHeight() + supportFootTargetPosition.getZ();

			rightFootTargetPose.x = -1 * freeFootTargetPosition.getX();
			rightFootTargetPose.y = freeFootTargetPosition.getY();
			rightFootTargetPose.z = params.getWalkHeight() + freeFootTargetPosition.getZ();
			rightFootTargetPose.xAngle = freeFootTargetXAngle;
		} else {
			leftFootTargetPose.x = freeFootTargetPosition.getX();
			leftFootTargetPose.y = freeFootTargetPosition.getY();
			leftFootTargetPose.z = params.getWalkHeight() + freeFootTargetPosition.getZ();
			leftFootTargetPose.xAngle = freeFootTargetXAngle;

			rightFootTargetPose.x = -1 * supportFootTargetPosition.getX();
			rightFootTargetPose.y = supportFootTargetPosition.getY();
			rightFootTargetPose.z = params.getWalkHeight() + supportFootTargetPosition.getZ();
		}

		interpolateMovement(leftFootTargetPose, rightFootTargetPose, params.getAdjustmentFactors());
	}

	@Override
	public SupportFoot getNextSupportFoot()
	{
		return supportFoot;
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		if (supportFoot == SupportFoot.LEFT) {
			return new Rotation(Vector3D.PLUS_J, Math.toRadians(-3), RotationConvention.VECTOR_OPERATOR)
					.applyTo(Vector3D.PLUS_K);
		} else {
			return new Rotation(Vector3D.PLUS_J, Math.toRadians(3), RotationConvention.VECTOR_OPERATOR)
					.applyTo(Vector3D.PLUS_K);
		}

		// return Vector3D.PLUS_K;
	}
}
