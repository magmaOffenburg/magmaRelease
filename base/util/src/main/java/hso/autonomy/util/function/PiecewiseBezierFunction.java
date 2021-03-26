/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a piecewise 4-point-Bezier function with any number of pieces
 *
 * @author Stefan Glaser
 */
public class PiecewiseBezierFunction extends SupportPointFunction
{
	/** name identifying a this kind of function */
	public static final String NAME = "bezier";

	public PiecewiseBezierFunction(ArrayList<SupportPoint> points, float stiffness)
	{
		super(points, stiffness);
	}

	/**
	 * Copy Constructor
	 *
	 * @param source Source object to copy
	 */
	public PiecewiseBezierFunction(PiecewiseBezierFunction source)
	{
		super(source);
	}

	@Override
	protected int getMinimumSupportPointSize()
	{
		return 2;
	}

	@Override
	protected int getMaximumSupportPointSize()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public int addSupportPoint(float x, float y)
	{
		SupportPoint newSupportPoint = new SupportPoint(x - 1, y, x, y, x + 1, y);

		return addSupportPoint(newSupportPoint);
	}

	/**
	 * Returns the value at the passed x coordinate. Parameters are used as
	 * written in constructor.
	 * @param t the coordinate at which to return the functions value
	 * @return the value at the passed x coordinate
	 */
	@Override
	public double value(double t)
	{
		// Catch border conditions
		if (t <= getMinX()) {
			return supportPoints.get(0).y;
		} else if (t >= getMaxX()) {
			return supportPoints.get(supportPoints.size() - 1).y;
		}

		int index = 1;

		// Find index after the SupportPoint that is closest, but smaller or equal
		// to t
		while (index < (supportPoints.size() - 1) && t >= supportPoints.get(index).x) {
			index++;
		}

		SupportPoint sPointBefore = supportPoints.get(index - 1);
		SupportPoint sPointAfter = supportPoints.get(index);

		Vector2D p0 = sPointBefore.asVector2D();
		Vector2D p1 = sPointBefore.tangentPointAfter.asVector2D();
		Vector2D p2 = sPointAfter.tangentPointBefore.asVector2D();
		Vector2D p3 = sPointAfter.asVector2D();

		double progress = (t - sPointBefore.x) / (sPointAfter.x - sPointBefore.x);

		// 4-point-Bezier interpolation
		return FunctionUtil.bezierPoint(p0, p1, p2, p3, (float) progress).getY();
	}

	@Override
	public void initialize()
	{
		super.initialize();

		SupportPoint previousSp = supportPoints.get(0).createTangentPoints();
		SupportPoint currentSp = supportPoints.get(1).createTangentPoints();
		SupportPoint nextSp = null;
		float diffThirdBefore;
		float diffThirdAfter;

		// Set the tangent point after the first support point to 1/3 width to the
		// second support point and to the same height than the first support
		// point
		diffThirdAfter = (currentSp.x - previousSp.x) / 3;
		previousSp.tangentPointAfter.x = previousSp.x + diffThirdAfter;
		previousSp.tangentPointAfter.y = previousSp.y;

		for (int i = 2; i < supportPoints.size(); i++) {
			nextSp = supportPoints.get(i).createTangentPoints();

			// Distribute tangent points of the current support point with equal
			// distance (1/3) to its neighboring support points
			diffThirdBefore = (currentSp.x - previousSp.x) / 3;
			diffThirdAfter = (nextSp.x - currentSp.x) / 3;
			currentSp.tangentPointBefore.x = currentSp.x - diffThirdBefore;
			currentSp.tangentPointAfter.x = currentSp.x + diffThirdAfter;

			// Calculate the y coordinates of the tangent points around the current
			// support point
			if (previousSp.y <= currentSp.y && nextSp.y <= currentSp.y ||
					previousSp.y >= currentSp.y && nextSp.y >= currentSp.y) {
				// This support point is below/above both neighboring support
				// points, so set tangent points around the current support point to
				// the same height than the actual support point
				currentSp.tangentPointBefore.y = currentSp.y;
				currentSp.tangentPointAfter.y = currentSp.y;
			} else {
				// This support point is between its two neighboring support points,
				// so try to match the slope of the neighboring support points with
				// the tangent points
				float slope = (nextSp.y - previousSp.y) / (nextSp.x - previousSp.x);
				currentSp.tangentPointBefore.y = currentSp.y - slope * diffThirdBefore;
				currentSp.tangentPointAfter.y = currentSp.y + slope * diffThirdAfter;

				if ((currentSp.y < previousSp.y && currentSp.tangentPointBefore.y > previousSp.y) ||
						(currentSp.y > previousSp.y && currentSp.tangentPointBefore.y < previousSp.y)) {
					// Tangent point before is below/above the previous support
					// point, so adjust it to match the height of the previous
					// support point
					currentSp.tangentPointBefore.y = previousSp.y;

					slope = (currentSp.y - currentSp.tangentPointBefore.y) / diffThirdBefore;
					currentSp.tangentPointAfter.y = currentSp.y + slope * diffThirdAfter;

				} else if ((currentSp.y <= nextSp.y && currentSp.tangentPointAfter.y > nextSp.y) ||
						   (currentSp.y >= nextSp.y && currentSp.tangentPointAfter.y < nextSp.y)) {
					// Tangent point after is below/above the next support point, so
					// adjust it to match the height of the next support point
					currentSp.tangentPointAfter.y = nextSp.y;

					slope = (currentSp.tangentPointAfter.y - currentSp.y) / diffThirdAfter;
					currentSp.tangentPointBefore.y = currentSp.y - slope * diffThirdBefore;
				}
			}

			previousSp = currentSp;
			currentSp = nextSp;
		}

		// Set the tangent point after the first support point to 1/3 width to the
		// second support point and to the same height than the first support
		// point
		diffThirdBefore = (currentSp.x - previousSp.x) / 3;
		currentSp.tangentPointBefore.x = currentSp.x - diffThirdBefore;
		currentSp.tangentPointBefore.y = currentSp.y;
	}

	/**
	 * @see hso.autonomy.util.function.IFunction#copy()
	 */
	@Override
	public IFunction copy()
	{
		return new PiecewiseBezierFunction(this);
	}
}
