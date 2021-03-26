/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hso.autonomy.util.geometry.Geometry;
import hso.autonomy.util.geometry.Pose6D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Stefan Glaser
 */
public class BalancingEngineTest
{
	private static final float COM_HEIGHT = -0.075f;

	Pose6D poseLeft;

	Pose6D poseRight;

	Vector3D com;

	Vector3D intendedLeaning;

	float saggitalAdjustmentFactor;

	float maxAbsSaggitalAdjustment;

	float coronalAdjustmentFactor;

	float maxAbsCoronalAdjustment;

	@BeforeEach
	public void setUp()
	{
		poseLeft = new Pose6D(-0.12, 0.01, -0.67, 0, 0, 15);
		poseRight = new Pose6D(0.12, 0.01, -0.61, 0, 0, -15);
		com = new Vector3D(0, 0, COM_HEIGHT);

		// Set default values for dynamic adjustment
		saggitalAdjustmentFactor = 0.2f;
		maxAbsSaggitalAdjustment = 100;
		coronalAdjustmentFactor = 0.2f;
		maxAbsCoronalAdjustment = 100;
	}

	@Test
	public void testEngineStaticNoIntendedLeaning()
	{
		intendedLeaning = Vector3D.PLUS_K;

		Pose6D expectedPoseLeft = new Pose6D(-0.12, 0.01, -0.67 + COM_HEIGHT, 0, 0, 15);
		Pose6D expectedPoseRight = new Pose6D(0.12, 0.01, -0.61 + COM_HEIGHT, 0, 0, -15);

		checkEngineStaticInRange(expectedPoseLeft, expectedPoseRight);
	}

