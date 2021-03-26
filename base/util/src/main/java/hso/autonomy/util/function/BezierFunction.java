/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents a Bezier function in 3D.
 * @author kdorer
 */
public class BezierFunction
{
	/** start point of the curve */
	Vector3D p0;

	/** point defining the tangent at the start point */
	Vector3D p1;

	/** point defining the tangent at the end point */
	Vector3D p2;

	/** end point of the curve */
	Vector3D p3;

	/**
	 * @param p0 start point of the curve
	 * @param p1 point defining the tangent at the start point
	 * @param p2 point defining the tangent at the end point
	 * @param p3 end point of the curve
	 */
	public BezierFunction(Vector3D p0, Vector3D p1, Vector3D p2, Vector3D p3)
	{
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	/**
	 * Calculates a point in 3D space that resembles a point on a 3D Bezier
	 * curve.
	 *
	 * @param t the interpolation parameter (0..1)
	 * @return point on Bezier curve.
	 */
	public Vector3D calculateBezierPoint(float t)
	{
		return FunctionUtil.bezierPoint(p0, p1, p2, p3, t);
	}

	/**
	 * Calculates and prints the specified number of Bezier points
	 * @param segments the number of points to calculate
	 */
	public void calculateCurve(int segments)
	{
		for (int i = 0; i <= segments; i++) {
			float t = i / (float) segments;
			calculateBezierPoint(t);
		}
	}
}
