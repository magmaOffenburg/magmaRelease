/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import hso.autonomy.util.misc.FuzzyCompare;
import java.util.ArrayList;

/**
 * A simple class representing support points in 2D space.
 *
 * @author Stefan Glaser
 */
public class SupportPoint extends Point implements ISupportPoint
{
	/** The sub-support point before the actual support point */
	public Point tangentPointBefore;

	/** The sub-support point after the actual support point */
	public Point tangentPointAfter;

	/**
	 * Constructs a new SupportPoint at the given position.
	 *
	 * @param x - the x coordinate of this support point
	 * @param y - the y coordinate of this support point
	 */
	public SupportPoint(float x, float y)
	{
		super(x, y);
	}

	/**
	 * Copy constructor.
	 *
	 * @param source - source support point
	 */
	public SupportPoint(SupportPoint source)
	{
		this(source.x, source.y);

		if (source.tangentPointBefore != null) {
			this.tangentPointBefore = new Point(source.tangentPointBefore);
		}

		if (source.tangentPointAfter != null) {
			this.tangentPointAfter = new Point(source.tangentPointAfter);
		}
	}

	/**
	 * Constructs a new SupportPoint at the given position with the given
	 * tangential points.
	 *
	 * @param xBefore - the x coordinate of the tangent point before the actual
	 *        support point
	 * @param yBefore - the y coordinate of the tangent point before the actual
	 *        support point
	 * @param x - the x coordinate of this support point
	 * @param y - the y coordinate of this support point
	 * @param xAfter - the x coordinate of the tangent point after the actual
	 *        support point
	 * @param yAfter - the y coordinate of the tangent point after the actual
	 *        support point
	 */
	public SupportPoint(float xBefore, float yBefore, float x, float y, float xAfter, float yAfter)
	{
		super(x, y);
		this.tangentPointBefore = new Point(xBefore, yBefore);
		this.tangentPointAfter = new Point(xAfter, yAfter);
	}

	/**
	 * Creates instances for the tangent points if they are not already present.
	 *
	 * @return this SupportPoint
	 */
	public SupportPoint createTangentPoints()
	{
		if (tangentPointBefore == null) {
			tangentPointBefore = new Point(x - 1, y);
		}

		if (tangentPointAfter == null) {
			tangentPointAfter = new Point(x + 1, y);
		}

		return this;
	}

	@Override
	public Point getTangentPointBefore()
	{
		return tangentPointBefore;
	}

	@Override
	public Point getTangentPointAfter()
	{
		return tangentPointAfter;
	}

	/**
	 * Move this support point relative to its current position. Adjusts before
	 * and after tangential points accordingly.
	 *
	 * @param x - the shift in x-direction
	 * @param y - the shift in y direction
	 */
	@Override
	public void move(float x, float y)
	{
		this.x += x;
		this.y += y;

		if (tangentPointBefore != null) {
			tangentPointBefore.x += x;
			tangentPointBefore.y += y;
		}

		if (tangentPointAfter != null) {
			tangentPointAfter.x += x;
			tangentPointAfter.y += y;
		}
	}

	/**
	 * Move this support point to an absolute position. Adjusts before and after
	 * tangential points accordingly.
	 *
	 * @param x - the new x position of this support point
	 * @param y - the new y position of this support point
	 */
	public void moveTo(float x, float y)
	{
		if (tangentPointBefore != null) {
			tangentPointBefore.x = x + (tangentPointBefore.x - this.x);
			tangentPointBefore.y = y + (tangentPointBefore.y - this.y);
		}

		if (tangentPointAfter != null) {
			tangentPointAfter.x = x + (tangentPointAfter.x - this.x);
			tangentPointAfter.y = y + (tangentPointAfter.y - this.y);
		}

		this.x = x;
		this.y = y;
	}

	public static ArrayList<SupportPoint> fromArrays(double[] xValues, double[] yValues)
	{
		ArrayList<SupportPoint> result = new ArrayList<>();

		for (int i = 0; i < xValues.length; i++) {
			result.add(new SupportPoint((float) xValues[i], (float) yValues[i]));
		}

		return result;
	}

	public static ArrayList<SupportPoint> fromArray(double... values)
	{
		ArrayList<SupportPoint> result = new ArrayList<>();

		for (int i = 0; i < values.length - 1; i += 2) {
			result.add(new SupportPoint((float) values[i], (float) values[i + 1]));
		}

		return result;
	}

	public static double[][] toArrays(ArrayList<SupportPoint> supportPoints)
	{
		double[][] result = new double[2][supportPoints.size()];

		for (int i = 0; i < supportPoints.size(); i++) {
			result[0][i] = supportPoints.get(i).x;
			result[1][i] = supportPoints.get(i).y;
		}

		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (super.equals(obj)) {
			return true;
		}

		if (obj instanceof SupportPoint) {
			SupportPoint other = (SupportPoint) obj;
			return equalsX(other) && equalsY(other);
		}

		return false;
	}

	@Override
	public boolean equalsX(SupportPoint other)
	{
		return FuzzyCompare.eq(x, other.x, 0.00001);
	}

	@Override
	public boolean equalsY(SupportPoint other)
	{
		return FuzzyCompare.eq(y, other.y, 0.00001);
	}
}