	@Test
	public void testEngineStaticWithIntendedLeaning()
	{
		Rotation intendedLeaningRotation =
				new Rotation(Vector3D.PLUS_I, Math.toRadians(10), RotationConvention.VECTOR_OPERATOR);
		intendedLeaning = intendedLeaningRotation.applyTo(Vector3D.PLUS_K);

		Vector3D expectedLeftPos = com.add(intendedLeaningRotation.applyInverseTo(poseLeft.getPosition()));
		Vector3D expectedRightPos = com.add(intendedLeaningRotation.applyInverseTo(poseRight.getPosition()));

		Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, new Vector3D(-10, 0, poseLeft.zAngle));
		Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(-10, 0, poseRight.zAngle));

		checkEngineStaticInRange(expectedPoseLeft, expectedPoseRight);
	}

	@Test
	@Disabled
	public void testEngineDynamicNoIntendedLeaning()
	{
		intendedLeaning = Vector3D.PLUS_K;

		// int range = 66;
		// int step = 33;
		// for (int xDeg = -range; xDeg <= range; xDeg += step) {
		// for (int yDeg = -range; yDeg <= range; yDeg += step) {
		int xDeg = 10;
		int yDeg = -5;

		Rotation zFreeOrientation = new Rotation(
				RotationOrder.ZXY, RotationConvention.VECTOR_OPERATOR, 0, Math.toRadians(xDeg), Math.toRadians(yDeg));

		Rotation adjustmentRotation = zFreeOrientation.applyTo(new Rotation(RotationOrder.YXZ,
				RotationConvention.VECTOR_OPERATOR, Math.toRadians(-yDeg * saggitalAdjustmentFactor),
				Math.toRadians(-xDeg * coronalAdjustmentFactor), 0));

		Vector3D expectedLeftPos = com.add(adjustmentRotation.applyInverseTo(poseLeft.getPosition()));
		Vector3D expectedRightPos = com.add(adjustmentRotation.applyInverseTo(poseRight.getPosition()));

		// TODO: Check Engine with respect to the resulting foot angles. So far,
		// it is not really clear what's calculated.
		Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, new Vector3D(-8.02392, 3.93913, poseLeft.zAngle));
		Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(-8.02392, 3.93913, poseRight.zAngle));

		// Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, //
		// new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor),//
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseLeft.zAngle));
		// Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor), //
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseRight.zAngle));

		checkEngineInZRange(expectedPoseLeft, expectedPoseRight, xDeg, yDeg);
		// }
		// }
	}

	@Test
	@Disabled
	public void testEngineDynamicWithIntendedLeaning()
	{
		Rotation intendedLeaningRotation =
				new Rotation(Vector3D.PLUS_I, Math.toRadians(10), RotationConvention.VECTOR_OPERATOR);
		intendedLeaning = intendedLeaningRotation.applyTo(Vector3D.PLUS_K);

		// int range = 66;
		// int step = 33;
		// for (int xDeg = -range; xDeg <= range; xDeg += step) {
		// for (int yDeg = -range; yDeg <= range; yDeg += step) {
		int xDeg = 10;
		int yDeg = -5;

		Rotation zFreeOrientation = new Rotation(
				RotationOrder.ZXY, RotationConvention.VECTOR_OPERATOR, 0, Math.toRadians(xDeg), Math.toRadians(yDeg));

		Rotation adjustmentRotation = zFreeOrientation.applyTo(new Rotation(RotationOrder.YXZ,
				RotationConvention.VECTOR_OPERATOR, Math.toRadians(-yDeg * saggitalAdjustmentFactor),
				Math.toRadians(-(10 - xDeg) * coronalAdjustmentFactor), 0));

		Vector3D expectedLeftPos = com.add(adjustmentRotation.applyInverseTo(poseLeft.getPosition()));
		Vector3D expectedRightPos = com.add(adjustmentRotation.applyInverseTo(poseRight.getPosition()));

		// TODO: Check Engine with respect to the resulting foot angles. So far,
		// it is not really clear what's calculated.
		Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, new Vector3D(-10.02392, 3.93913, poseLeft.zAngle));
		Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(-10.02392, 3.93913, poseRight.zAngle));

		// Pose6D expectedPoseLeft = new Pose6D(expectedLeftPos, //
		// new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor),//
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseLeft.zAngle));
		// Pose6D expectedPoseRight = new Pose6D(expectedRightPos, new Vector3D(//
		// -xDeg * (1 - coronalAdjustmentFactor), //
		// -yDeg * (1 - saggitalAdjustmentFactor), //
		// poseRight.zAngle));

		checkEngineInZRange(expectedPoseLeft, expectedPoseRight, xDeg, yDeg);
		// }
		// }
	}

	/**
	 * Check method used to ensure the independence of the z-, x, and y-rotation
	 * in the provided orientation in the <b>static parameterization case</b>.
	 * This method calls the
	 * {@link BalancingEngine#adjustTargetPoses(IBalancingEngineParameters,
	 * Pose6D...)} in a nested loop with a set of z-, x- and y-rotations and
	 * compares its results to the provided expected poses. In the static case,
	 * the expected poses should be met regardless of the provided base
	 * orientation. <br> <b>Note:</b> This Method sets adjustment values and
	 * limits to meet the static case.
	 *
	 * @param expectedPoseLeft - the expected left pose
	 * @param expectedPoseRight - the expected left pose
	 */
	protected void checkEngineStaticInRange(Pose6D expectedPoseLeft, Pose6D expectedPoseRight)
	{
		int range = 66;
		int step = 33;
		saggitalAdjustmentFactor = 1f;
		maxAbsSaggitalAdjustment = 360;
		coronalAdjustmentFactor = 1f;
		maxAbsCoronalAdjustment = 360;

		for (int xDeg = -range; xDeg <= range; xDeg += step) {
			for (int yDeg = -range; yDeg <= range; yDeg += step) {
				checkEngineInZRange(expectedPoseLeft, expectedPoseRight, xDeg, yDeg);
			}
		}
	}

	/**
	 * Check method used to ensure the independence of the z-rotation in the
	 * provided orientation. This method calls the
	 * {@link BalancingEngine#adjustTargetPoses(IBalancingEngineParameters,
	 * Pose6D...)} in a loop with a set of z-rotations and the given x- and
	 * y-rotations and compares its results to the provided expected poses.
	 *
	 * @param expectedPoseLeft - the expected left pose
	 * @param expectedPoseRight - the expected left pose
	 * @param xDeg - the additional x-rotation in degrees
	 * @param yDeg - the additional y-rotation in degrees
	 */
	protected void checkEngineInZRange(Pose6D expectedPoseLeft, Pose6D expectedPoseRight, int xDeg, int yDeg)
	{
		int zRange = 175;
		int zStep = 35;
		BalancingEngineParameters params = new BalancingEngineParameters(intendedLeaning, com, saggitalAdjustmentFactor,
				maxAbsSaggitalAdjustment, coronalAdjustmentFactor, maxAbsCoronalAdjustment);
		Pose6D[] resultingPoses;
		Rotation orientationEstimation;

		for (int zDdeg = -zRange; zDdeg <= zRange; zDdeg += zStep) {
			orientationEstimation = new Rotation(RotationOrder.ZXY, RotationConvention.VECTOR_OPERATOR,
					Math.toRadians(zDdeg), Math.toRadians(xDeg), Math.toRadians(yDeg));

			resultingPoses = BalancingEngine.adjustTargetPoses(orientationEstimation, params, poseLeft, poseRight);

			comparePoses(expectedPoseLeft, resultingPoses[0]);
			comparePoses(expectedPoseRight, resultingPoses[1]);
		}
	}

	protected void checkEngine(Pose6D expectedPoseLeft, Pose6D expectedPoseRight, Rotation orientationEstimation)
	{
		BalancingEngineParameters params = new BalancingEngineParameters(intendedLeaning, com, saggitalAdjustmentFactor,
				maxAbsSaggitalAdjustment, coronalAdjustmentFactor, maxAbsCoronalAdjustment);
		Pose6D[] resultingPoses = BalancingEngine.adjustTargetPoses(orientationEstimation, params, poseLeft, poseRight);

		comparePoses(expectedPoseLeft, resultingPoses[0]);
		comparePoses(expectedPoseRight, resultingPoses[1]);
	}

	protected void comparePoses(Pose6D pose1, Pose6D pose2)
	{
		assertEquals(pose1.x, pose2.x, 0.00001, "BalancingEngine x");
		assertEquals(pose1.y, pose2.y, 0.00001, "BalancingEngine y");
		assertEquals(pose1.z, pose2.z, 0.00001, "BalancingEngine z");
		assertEquals(pose1.xAngle, pose2.xAngle, 0.00001, "BalancingEngine xAngle");
		assertEquals(pose1.yAngle, pose2.yAngle, 0.00001, "BalancingEngine yAngle");
		assertEquals(pose1.zAngle, pose2.zAngle, 0.00001, "BalancingEngine zAngle");
	}

	@Test
	@Disabled
	public void testRotations()
	{
		double xAngle = Math.toRadians(10);
		double yAngle = Math.toRadians(20);
		double zAngle = Math.toRadians(0);

		Rotation torsoTarget =
				new Rotation(RotationOrder.ZXY, RotationConvention.VECTOR_OPERATOR, zAngle, xAngle, yAngle);

		System.out.println("Torso Target:");
		Geometry.printRotationMatrix(torsoTarget);

		Rotation rotX = new Rotation(Vector3D.PLUS_I, -xAngle / 4.5, RotationConvention.VECTOR_OPERATOR);
		Rotation rotY = new Rotation(Vector3D.PLUS_J, -yAngle / 10.4, RotationConvention.VECTOR_OPERATOR);

		Rotation rotYFull = new Rotation(Vector3D.PLUS_J, yAngle, RotationConvention.VECTOR_OPERATOR);
		Rotation rotYNegFull = new Rotation(Vector3D.PLUS_J, -yAngle, RotationConvention.VECTOR_OPERATOR);

		Rotation rot2 = rotYNegFull.applyTo(rotX).applyTo(rotYFull).applyTo(rotY);
		Rotation rot3 = rotYNegFull.applyTo(rotX).applyTo(rotYNegFull.applyInverseTo(rotY));

		System.out.println(Rotation.distance(rot2, rot3));

		Rotation result = torsoTarget.applyTo(rot2);

		System.out.println("Result:");
		Geometry.printRotationMatrix(result);
	}
}