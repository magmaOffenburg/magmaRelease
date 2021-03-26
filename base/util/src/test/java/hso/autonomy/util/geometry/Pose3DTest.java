/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

public class Pose3DTest
{
	/**
	 * Test for {@link Pose3D#applyTo(IPose3D)}, {@link Pose3D#applyTo(Vector3D)}
	 * , {@link Pose3D#applyInverseTo(IPose3D)} and
	 * {@link Pose3D#applyInverseTo(Vector3D)}
	 */
	@Test
	public void testApplyTo()
	{
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 0),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(90), RotationConvention.VECTOR_OPERATOR));

		// Test applyTo other pose
		Pose3D otherPose = new Pose3D(new Vector3D(1, 0, 0),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(20), RotationConvention.VECTOR_OPERATOR));
		Pose3D resPose = testee.applyTo(otherPose);
		GeometryTestUtil.comparePose3D(new Pose3D(3, 2, Angle.deg(110)), resPose);

		// Test applyInverseTo other pose
		Pose3D resPoseInverse = testee.applyInverseTo(resPose);
		GeometryTestUtil.comparePose3D(otherPose, resPoseInverse);

		// Test applyTo other position
		Vector3D otherPosition = new Vector3D(2, 1, 0);
		Vector3D resPos = testee.applyTo(otherPosition);
		GeometryTestUtil.compareVector3D(2, 3, 0, resPos);

		// Test applyInverseTo other position
		Vector3D resPosInverse = testee.applyInverseTo(resPos);
		GeometryTestUtil.compareVector3D(otherPosition, resPosInverse);
	}

	/**
	 * Test for {@link Pose3D#applyInverseTo(IPose3D)} and
	 * {@link Pose3D#applyInverseTo(Vector3D)}
	 */
	@Test
	public void testApplyInverseTo()
	{
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 0),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(90), RotationConvention.VECTOR_OPERATOR));

		// Test applyTo other pose
		Pose3D otherPose = new Pose3D(new Vector3D(1, 0, 0),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(20), RotationConvention.VECTOR_OPERATOR));
		Pose3D resPose = testee.applyInverseTo(otherPose);
		GeometryTestUtil.comparePose3D(new Pose3D(-1, 2, Angle.deg(-70)), resPose);

		// Test applyTo other position
		Vector3D otherPosition = new Vector3D(2, 1, 0);
		Vector3D resPos = testee.applyInverseTo(otherPosition);
		GeometryTestUtil.compareVector3D(0, 1, 0, resPos);
	}

	@Test
	public void testGet2DPose()
	{
		// only horizontally rotated
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 2),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(45), RotationConvention.VECTOR_OPERATOR));
		IPose2D resPose = testee.get2DPose();
		GeometryTestUtil.comparePose2D(new Pose2D(3, 1, Angle.deg(45)), resPose);

		// 3D rotated
		Pose3D otherPose = new Pose3D(
				Vector3D.ZERO, new Rotation(Vector3D.PLUS_J, Math.toRadians(90), RotationConvention.VECTOR_OPERATOR));
		Pose3D newPose = testee.applyTo(otherPose);
		resPose = newPose.get2DPose();
		GeometryTestUtil.comparePose2D(new Pose2D(3, 1, Angle.deg(45)), resPose);
	}

	@Test
	public void testGetHorizontalAngleTo()
	{
		Pose3D testee = new Pose3D(new Vector3D(3, 1, 2),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(0), RotationConvention.VECTOR_OPERATOR));
		Pose3D other = new Pose3D(new Vector3D(4, 2, 2),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(45), RotationConvention.VECTOR_OPERATOR));
		Angle result = testee.getHorizontalAngleTo(other);
		assertEquals(45, result.degrees(), 0.0001);

		testee = new Pose3D(new Vector3D(3, 1, 2),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(45), RotationConvention.VECTOR_OPERATOR));
		other = new Pose3D(new Vector3D(4, 2, 2),
				new Rotation(Vector3D.PLUS_K, Math.toRadians(45), RotationConvention.VECTOR_OPERATOR));
		result = testee.getHorizontalAngleTo(other);
		assertEquals(0, result.degrees(), 0.0001);
	}
}
