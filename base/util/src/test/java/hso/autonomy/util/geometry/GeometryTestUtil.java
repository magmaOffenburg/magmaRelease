/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class GeometryTestUtil
{
	public static final double PRECISION = 0.00001;

	public static void comparePose2D(double x, double y, Angle angle, IPose2D real)
	{
		comparePose2D(new Pose2D(x, y, angle), real);
	}

	public static void comparePose2D(IPose2D expected, IPose2D real)
	{
		assertEquals(expected.getX(), real.getX(), PRECISION);
		assertEquals(expected.getY(), real.getY(), PRECISION);
		assertEquals(expected.getAngle().degrees(), real.getAngle().degrees(), PRECISION);
	}

	public static void comparePose3D(IPose3D expected, IPose3D real)
	{
		compareVector3D(expected.getPosition(), real.getPosition());
		compareRotation(expected.getOrientation(), real.getOrientation());
	}

	public static void compareRotation(Rotation expected, Rotation real)
	{
		double[] expectedAngles = expected.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
		double[] realAngles = real.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
		for (int i = 0; i < expectedAngles.length; i++) {
			assertEquals(Angle.rad(expectedAngles[i]).degrees(), Angle.rad(realAngles[i]).degrees(), PRECISION);
		}
	}

	public static void compareCircle(Circle2D expected, Circle2D real)
	{
		assertEquals(expected.getRadius(), real.getRadius(), PRECISION);
		assertEquals(expected.getX(), real.getX(), PRECISION);
		assertEquals(expected.getY(), real.getY(), PRECISION);
	}

	public static void compareAngle(Angle expected, Angle real)
	{
		assertEquals(expected.degrees(), real.degrees(), PRECISION);
	}

	public static void compareLine2D(Line2D expected, Line2D real)
	{
		compareVector2D(expected.getStart(), real.getStart());
		compareVector2D(expected.getExtensionVector(), real.getExtensionVector());
		compareVector2D(expected.getEnd(), real.getEnd());
	}

	public static void compareVector2D(double x, double y, Vector2D real)
	{
		compareVector2D(new Vector2D(x, y), real);
	}

	public static void compareVector2D(Vector2D expected, Vector2D real)
	{
		assertEquals(expected.getX(), real.getX(), PRECISION);
		assertEquals(expected.getY(), real.getY(), PRECISION);
	}

	public static void compareVector3D(double x, double y, double z, Vector3D real)
	{
		compareVector3D(new Vector3D(x, y, z), real);
	}

	public static void compareVector3D(Vector3D expected, Vector3D real)
	{
		assertEquals(expected.getX(), real.getX(), PRECISION);
		assertEquals(expected.getY(), real.getY(), PRECISION);
		assertEquals(expected.getZ(), real.getZ(), PRECISION);
	}
}
