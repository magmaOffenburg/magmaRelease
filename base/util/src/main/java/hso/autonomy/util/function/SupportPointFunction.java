/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import hso.autonomy.util.misc.FuzzyCompare;
import hso.autonomy.util.misc.ValueUtil;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represents a function defined by a list of SupportPoints.<br>
 * The SupportPoints are expected to be stored in ascending order regarding
 * their x-values.
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public abstract class SupportPointFunction implements IFunction
{
	protected static final float SUBSTEPS_PER_CYCLE = 100f;

	protected static final SupportPointComparator supportPointComparator = new SupportPointComparator();

	/** The support points of this function */
	protected ArrayList<SupportPoint> supportPoints;

	protected float stiffness;

	/** The minimum x value of all SupportPoints */
	protected float minXValue;

	/** The maximum x value of all SupportPoints */
	protected float maxXValue;

	/** The minimum y value of all SupportPoints */
	protected float minYValue;

	/** The maximum y value of all SupportPoints */
	protected float maxYValue;

	protected SupportPointFunction(float stiffness)
	{
		supportPoints = new ArrayList<>();
		setDefaultSupportPoints();
		this.stiffness = ValueUtil.limitValue(stiffness, 0, 1);
	}

	/**
	 * Constructs a new SupportPointFunction with the given SupportPoints. This
	 * Constructor ensures that there exist at least 2 Support Points.
	 *
	 * @param points - an ArrayList containing the initial support points
	 */
	public SupportPointFunction(List<SupportPoint> points, float stiffness)
	{
		supportPoints = new ArrayList<>();
		int maxSize = getMaximumSupportPointSize();

		if (points != null && points.size() >= getMinimumSupportPointSize()) {
			for (int i = 0; i < points.size() && i < maxSize; i++) {
				supportPoints.add(new SupportPoint(points.get(i)));
			}
			sortSupportPoints();
			initialize();
		} else {
			setDefaultSupportPoints();
		}
		this.stiffness = ValueUtil.limitValue(stiffness, 0, 1);
	}

	/**
	 * Copy constructor
	 *
	 * @param source the source to copy
	 */
	public SupportPointFunction(SupportPointFunction source)
	{
		this(source.supportPoints, source.stiffness);
	}

	/**
	 * Called to set the default support points for this function type.
	 */
	protected void setDefaultSupportPoints()
	{
		supportPoints.clear();

		for (int i = 0; i < getMinimumSupportPointSize(); i++) {
			supportPoints.add(new SupportPoint(i * 5, 0));
		}

		initialize();
	}

	/**
	 * @return the minimum number of support points required for this function
	 */
	protected abstract int getMinimumSupportPointSize();

	/**
	 * @return the maximum number of support points possible for this function
	 */
	protected abstract int getMaximumSupportPointSize();

	protected void sortSupportPoints()
	{
		Collections.sort(supportPoints, supportPointComparator);
	}

	@Override
	public String toCSVString()
	{
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		DecimalFormat formatter = new DecimalFormat("##0.0#", symbols);
		StringBuilder result = new StringBuilder();

		// Append name,support-point-size,0,[support-points]
		result.append(getName()).append(",").append(supportPoints.size()).append(",").append("0");
		for (int i = 0; i < supportPoints.size(); i++) {
			result.append(",").append(formatter.format(supportPoints.get(i).x));
			result.append(",").append(formatter.format(supportPoints.get(i).y));
		}
		return result.toString();
	}

	@Override
	public float getMinX()
	{
		return minXValue;
	}

	@Override
	public float getMaxX()
	{
		return maxXValue;
	}

	@Override
	public float getMinY()
	{
		return minYValue;
	}

	@Override
	public float getMaxY()
	{
		return maxYValue;
	}

	@Override
	public float getDefinitionRange()
	{
		return getMaxX() - getMinX();
	}

	@Override
	public boolean hasSupportPointsInArea(float minX, float maxX, float minY, float maxY)
	{
		float width = maxX - minX;
		float height = maxY - minY;

		if (minX > minXValue - width && maxX < maxXValue - width && minY > minYValue - height &&
				maxY < maxYValue + height) {
			ISupportPoint supportPoint;

			int index = 0;
			while (index < supportPoints.size() && supportPoints.get(index).getX() < maxX) {
				supportPoint = supportPoints.get(index);

				if (supportPoint.getX() > minX && supportPoint.getX() < maxX && supportPoint.getY() > minY &&
						supportPoint.getY() < maxY) {
					return true;
				}
				index++;
			}
		}

		return false;
	}

	/**
	 * Checks if the function has a support point in the specified area
	 * @param right border (including) (joint coordinates)
	 * @param left border (including) (joint coordinates)
	 * @return true if at least one support point is inside the specified x area
	 */
	public boolean hasSupportPointInArea(double left, double right)
	{
		for (int i = 0; i < supportPoints.size(); i++) {
			if (supportPoints.get(i).x >= left && supportPoints.get(i).x <= right) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<ISupportPoint> getSupportPointsInArea(float minX, float maxX, float minY, float maxY)
	{
		ArrayList<ISupportPoint> pointsInArea = new ArrayList<>();

		float width = maxX - minX;
		float height = maxY - minY;

		if (minX > minXValue - width && maxX < maxXValue + width && minY > minYValue - height &&
				maxY < maxYValue + height) {
			ISupportPoint supportPoint;

			int index = 0;
			while (index < supportPoints.size() && supportPoints.get(index).getX() < maxX) {
				supportPoint = supportPoints.get(index);

				if (supportPoint.getX() > minX && supportPoint.getX() < maxX && supportPoint.getY() > minY &&
						supportPoint.getY() < maxY) {
					pointsInArea.add(supportPoint);
				}
				index++;
			}
		}

		return pointsInArea;
	}

	@Override
	public int getSupportPointSize()
	{
		return supportPoints.size();
	}

	@Override
	public SupportPoint getSupportPoint(int index)
	{
		return supportPoints.get(index);
	}

	@Override
	public int indexOf(ISupportPoint supportPoint)
	{
		return supportPoints.indexOf(supportPoint);
	}

	@Override
	public float getStiffness()
	{
		return stiffness;
	}

	@Override
	public void moveFunction(float xShift, float yShift)
	{
		for (SupportPoint supportPoint : supportPoints) {
			supportPoint.move(xShift, yShift);
		}

		initialize();
	}

	@Override
	public void mirrorYFunction()
	{
		for (SupportPoint supportPoint : supportPoints) {
			supportPoint.setY(-supportPoint.getY());
		}

		initialize();
	}

	@Override
	public void rescaleXRange(float start, float end, float newEnd)
	{
		if (start > end || newEnd <= start || end == newEnd) {
			return;
		}

		float xShift = newEnd - end;
		float rangeWidth = end - start;
		float newRangeWidth = newEnd - start;
		float relativePos;

		// Shift all support points after the end position
		int index = supportPoints.size() - 1;
		while (index >= 0 && supportPoints.get(index).x >= end) {
			supportPoints.get(index).x += xShift;
			index--;
		}

		// int lastInterpolatedIndex = index;
		// Re-interpolate all intermediate points until the start position
		while (index >= 0 && supportPoints.get(index).x > start) {
			relativePos = (supportPoints.get(index).x - start) / rangeWidth;
			// supportPoints.get(index).x = truncateX(start + relativePos
			// * newRangeWidth);
			supportPoints.get(index).x = start + relativePos * newRangeWidth;
			index--;
		}

		// // Check if re-interpolated support points overlap in position and
		// remove
		// // them accordingly
		// if (index < supportPoints.size() - 2) {
		// float halfRange = 1f / (SUBSTEPS_PER_CYCLE * 2);
		// int firstInterpolatedIndex = index;
		// for (index = lastInterpolatedIndex + 1; index >=
		// firstInterpolatedIndex; index--) {
		// if (FuzzyCompare.eq(supportPoints.get(index).x,
		// supportPoints.get(index - 1).x, halfRange)) {
		// supportPoints.remove(index - 1);
		// }
		// }
		// }

		initialize();
	}

	@Override
	public Vector2D moveSupportPoint(int index, float xMove, float yMove)
	{
		ISupportPoint point = supportPoints.get(index);

		return moveSupportPointTo(index, point.getX() + xMove, point.getY() + yMove);
	}

	@Override
	public Vector2D moveSupportPoint(ISupportPoint point, float xMove, float yMove)
	{
		int index = indexOf(point);
		if (index >= 0) {
			return moveSupportPointTo(index, point.getX() + xMove, point.getY() + yMove);
		}

		return null;
	}

	@Override
	public Vector2D moveSupportPointTo(ISupportPoint point, float x, float y)
	{
		int index = indexOf(point);
		if (index >= 0) {
			return moveSupportPointTo(index, x, y);
		}

		return null;
	}

	@Override
	public Vector2D moveSupportPointTo(int index, float x, float y)
	{
		SupportPoint point = supportPoints.remove(index);
		Vector2D previousPos = null;

		if (point != null) {
			previousPos = point.asVector2D();
			point.moveTo(x, y);
			addSupportPoint(point);
		}

		return previousPos;
	}

	@Override
	public Vector2D moveTangentPointBeforeTo(int index, float x, float y)
	{
		SupportPoint point = supportPoints.get(index);

		// If tangent point doesn't exist, do nothing
		if (point.tangentPointBefore == null) {
			return null;
		}

		Vector2D previousPos = point.tangentPointBefore.asVector2D();
		float halfRange = 1f / (SUBSTEPS_PER_CYCLE * 2);

		x = truncateX(x);

		if (x >= point.x - halfRange) {
			x = point.x - 1f / SUBSTEPS_PER_CYCLE;
		}

		point.tangentPointBefore.x = x;
		point.tangentPointBefore.y = y;

		initialize();

		return previousPos;
	}

	@Override
	public Vector2D moveTangentPointAfterTo(int index, float x, float y)
	{
		SupportPoint point = supportPoints.get(index);

		// If tangent point doesn't exist, do nothing
		if (point.tangentPointAfter == null) {
			return null;
		}

		Vector2D previousPos = point.tangentPointAfter.asVector2D();
		float halfRange = 1f / (SUBSTEPS_PER_CYCLE * 2);

		x = truncateX(x);

		if (x <= point.x + halfRange) {
			x = point.x + 1f / SUBSTEPS_PER_CYCLE;
		}

		point.tangentPointAfter.x = x;
		point.tangentPointAfter.y = y;

		initialize();

		return previousPos;
	}

	@Override
	public int addSupportPoint(float x, float y)
	{
		SupportPoint newSupportPoint = new SupportPoint(x, y);

		return addSupportPoint(newSupportPoint);
	}

	/**
	 * Add a new SupportPoint and re-initialize the function.
	 *
	 * @param newPoint - new SupportPoint
	 * @return the index, if the given SupportPoint could be inserted
	 *         successfully, -1 otherwise
	 */
	protected int addSupportPoint(SupportPoint newPoint)
	{
		if (supportPoints.contains(newPoint)) {
			return -1;
		}

		if (supportPoints.size() >= getMaximumSupportPointSize()) {
			return -1;
		}

		// Truncate x value of SupportPoint
		newPoint.x = truncateX(newPoint.x);

		// Search for index to insert the new SupportPoint
		int index = 0;
		float halfRange = 1f / (SUBSTEPS_PER_CYCLE * 2);

		while (index < supportPoints.size() && newPoint.x >= supportPoints.get(index).x - halfRange) {
			// If the current SupportPoint matches the new SupportPoint position,
			// move the new SupportPoint 1 / SUBSTEPS_PER_CYCLE in x direction
			if (FuzzyCompare.eq(supportPoints.get(index).x, newPoint.x, halfRange)) {
				newPoint.x += 1f / SUBSTEPS_PER_CYCLE;
			}

			index++;
		}

		// Add SupportPoint to list and reinitialize the function
		supportPoints.add(index, newPoint);
		initialize();

		return index;
	}

	@Override
	public SupportPoint removeSupportPoint(int index)
	{
		if (index >= 0 && index < supportPoints.size() && supportPoints.size() > getMinimumSupportPointSize()) {
			SupportPoint removedPoint = supportPoints.remove(index);
			initialize();

			return removedPoint;
		}

		return null;
	}

	/**
	 * Called after changes to support points. Default updates the min / max - x
	 * / y - values of all SupportPoints.
	 */
	public void initialize()
	{
		if (supportPoints.size() == 0) {
			minXValue = 0;
			maxXValue = 0;

			minYValue = 0;
			maxYValue = 0;
			return;
		}

		minXValue = supportPoints.get(0).getX();
		maxXValue = supportPoints.get(supportPoints.size() - 1).getX();
		minYValue = supportPoints.get(0).getY();
		maxYValue = minYValue;
		float yValue;

		for (int i = 1; i < supportPoints.size(); i++) {
			yValue = supportPoints.get(i).getY();
			if (yValue < minYValue) {
				minYValue = yValue;
			}

			if (yValue > maxYValue) {
				maxYValue = yValue;
			}
		}
	}

	protected float truncateX(float x)
	{
		return (int) (x * SUBSTEPS_PER_CYCLE) / SUBSTEPS_PER_CYCLE;
	}

	/**
	 * Approximates the first derivative at value t by calculating the difference
	 * quotient with a delta t of 0.001
	 * @param x the coordinate at which to return the functions first derivative
	 *        value
	 * @return the functions first derivative value at position x
	 */
	@Override
	public double derivative(double x)
	{
		double deltaT = FunctionUtil.DELTA_T;
		double y1 = value(x - deltaT);
		double y2 = value(x + deltaT);
		return FunctionUtil.derivative1(y1, y2, 2 * deltaT);
	}

	/**
	 * Approximates the second derivative at value t by calculating the
	 * difference quotient of the approximated first derivative with a delta t of
	 * 0.001
	 * @param x the coordinate at which to return the functions second derivative
	 *        value
	 * @return the functions first derivative value at position x
	 */
	@Override
	public double derivative2(double x)
	{
		double deltaT = FunctionUtil.DELTA_T;
		double y1 = value(x - deltaT);
		double y2 = value(x);
		double y3 = value(x + deltaT);
		return FunctionUtil.derivative2(y1, y2, y3, deltaT);
	}

	/**
	 * Sorts SupportPoints by their x coordinate
	 */
	public static class SupportPointComparator implements Comparator<SupportPoint>
	{
		@Override
		public int compare(SupportPoint o1, SupportPoint o2)
		{
			return Float.compare(o1.x, o2.x);
		}
	}
}