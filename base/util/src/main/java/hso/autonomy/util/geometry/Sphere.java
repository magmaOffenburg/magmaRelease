/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents a sphere in 3D space
 * @author kdorer
 */
public class Sphere
{
	/** center of the sphere */
	private Vector3D center;

	/** radius of the sphere */
	private double radius;

	public Sphere(Vector3D center, double radius)
	{
		this.center = center;
		this.radius = radius;
	}

	/**
	 * Calculates the intersection of this sphere with the circle specified. The
	 * result is either an empty array, if the two objects do not intersect or if
	 * the two centers have the same y coordinate. So we also return an empty
	 * array if the circle is on the sphere and would result in an infinity of
	 * intersection points. Otherwise it returns the two intersection points. We
	 * assume that the circle is parallel to x-plane. If it is not, sphere and
	 * circle have to be rotated into a coordinate system, where this is the case
	 * before calling this method.
	 *
	 * @param circle a circle on the x-plane
	 * @return no or two intersection points
	 */
	public Vector3D[] intersect(Circle3D circle)
	{
		double a = center.getX();
		double b = center.getY();
		double c = center.getZ();
		double s2 = radius * radius;
		// we assume that the circle is parallel to x-plane. If it is not, sphere
		// and circle have to be rotated into a coordinate system, where this is
		// the case.
		double x = circle.getCenter().getX();
		double d = circle.getCenter().getY();
		double f = circle.getCenter().getZ();
		double r2 = circle.getRadius() * circle.getRadius();

		double dmib = d - b;
		if (Math.abs(dmib) < 1e-10) {
			// The case were both centers have the same y coordinate is not yet
			// supported.
			return new Vector3D[0];
		}

		// the calculation is the solution of the equations
		// x = circle.x
		// (x-a)^2 + (y-b)^2 + (z-c)^2 = s^2
		// (y-d)^2 + (z-f)^2 = r^2
		double m = (c - f) / dmib;
		double m2 = m * m;
		double f2 = f * f;
		double c2 = c * c;
		double xmia = x - a;
		double xma2 = xmia * xmia;
		double k = (s2 - xma2 - r2 + f2 - c2) / (2 * dmib) + (b * .5) + (d * .5);

		double denominator = 1 + m2;
		double p = (k * m - d * m - f) / denominator;
		double kmid = k - d;
		double q = (kmid * kmid + f2 - r2) / denominator;
		double radikant = p * p - q;
		if (radikant < 0) {
			// the circle is too far to intersect
			return new Vector3D[0];
		}
		double sqrt = Math.sqrt(radikant);
		double z1 = -p + sqrt;
		double z2 = -p - sqrt;

		double y1 = k + m * z1;
		double y2 = k + m * z2;

		Vector3D[] result = new Vector3D[2];
		result[0] = new Vector3D(x, y1, z1);
		result[1] = new Vector3D(x, y2, z2);
		return result;
	}

	@Override
	public String toString()
	{
		return center + " : " + radius;
	}
}
