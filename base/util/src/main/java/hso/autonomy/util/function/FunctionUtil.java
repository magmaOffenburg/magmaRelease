/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.util.List;
import org.apache.commons.math3.geometry.Space;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Utilities to calculate some specific functions.
 *
 * @author Stefan Glaser
 */
public class FunctionUtil
{
	/** the time delta we use for difference quotient */
	public static final double DELTA_T = 0.001;

	/**
	 * Calculates a point in 3D space that resembles a point on an 3D Bezier
	 * curve of arbitrary length.<br>
	 * This is a convenience method. It uses direct static calculations for lower
	 * support point sizes like linear, quadratic and cubic Bezier curves and
	 * switches to an iterative calculation schema in higher dimensions.
	 *
	 * @param points - the list of support points
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector3D bezierPoint3D(List<Vector3D> points, float t)
	{
		switch (points.size()) {
		case 0:
			return Vector3D.ZERO;
		case 1:
			return points.get(0);
		case 2:
			return bezierPoint(points.get(0), points.get(1), t);
		case 3:
			return bezierPoint(points.get(0), points.get(1), points.get(2), t);
		case 4:
			return bezierPoint(points.get(0), points.get(1), points.get(2), points.get(3), t);
		default:
			return bezierPointBA3D(points, t);
		}
	}

	/**
	 * Calculates a point in 2D space that resembles a point on an 2D Bezier
	 * curve of arbitrary length.<br>
	 * This is a convenience method. It uses direct static calculations for lower
	 * support point sizes like linear, quadratic and cubic Bezier curves and
	 * switches to an iterative calculation schema in higher dimensions.
	 *
	 * @param points - the list of support points
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector2D bezierPoint2D(List<Vector2D> points, float t)
	{
		switch (points.size()) {
		case 0:
			return Vector2D.ZERO;
		case 1:
			return points.get(0);
		case 2:
			return bezierPoint(points.get(0), points.get(1), t);
		case 3:
			return bezierPoint(points.get(0), points.get(1), points.get(2), t);
		case 4:
			return bezierPoint(points.get(0), points.get(1), points.get(2), points.get(3), t);
		default:
			return bezierPointBA2D(points, t);
		}
	}

	/**
	 * Calculates a point in nD space that resembles a point on an n-dimensional
	 * Bezier curve of arbitrary length.<br>
	 * Note that this method uses the base algorithm (BA) and is therefore
	 * relatively expensive, since it's iteratively computing the linear
	 * interpolations of the running pointset.
	 *
	 * @param currentPoints - the list of support points
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static float[] bezierPointBA(float[][] currentPoints, float t)
	{
		float[][] nextPoints = new float[currentPoints.length - 1][currentPoints[0].length];
		float[][] tempPoints;

		// Calculate iterative linear interpolations between subsequent points
		// until there is just one point left
		for (int pointsCnt = currentPoints.length; pointsCnt > 1; pointsCnt--) {
			for (int i = 0; i < pointsCnt - 1; i++) {
				nextPoints[i][0] = currentPoints[i][0] + ((currentPoints[i + 1][0] - currentPoints[i][0]) * t);
				nextPoints[i][1] = currentPoints[i][1] + ((currentPoints[i + 1][1] - currentPoints[i][1]) * t);
				nextPoints[i][2] = currentPoints[i][2] + ((currentPoints[i + 1][2] - currentPoints[i][2]) * t);
			}

			// Switch points arrays
			tempPoints = currentPoints;
			currentPoints = nextPoints;
			nextPoints = tempPoints;
		}

		// Return the first vector of the currentPoints array, which is the last
		// valid point left after interpolation calculations
		return currentPoints[0];
	}

	/**
	 * Calculates a point in 3D space that resembles a point on an 3D Bezier
	 * curve of arbitrary length.<br>
	 * Note that this method uses the base algorithm (BA) and is therefore
	 * relatively expensive, since it's iteratively computing the linear
	 * interpolations of the running pointset.
	 *
	 * @param points - the list of support points
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector3D bezierPointBA3D(List<Vector3D> points, float t)
	{
		float[][] currentPoints = new float[points.size()][3];

		// Initialize currentPoints array
		for (int i = 0; i < points.size(); i++) {
			currentPoints[i][0] = (float) points.get(i).getX();
			currentPoints[i][1] = (float) points.get(i).getY();
			currentPoints[i][2] = (float) points.get(i).getZ();
		}

		float[] result = bezierPointBA(currentPoints, t);
		return new Vector3D(result[0], result[1], result[2]);
	}

	/**
	 * Calculates a point in 2D space that resembles a point on an 2D Bezier
	 * curve of arbitrary length.<br>
	 * Note that this method uses the base algorithm (BA) and is therefore
	 * relatively expensive, since it's iteratively computing the linear
	 * interpolations of the running pointset.
	 *
	 * @param points - the list of support points
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector2D bezierPointBA2D(List<Vector2D> points, float t)
	{
		float[][] currentPoints = new float[points.size()][2];

		// Initialize currentPoints array
		for (int i = 0; i < points.size(); i++) {
			currentPoints[i][0] = (float) points.get(i).getX();
			currentPoints[i][1] = (float) points.get(i).getY();
		}

		float[] result = bezierPointBA(currentPoints, t);
		return new Vector2D(result[0], result[1]);
	}

	/**
	 * Calculates a point in 2D/3D space that resembles a point on a linear 2D/3D
	 * Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static <S extends Space> Vector<S> bezierPointGeneric(Vector<S> p0, Vector<S> p1, float t)
	{
		Vector<S> p = p0.scalarMultiply(1 - t);
		p = p.add(p1.scalarMultiply(t));

		return p;
	}

	/**
	 * Calculates a point in 3D space that resembles a point on a linear 3D
	 * Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector3D bezierPoint(Vector3D p0, Vector3D p1, float t)
	{
		return (Vector3D) bezierPointGeneric(p0, p1, t);
	}

	/**
	 * Calculates a point in 2D space that resembles a point on a linear 2D
	 * Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector2D bezierPoint(Vector2D p0, Vector2D p1, float t)
	{
		return (Vector2D) bezierPointGeneric(p0, p1, t);
	}

	/**
	 * Calculates a point in 2D/3D space that resembles a point on a quadratic
	 * 2D/3D Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param p2 - the third support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static <S extends Space> Vector<S> bezierPointGeneric(Vector<S> p0, Vector<S> p1, Vector<S> p2, float t)
	{
		float u = 1.0f - t;
		float tt = t * t;
		float uu = u * u;

		Vector<S> p = p0.scalarMultiply(uu);
		p = p.add(p1.scalarMultiply(2 * u * t));
		p = p.add(p2.scalarMultiply(tt));

		return p;
	}

	/**
	 * Calculates a point in 3D space that resembles a point on a quadratic 3D
	 * Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param p2 - the third support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector3D bezierPoint(Vector3D p0, Vector3D p1, Vector3D p2, float t)
	{
		return (Vector3D) bezierPointGeneric(p0, p1, p2, t);
	}

	/**
	 * Calculates a point in 2D space that resembles a point on a quadratic 2D
	 * Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param p2 - the third support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector2D bezierPoint(Vector2D p0, Vector2D p1, Vector2D p2, float t)
	{
		return (Vector2D) bezierPointGeneric(p0, p1, p2, t);
	}

	/**
	 * Calculates a point in 2D/3D space that resembles a point on a cubic 2D/3D
	 * Bezier curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param p2 - the third support point
	 * @param p3 - the forth support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static <S extends Space> Vector<S> bezierPointGeneric(
			Vector<S> p0, Vector<S> p1, Vector<S> p2, Vector<S> p3, float t)
	{
		float u = 1.0f - t;
		float tt = t * t;
		float uu = u * u;
		float uuu = uu * u;
		float ttt = tt * t;

		Vector<S> p = p0.scalarMultiply(uuu);
		p = p.add(p1.scalarMultiply(3 * uu * t));
		p = p.add(p2.scalarMultiply(3 * u * tt));
		p = p.add(p3.scalarMultiply(ttt));

		return p;
	}

	/**
	 * Calculates a point in 3D space that resembles a point on a cubic 3D Bezier
	 * curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param p2 - the third support point
	 * @param p3 - the forth support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector3D bezierPoint(Vector3D p0, Vector3D p1, Vector3D p2, Vector3D p3, float t)
	{
		return (Vector3D) bezierPointGeneric(p0, p1, p2, p3, t);
	}

	/**
	 * Calculates a point in 2D space that resembles a point on a cubic 2D Bezier
	 * curve.
	 *
	 * @param p0 - the first support point
	 * @param p1 - the second support point
	 * @param p2 - the third support point
	 * @param p3 - the forth support point
	 * @param t - the interpolation parameter (0..1)
	 *
	 * @return point on Bezier curve
	 */
	public static Vector2D bezierPoint(Vector2D p0, Vector2D p1, Vector2D p2, Vector2D p3, float t)
	{
		return (Vector2D) bezierPointGeneric(p0, p1, p2, p3, t);
	}

