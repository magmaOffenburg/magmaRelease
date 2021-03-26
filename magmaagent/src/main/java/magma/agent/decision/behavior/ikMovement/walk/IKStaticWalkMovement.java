/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose6D;
import magma.agent.decision.behavior.ikMovement.IIKMovement;
import magma.agent.decision.behavior.ikMovement.Step;
import magma.agent.model.agentmodel.SupportFoot;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * @author Stefan Glaser
 */
public class IKStaticWalkMovement extends IKStepMovementBase implements IIKWalkMovement
{
	/** the current step parameters */
	protected Step currentStep;

	/** the current step parameters */
	protected Step previousStep;

	public IKStaticWalkMovement(String name, IRoboCupThoughtModel thoughtModel, IKWalkMovementParametersBase params)
	{
		super(name, thoughtModel, params);

		currentStep = new Step();
		previousStep = new Step();
		isStatic = true;
	}

	@Override
	public void init(IIKMovement other)
	{
		// Adjust interpolator-amplitudes to the current step
		freeFootHeightInterpolator.amplitude = currentStep.upward;
		supportFootHeightInterpolator.amplitude = -1 * currentStep.upward * params.getPushDownFactor();

		super.init(other);
	}

	@Override
	protected void calculateMovementTrajectory()
	{
		// target poses
		Pose6D leftFootTargetPose = calculateFootTargetPose(currentStep, SupportFoot.LEFT, supportFoot, 0.0);
		Pose6D rightFootTargetPose = calculateFootTargetPose(currentStep, SupportFoot.RIGHT, supportFoot, 0.0);

		// interpolate movement
		interpolateMovement(leftFootTargetPose, rightFootTargetPose, new Vector2D(1, 1));
	}

	/**
	 * Calculate the target pose based on the given step parameters.<br>
	 * The default implementation calculates and limits the target pose
	 * symmetrically for both legs.
	 */
	protected Pose6D calculateFootTargetPose(
			Step step, SupportFoot targetFoot, SupportFoot supportFoot, double widthOffset)
	{
		int side = targetFoot == SupportFoot.LEFT ? -1 : 1;
		int support = supportFoot == SupportFoot.LEFT ? -1 : 1;

		// Limit x-target distance, since we can't move too much to the side
		double targetX = -1 * side * support * step.sideward;
		double limit = -0.02;
		// EXPERIMENT: adjust step limit if turning
		// + Geometry.getLinearFuzzyValue(5, 50, true,
		// Math.abs(step.turnAngle)) * 0.04;

		if (side * targetX < limit) {
			targetX = limit * side;
		}
		double walkWidth = params.getWalkWidth() + widthOffset;
		targetX += side * walkWidth;

		double targetY = params.getWalkOffset() - side * support * step.forward;

		// EXPERIMENT: make bigger side steps if hanging (capture steps)
		//		Rotation orientation = worldModel.getThisPlayer().getOrientation();
		//		double[][] matrix = orientation.getMatrix();
		//		double hangingSide = matrix[2][0];
		//		double adjust = 0.015;
		//		if (Math.abs(hangingSide) > 0.1) {
		//			if (supportFoot == SupportFoot.LEFT && /*targetFoot == SupportFoot.RIGHT &&*/ hangingSide > 0) {
		//				// make capture step right
		//				double adjustment = adjust * Geometry.getLinearFuzzyValue(0.1, 0.2, true, hangingSide);
		//				if (targetFoot == SupportFoot.RIGHT) {
		//					targetX += adjustment;
		//				} else {
		//					targetX -= adjustment;
		//				}
		//			} else if (supportFoot == SupportFoot.RIGHT /*&& targetFoot == SupportFoot.LEFT && hangingSide <
		// 0*/)
		//{
		//				// make capture step left
		//				double adjustment = adjust * Geometry.getLinearFuzzyValue(0.1, 0.2, true, -hangingSide);
		//				if (targetFoot == SupportFoot.RIGHT) {
		//					targetX -= adjustment;
		//				} else {
		//					targetX += adjustment;
		//				}
		//			}
		//		}

		// EXPERIMENT: make bigger front steps if hanging (capture steps)
		//		double hangingFront = matrix[2][1];
		//		if (Math.abs(hangingFront) > 0.08) {
		//			// make capture step right
		//			if (supportFoot == SupportFoot.LEFT && targetFoot == SupportFoot.RIGHT && hangingFront > 0) {
		//				targetY += adjust * Geometry.getLinearFuzzyValue(0.08, 0.2, true, hangingFront);
		//			} else if (supportFoot == SupportFoot.RIGHT && targetFoot == SupportFoot.LEFT && hangingFront < 0) {
		//				targetY -= adjust * Geometry.getLinearFuzzyValue(0.08, 0.2, true, -hangingFront);
		//			}
		//		}

		// Limit z-target angle, since we can't reach negative/positive angles
		// with the left/right foot
		double targetZAngle = -1 * side * support * step.turn.degrees();
		if (side * targetZAngle > 0) {
			targetZAngle = 0;
		}

		return new Pose6D(targetX, targetY, params.getWalkHeight(), //
				params.getFootSlantAngle(), 0, targetZAngle);
	}

