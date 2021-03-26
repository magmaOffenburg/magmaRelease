/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.jupiter.api.Test;

public class Pose2DTest
{
	/**
	 * Test for {@link Pose2D#applyTo(IPose2D)}, {@link Pose2D#applyTo(Vector3D)}
	 * , {@link Pose2D#applyInverseTo(IPose2D)} and
	 * {@link Pose2D#applyInverseTo(Vector3D)}
	 */
	@Test
	public void testApplyTo()
	{
		Pose2D testee = new Pose2D(3, 1, Angle.deg(90));

		// Test applyTo other pose
		Pose2D otherPose = new Pose2D(1, 0, Angle.deg(20));
		Pose2D resPose = testee.applyTo(otherPose);
		GeometryTestUtil.comparePose2D(new Pose2D(3, 2, Angle.deg(110)), resPose);

		// Test applyInverseTo other pose
		Pose2D resPoseInverse = testee.applyInverseTo(resPose);
		GeometryTestUtil.comparePose2D(otherPose, resPoseInverse);

		// Test applyTo other position
		Vector3D otherPosition = new Vector3D(2, 1, 0);
		Vector3D resPos = testee.applyTo(otherPosition);
		GeometryTestUtil.compareVector3D(2, 3, 0, resPos);

		// Test applyInverseTo other position
		Vector3D resPosInverse = testee.applyInverseTo(resPos);
		GeometryTestUtil.compareVector3D(otherPosition, resPosInverse);
	}

	/**
	 * Test for {@link Pose2D#applyInverseTo(IPose2D)} and
	 * {@link Pose2D#applyInverseTo(Vector3D)}
	 */
	@Test
	public void testApplyInverseTo()
	{
		Pose2D testee = new Pose2D(3, 1, Angle.deg(90));

		// Test applyTo other pose
		Pose2D otherPose = new Pose2D(1, 0, Angle.deg(20));
		Pose2D resPose = testee.applyInverseTo(otherPose);
		GeometryTestUtil.comparePose2D(-1, 2, Angle.deg(-70), resPose);

		// Test applyTo other position
		Vector3D otherPosition = new Vector3D(2, 1, 0);
		Vector3D resPos = testee.applyInverseTo(otherPosition);
		GeometryTestUtil.compareVector3D(0, 1, 0, resPos);

		// Test for 2D
		Vector2D pos2D = new Vector2D(2, 1);
		Vector2D res2D = testee.applyInverseTo(pos2D);
		GeometryTestUtil.compareVector2D(0, 1, res2D);

		assertEquals(0, res2D.getX(), 0.0001);
		assertEquals(1, res2D.getY(), 0.0001);
	}

	@Test
	public void testGetDistanceTo()
	{
		Pose2D pose1 = new Pose2D(-2, -2);
		Pose2D pose2 = new Pose2D(1, 2);
		assertEquals(5.0, pose1.getDistanceTo(pose2), 0.001);

		pose1 = new Pose2D(2, 2);
		pose2 = new Pose2D(-1, -2);
		assertEquals(5.0, pose1.getDistanceTo(pose2), 0.001);
	}

	@Test
	public void testGetDeltaAngle()
	{
		Pose2D pose1 = new Pose2D(-2, -2, Angle.deg(90));
		Pose2D pose2 = new Pose2D(-2, -2, Angle.deg(45));
		assertEquals(-45.0, pose1.getDeltaAngle(pose2).degrees(), 0.001);
	}

	@Test
	public void testGetAngleTo()
	{
		Pose2D pose1 = new Pose2D(-2, -2, Angle.deg(90));
		Pose2D pose2 = new Pose2D(2, 2);
		assertEquals(-45, pose1.getAngleTo(pose2).degrees(), 0.001);

		pose1 = new Pose2D(2, 2, Angle.deg(91));
		pose2 = new Pose2D(2, 0);
		assertEquals(179, pose1.getAngleTo(pose2).degrees(), 0.001);
	}

	@Test
	public void testCreate()
	{
		assertValuesEqual(0, 0, Angle.ZERO, new Pose2D());
	}

	@Test
	public void testCreateFromPosition()
	{
		assertValuesEqual(10, 5, Angle.ZERO, new Pose2D(10, 5));
	}

	@Test
	public void testCreateFromPositionAndAngle()
	{
		assertValuesEqual(10, 5, Angle.ANGLE_90, new Pose2D(10, 5, Angle.ANGLE_90));
	}

	@Test
	public void testCreateFromVector3D()
	{
		assertValuesEqual(10, 5, Angle.ZERO, new Pose2D(new Vector3D(10, 5, 4)));
	}

	@Test
	public void testCreateFromVector3DAndAngle()
	{
		assertValuesEqual(10, 5, Angle.ANGLE_180, new Pose2D(new Vector3D(10, 5, 4), Angle.ANGLE_180));
	}

	@Test
	public void testCreateFromPose2D()
	{
		assertValuesEqual(10, 5, Angle.ANGLE_180, new Pose2D(new Pose2D(10, 5, Angle.ANGLE_180)));
	}

	@Test
	public void testCreateFromVector2DAndAngle()
	{
		assertValuesEqual(10, 5, Angle.ANGLE_180, new Pose2D(new Vector2D(10, 5), Angle.ANGLE_180));
	}

	@Test
	public void testCreateFromVector2D()
	{
		assertValuesEqual(10, 5, Angle.ZERO, new Pose2D(new Vector2D(10, 5), Angle.ZERO));
	}

	public void assertValuesEqual(double x, double y, Angle angle, Pose2D pose)
	{
		GeometryTestUtil.comparePose2D(x, y, angle, pose);
	}
}