	/**
	 * Determines the length of a Bezier curve by accumulating linear
	 * interpolations between the specified amount of sample points.
	 *
	 * @param points - the Bezier curve
	 * @param samples - the amount of sample points. If set to 0, the returned
	 *        length is the distance between the start and the end point of the
	 *        curve. Every number grater than 0 adds further sample points
	 *        between the start and the end point.
	 *
	 * @return the linear interpolated length of the given Bezier curve
	 */
	public static double bezierLengthLin3D(List<Vector3D> points, int samples)
	{
		Vector3D previousPoint = points.get(0);
		Vector3D currentPoint;
		double length = 0;

		// Accumulate pairwise distances of each interpolated point to its
		// predecessor point
		for (int i = 0; i < samples; i++) {
			float t = ((float) i + 1) / (samples + 1);
			currentPoint = bezierPoint3D(points, t);

			length += currentPoint.subtract(previousPoint).getNorm();

			previousPoint = currentPoint;
		}

		// Add distance from last sample point to final point on the curve
		length += points.get(points.size() - 1).subtract(previousPoint).getNorm();

		return length;
	}

	/**
	 * Calculates a sine function value.
	 *
	 * @param period - the period of the sine
	 * @param amplitude - the maximum amplitude
	 * @param phase - the phase delay of the sine
	 * @param offset - the offset to the whole function
	 * @param x - the x-coordinate
	 *
	 * @return the value of the parametrized sine function at the given
	 *         x-coordinate
	 */
	public static double sinePoint(double period, double amplitude, double phase, double offset, double x)
	{
		return amplitude * Math.sin(x / period * 2 * Math.PI + phase) + offset;
	}

	/**
	 * Calculates the difference quotient of the passed variables.
	 * @param y1 function value 1
	 * @param y2 function value 2
	 * @param dt distance of the two points
	 */
	public static double derivative1(double y1, double y2, double dt)
	{
		return (y2 - y1) / dt;
	}

	/**
	 * Calculates second derivative as the two times difference quotient of the
	 * passed variables.
	 * @param y1 function value 1
	 * @param y2 function value 2
	 * @param y3 function value 3
	 * @param dt distance of each two points
	 */
	public static double derivative2(double y1, double y2, double y3, double dt)
	{
		double dx1 = (y2 - y1) / dt;
		double dx2 = (y3 - y2) / dt;
		return (dx2 - dx1) / dt;
	}
}
