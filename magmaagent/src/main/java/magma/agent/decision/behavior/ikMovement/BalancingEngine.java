/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose6D;
import hso.autonomy.util.misc.ValueUtil;
import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.CardanEulerSingularityException;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * @author Stefan Glaser
 */
public class BalancingEngine implements Serializable
{
	/**
	 * This Method implements the concept of the balancing engine.
	 *
	 * @param orientationEstimation - the current orientation estimation
	 * @param adjustmentParameter - the adjustment parameters
	 * @param poses - a list of poses that should be adjusted
	 *
	 * @return an array of adjusted poses
	 */
	public static Pose6D[] adjustTargetPoses(
			Rotation orientationEstimation, IBalancingEngineParameters adjustmentParameter, Pose6D... poses)
	{
		// Process the top view z-normalized orientation
		Rotation topViewOrientation = Geometry.getTopViewOrientation(orientationEstimation);

		// Fetch intended and current leaning vector
		Vector3D intendedLeaningVector = adjustmentParameter.getIntendedLeaningVector();
		double[][] tempMat = topViewOrientation.getMatrix();
		Vector3D currentLeaningVector = new Vector3D(tempMat[0][2], tempMat[1][2], tempMat[2][2]);

		// Calculate x-axis (FootPitch) and y-axis (FootRoll) difference angles
		double xDifferenceAngle =
				Math.toDegrees(Math.atan2(-intendedLeaningVector.getY(), intendedLeaningVector.getZ()) -
							   Math.atan2(-currentLeaningVector.getY(), currentLeaningVector.getZ()));

		double yDifferenceAngle =
				Math.toDegrees(Math.asin(intendedLeaningVector.getX()) - Math.asin(currentLeaningVector.getX()));

		// Process adjustment angles by weighting and limiting the difference
		// angles
		double footXAdjustment =
				ValueUtil.limitAbs(xDifferenceAngle * adjustmentParameter.getSaggitalAdjustmentFactor(),
						adjustmentParameter.getMaxAbsSaggitalAdjustment());
		double footYAdjustment = ValueUtil.limitAbs(yDifferenceAngle * adjustmentParameter.getCoronalAdjustmentFactor(),
				adjustmentParameter.getMaxAbsCoronalAdjustment());

		// Create torso target orientation (also describing the plane to adjust
		// the feet)
		Rotation rotXAdjustment =
				new Rotation(Vector3D.PLUS_I, Math.toRadians(footXAdjustment), RotationConvention.VECTOR_OPERATOR);
		Rotation rotYAdjustment =
				new Rotation(Vector3D.PLUS_J, Math.toRadians(footYAdjustment), RotationConvention.VECTOR_OPERATOR);
		Rotation rotYFull =
				new Rotation(Vector3D.PLUS_J, Math.toRadians(yDifferenceAngle), RotationConvention.VECTOR_OPERATOR);
		Rotation torsoTargetRotation = topViewOrientation.applyTo(
				rotYFull.applyTo(rotXAdjustment).applyTo(rotYFull.applyInverseTo(rotYAdjustment)));

		// ===== Alternative calculation via Quaternion slerp ==================
		// Rotation adjustment = new Rotation(currentLeaningVector,
		// intendedLeaningVector);
		// adjustment = new Rotation(adjustment.getAxis(), adjustment.getAngle()
		// * adjustmentFactor);
		// torsoTargetRotation = adjustment.applyTo(topViewOrientation);
		// ===== Alternative calculation via Quaternion slerp ==================

		Pose6D[] adjustedPoses = new Pose6D[poses.length];
		Vector3D adjustedLimbPos;
		Rotation adjustedLimbRotation;
		double[] angles;

		for (int i = 0; i < poses.length; i++) {
			// ========== ANGLES ========== //
			adjustedLimbRotation = new Rotation(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR,
					-1 * Math.toRadians(poses[i].xAngle), -1 * Math.toRadians(poses[i].yAngle), 0)
										   .applyTo(torsoTargetRotation)
										   .revert();
			angles = getXYZRotations(adjustedLimbRotation);

			// ========== POSITION ========== //
			adjustedLimbPos =
					adjustmentParameter.getPivotPoint().add(torsoTargetRotation.applyInverseTo(poses[i].getPosition()));

			// Create new adjusted pose
			adjustedPoses[i] = new Pose6D(adjustedLimbPos.getX(), adjustedLimbPos.getY(), adjustedLimbPos.getZ(),
					angles[0], angles[1], poses[i].zAngle, poses[i].rotationOrder);
		}

		return adjustedPoses;
	}

	/**
	 * @param orientation - the orientation to extract the axes-rotations
	 * @return an array containing the x-, y- and z-rotation
	 */
	private static double[] getXYZRotations(Rotation orientation)
	{
		try {
			double[] angles;
			angles = orientation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
			angles[0] = Math.toDegrees(angles[0]);
			angles[1] = Math.toDegrees(angles[1]);
			angles[2] = Math.toDegrees(angles[2]);

			return angles;
		} catch (CardanEulerSingularityException e) {
			e.printStackTrace();
		}

		return new double[] {0, 0, 0};
	}
}