	/**
	 * Calculates the 2D free foot pose relative to the support foot, which
	 * results from the application of the given step parameters.<br>
	 * <b>Note:</b> The resulting positions are in a system facing the x-axis
	 * just like the global field system.
	 *
	 * @param step the planned step parameters
	 * @param supportFoot the support foot of the step
	 * @return the relative pose of the free foot
	 */
	public Pose2D calculateRelativeFreeFootPose(Step step, SupportFoot supportFoot)
	{
		SupportFoot freeFoot = supportFoot == SupportFoot.LEFT ? SupportFoot.RIGHT : SupportFoot.LEFT;
		Pose6D supportFootTargetPose = calculateFootTargetPose(step, supportFoot, supportFoot, 0.0);
		Pose6D freeFootTargetPose = calculateFootTargetPose(step, freeFoot, supportFoot, 0.0);

		Pose2D supportFootTargetPose2D =
				new Pose2D(supportFootTargetPose.y, -supportFootTargetPose.x, Angle.deg(supportFootTargetPose.zAngle));
		Pose2D freeFootTargetPose2D =
				new Pose2D(freeFootTargetPose.y, -freeFootTargetPose.x, Angle.deg(freeFootTargetPose.zAngle));

		return supportFootTargetPose2D.applyInverseTo(freeFootTargetPose2D);
	}

	@Override
	public IKWalkMovementParametersBase getWalkParameters()
	{
		return params;
	}

	@Override
	public double getSpeed()
	{
		double maxMovement = Math.max(Math.abs(currentStep.sideward / params.getMaxStepWidth()),
				Math.abs(currentStep.forward / params.getMaxStepLength()));

		return Math.max(maxMovement, Math.abs(currentStep.turn.degrees() / params.getMaxTurnAngle().degrees()));
	}

	@Override
	public Vector3D getIntendedLeaningVector()
	{
		return getLeaningVector();
	}

	/**
	 * @param currentStep the current step parameters
	 * @return the leaning vector with respect to the given step parameter
	 */
	protected Vector3D getLeaningVectorAcceleration(Step currentStep)
	{
		double yAngle = 0;
		// double xAngle = 0;
		double acceleration = currentStep.forward - previousStep.forward;
		double xAngle = Math.toRadians(acceleration * -800);

		return new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR, xAngle, yAngle, 0)
				.applyTo(Vector3D.PLUS_K);
	}

	/**
	 * @return the leaning vector with respect to the given step parameters
	 */
	protected Vector3D getLeaningVector()
	{
		// double xAngle = Math.toRadians(-1 * params.getMaxForwardLeaning()
		// * currentStep.y_targetDistance / params.getMaxStepLength());
		// double yAngle = Math.toRadians(params.getMaxSidewardsLeaning()
		// * currentStep.x_targetDistance / params.getMaxStepWidth());
		double xAngle = Math.toRadians(-1 * params.getMaxForwardLeaning());
		double yAngle = Math.toRadians(params.getMaxSidewardsLeaning());

		return new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR, xAngle, yAngle, 0)
				.applyTo(Vector3D.PLUS_K);
	}

	@Override
	public IKStaticWalkMovement setNextStep(Step nextStep)
	{
		previousStep = currentStep.copy();
		currentStep = nextStep.copy();
		return this;
	}

	@Override
	public Step getCurrentStep()
	{
		return currentStep;
	}
}
