/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.OptionalDouble;
import java.util.function.ToDoubleFunction;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;

public class VectorUtils
{
	public static Vector3D to3D(Vector2D vector2D)
	{
		if (vector2D == null) {
			return null;
		}
		return new Vector3D(vector2D.getX(), vector2D.getY(), 0);
	}

	public static List<Vector3D> to3D(List<Vector2D> points)
	{
		List<Vector3D> result = new ArrayList<>(points.size());
		for (Vector2D point : points) {
			result.add(VectorUtils.to3D(point));
		}
		return result;
	}

	public static Vector2D to2D(Vector3D vector3D)
	{
		if (vector3D == null) {
			return null;
		}
		return new Vector2D(vector3D.getX(), vector3D.getY());
	}

	public static Vector3D average(Collection<Vector3D> vectors)
	{
		return average(vectors.toArray(new Vector3D[0]));
	}

	public static Vector3D average(Vector3D... vectors)
	{
		double x = average(vectors, Vector3D::getX);
		double y = average(vectors, Vector3D::getY);
		double z = average(vectors, Vector3D::getZ);
		return new Vector3D(x, y, z);
	}

	private static double average(Vector3D[] vectors, ToDoubleFunction<? super Vector3D> mapper)
	{
		OptionalDouble average = Arrays.stream(vectors).mapToDouble(mapper).average();
		return average.isPresent() ? average.getAsDouble() : 0;
	}

	public static double getDistanceBetweenXY(Vector3D a, Vector3D b)
	{
		Vector3D delta = a.subtract(b);
		return new Vector3D(delta.getX(), delta.getY(), 0).getNorm();
	}

	public static Angle getDirectionTo(Vector3D a, Vector3D b)
	{
		return Angle.rad(b.subtract(a).getAlpha());
	}

	public static Angle getDirectionTo(Vector2D a, Vector2D b)
	{
		Vector2D diff = b.subtract(a);
		return Angle.rad(FastMath.atan2(diff.getY(), diff.getX()));
	}

	public enum Mirror { X, Y, Z, XY, XZ, YZ, XYZ }

	public static Vector3D mirror(Vector3D v, Mirror mirror)
	{
		int x = 1;
		int y = 1;
		int z = 1;

		switch (mirror) {
		case X:
			x = -1;
			break;
		case Y:
			y = -1;
			break;
		case Z:
			z = -1;
			break;
		case XY:
			x = y = -1;
			break;
		case XZ:
			x = z = -1;
			break;
		case YZ:
			y = z = -1;
			break;
		case XYZ:
			x = y = z = -1;
			break;
		}

		return new Vector3D(v.getX() * x, v.getY() * y, v.getZ() * z);
	}

	public static Vector3D rotateAround(Vector3D v, Vector3D pivot, Angle angle)
	{
		v = v.subtract(pivot);
		v = angle.applyTo(v);
		return v.add(pivot);
	}

	public static Vector2D rotateAround(Vector2D v, Vector2D pivot, Angle angle)
	{
		v = v.subtract(pivot);
		v = angle.applyTo(v);
		return v.add(pivot);
	}
}