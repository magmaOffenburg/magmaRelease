/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose6D;
import kdo.util.parameter.IParameterList;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class IKCoMShiftingStepMovement extends IKStepMovementBase
{
	/**
	 * The support foot ratio specifies how much of the step is performed by the
	 * supporting foot. This ratio implicitly determines the shifting ratio of
	 * the center of mass from the support foot position towards the target
	 * position of the free foot.
	 */
	private final float supportFootRatio;

	/** The target pose of the free foot with respect to the supporting foot. */
	protected Pose2D targetPose;

	public IKCoMShiftingStepMovement(IRoboCupThoughtModel thoughtModel, IParameterList params)
	{
		this(thoughtModel, 2f / 3f, params);
	}

	public IKCoMShiftingStepMovement(IRoboCupThoughtModel thoughtModel, float supportFootRatio, IParameterList params)
	{
		super("TargetedStepMovement", thoughtModel, params);

		this.supportFootRatio = supportFootRatio;
		this.targetPose = new Pose2D();

		setMovementCycles(10);

		isStatic = false;
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		// // HACK //////////////////////////////////////////////////
		// if (supportFoot == SupportFoot.LEFT) {
		// targetPose = new Pose2D(0.13, -0.04, Angle.deg(-60));
		// } else {
		// targetPose = new Pose2D(-0.13, -0.04, Angle.deg(60));
		// }
		// // END HACK //////////////////////////////////////////////

		double stepHeight = 0.02;

		// Adjust interpolator-amplitudes to the current step
		freeFootHeightInterpolator.amplitude = stepHeight;
		supportFootHeightInterpolator.amplitude = -1 * stepHeight;
		// * params.getPushDownFactor();

		int sideFactor;
		float leftFootFactor;
		float rightFootFactor;

		// Determine side and foot factors. The distance of the step is
		// distributed across the two legs. The supportFootRatio specifies how
		// much (0...1) of the step distance is performed by the supporting foot.
		// The ratio is used to shift the center of mass towards or away from the
		// target pose.
		// Since the target pose of the free foot is given in the system of the
		// support foot, the target pose for the supporting foot has to be
		// negated. This negation is combined with the corresponding shift factor.
		if (supportFoot == SupportFoot.LEFT) {
			sideFactor = -1;
			leftFootFactor = -1 * supportFootRatio;
			rightFootFactor = 1f - supportFootRatio;
		} else {
			sideFactor = 1;
			leftFootFactor = 1f - supportFootRatio;
			rightFootFactor = -1 * supportFootRatio;
		}

		Vector3D leftFootTargetPosition = new Vector3D( //
				targetPose.getX() * leftFootFactor,		//
				targetPose.getY() * leftFootFactor,		//
				params.getWalkHeight());
		Vector3D rightFootTargetPosition = new Vector3D( //
				targetPose.getX() * rightFootFactor,	 //
				targetPose.getY() * rightFootFactor,	 //
				params.getWalkHeight());

		// Transform target positions to final rotated body system to gain an
		// accurate movement
		Rotation rot =
				new Rotation(Vector3D.PLUS_K, -targetPose.angle.radians() / 2, RotationConvention.VECTOR_OPERATOR);
		leftFootTargetPosition = rot.applyTo(leftFootTargetPosition);
		rightFootTargetPosition = rot.applyTo(rightFootTargetPosition);

		// target poses
		Pose6D leftFootTargetPose = new Pose6D(leftFootTargetPosition);
		leftFootTargetPose.zAngle = sideFactor * targetPose.angle.degrees() / 2;

		Pose6D rightFootTargetPose = new Pose6D(rightFootTargetPosition);
		rightFootTargetPose.zAngle = -1 * sideFactor * targetPose.angle.degrees() / 2;

		interpolateMovement(leftFootTargetPose, rightFootTargetPose, params.getAdjustmentFactors());
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		// if (supportFoot == SupportFoot.LEFT) {
		// return new Rotation(Vector3D.PLUS_J, Math.toRadians(3))
		// .applyTo(Vector3D.PLUS_K);
		// } else {
		// return new Rotation(Vector3D.PLUS_J, Math.toRadians(-3))
		// .applyTo(Vector3D.PLUS_K);
		// }

		return Vector3D.PLUS_K;
	}

	public void setFreeFootTargetPose(Pose2D targetPose)
	{
		this.targetPose.copy(targetPose);
	}
}
