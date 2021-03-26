/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import hso.autonomy.util.geometry.Geometry;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a piecewise linear function with any number of pieces
 *
 * @author Klaus Dorer
 */
public class PiecewiseLinearFunction extends SupportPointFunction
{
	/** name identifying a this kind of function */
	public static final String NAME = "linear";

	/**
	 * Default Constructor. Instantiates a new PiecewiseLinearFunction object
	 * with no initial SupportPoints.
	 */
	public PiecewiseLinearFunction()
	{
		super(1);
	}

	public PiecewiseLinearFunction(ArrayList<SupportPoint> points, float stiffness)
	{
		super(points, stiffness);
	}

	/**
	 * Copy Constructor
	 *
	 * @param source Source object to copy
	 */
	private PiecewiseLinearFunction(PiecewiseLinearFunction source)
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

	/**
	 * Returns the sin value at the passed x coordinate. Parameters are used as
	 * written in constructor.
	 * @param t the coordinate at which to return the functions value
	 * @return the sin value at the passed x coordinate
	 */
	@Override
	public double value(double t)
	{
		// Special condition for logging use
		if (supportPoints.size() == 0) {
			return 0;
		}

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

		// linear interpolation
		return Geometry.linearInterpolation(sPointBefore.x, sPointBefore.y, sPointAfter.x, sPointAfter.y, t);
	}

	/**
	 * @see hso.autonomy.util.function.IFunction#copy()
	 */
	@Override
	public IFunction copy()
	{
		return new PiecewiseLinearFunction(this);
	}

	@Override
	public Vector2D moveTangentPointBeforeTo(int index, float x, float y)
	{
		return null;
	}

	@Override
	public Vector2D moveTangentPointAfterTo(int index, float x, float y)
	{
		return null;
	}

	/**
	 * Removes unnecessary support points.
	 */
	public PiecewiseLinearFunction simplify()
	{
		if (supportPoints.size() < 3) {
			return this;
		}

		for (int i = 1; i < supportPoints.size() - 1; i++) {
			SupportPoint previous = supportPoints.get(i - 1);
			SupportPoint current = supportPoints.get(i);
			SupportPoint next = supportPoints.get(i + 1);

			if (current.equalsY(previous) && current.equalsY(next)) {
				removeSupportPoint(i);
				i--;
			}
		}
		return this;
	}
}