/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.function;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Base class for all functions used as motor functions.
 * @author Klaus Dorer
 */
public interface IFunction extends Serializable {
	/**
	 * Returns the name of the function
	 *
	 * @return the name of the function
	 */
	String getName();

	/**
	 * Returns the function value at the passed x coordinate.
	 * @param x the coordinate at which to return the functions value
	 * @return the function value at the passed x coordinate
	 */
	double value(double x);

	/**
	 * Approximates the first derivative at value t by calculating the difference
	 * quotient with a delta t of 0.001
	 * @param x the coordinate at which to return the functions first derivative
	 *        value
	 * @return the functions first derivative value at position x
	 */
	double derivative(double x);

	/**
	 * Approximates the second derivative at value t by calculating the
	 * difference quotient of the approximated first derivative with a delta t of
	 * 0.001
	 * @param x the coordinate at which to return the functions second derivative
	 *        value
	 * @return the functions first derivative value at position x
	 */
	double derivative2(double x);

	/**
	 * Returns a String with comma separated values of this function to be used
	 * e.g. for file writing
	 * @return the String with comma separated values
	 */
	String toCSVString();

	float getStiffness();

	/**
	 * @return the maximal x value of all SupportPoints
	 */
	float getMaxX();

	/**
	 * @return the minimal x value of all SupportPoints
	 */
	float getMinX();

	/**
	 * @return the maximal y value of all SupportPoints
	 */
	float getMaxY();

	/**
	 * @return the minimal y value of all SupportPoints
	 */
	float getMinY();

	/**
	 * The definition range (minX to maxX) of all SupportPoints
	 *
	 * @return the definition range
	 */
	float getDefinitionRange();

	/**
	 * Check it this function has at least one SupportPoints in the given area.
	 *
	 * @param minX - the lower x bound
	 * @param maxX - the upper x bound
	 * @param minY - the lower y bound
	 * @param maxY - the upper y bound
	 * @return true if in the given area is at least one SupportPoint
	 */
	boolean hasSupportPointsInArea(float minX, float maxX, float minY, float maxY);

	/**
	 * Get the SupportPoints of this function in the given area.
	 *
	 * @param minX - the lower x bound
	 * @param maxX - the upper x bound
	 * @param minY - the lower y bound
	 * @param maxY - the upper y bound
	 * @return a list of SupportPoints in that area
	 */
	List<ISupportPoint> getSupportPointsInArea(float minX, float maxX, float minY, float maxY);

	/**
	 * @return a copy of this object
	 */
	IFunction copy();

	/**
	 * @return the number of support points
	 */
	int getSupportPointSize();

	/**
	 * Retrieve the index of the given SupportPoint.
	 *
	 * @param supportPoint - the SupportPoint
	 * @return the index of the given SupportPoint
	 */
	int indexOf(ISupportPoint supportPoint);

	/**
	 * Retrieve the SupportPoint at the given index
	 *
	 * @param index - the index of the SupportPoint
	 * @return the SupportPoint at the given index
	 */
	ISupportPoint getSupportPoint(int index);

	/**
	 * Move function in space. This method moves all SupportPoint simultaneously
	 * to shift the whole function.
	 *
	 * @param xShift - the shift in x direction
	 * @param yShift - the shift in y direction
	 */
	void moveFunction(float xShift, float yShift);

	/**
	 * Mirror function so that all support points get negated y coordinates.
	 */
	void mirrorYFunction();

	/**
	 * Re-scale a given area of the function. By re-scaling we describe the
	 * operation where all support points after the end position are shifted
	 * about (newEnd - end) and all support points within the range described by
	 * start and end are re-interpolated to the range described by start and
	 * newEnd.
	 *
	 * @param start - the start of the range
	 * @param end - the end of the range
	 * @param newEnd - the new end of the range
	 */
	void rescaleXRange(float start, float end, float newEnd);

	/**
	 * @param point - the SupportPoint to move
	 * @param xMove - the shift in the x coordinate of the SupportPoint
	 * @param yMove - the shift in the y coordinate of the SupportPoint
	 * @return the SupportPoint if move operation was (maybe partially)
	 *         successful, null otherwise
	 */
	Vector2D moveSupportPoint(ISupportPoint point, float xMove, float yMove);

	/**
	 * @param index - the index of the SupportPoint to move
	 * @param xMove - the shift in the x coordinate of the SupportPoint
	 * @param yMove - the shift in the y coordinate of the SupportPoint
	 * @return the SupportPoint if move operation was (maybe partially)
	 *         successful, null otherwise
	 */
	Vector2D moveSupportPoint(int index, float xMove, float yMove);

	/**
	 * @param point - the SupportPoint to move
	 * @param x - the new x coordinate of the SupportPoint
	 * @param y - the new y coordinate of the SupportPoint
	 * @return the SupportPoint if move operation was (maybe partially)
	 *         successful, null otherwise
	 */
	Vector2D moveSupportPointTo(ISupportPoint point, float x, float y);

	/**
	 * @param index - the index of the SupportPoint to move
	 * @param x - the new x coordinate of the SupportPoint
	 * @param y - the new y coordinate of the SupportPoint
	 * @return the SupportPoint if move operation was (maybe partially)
	 *         successful, null otherwise
	 */
	Vector2D moveSupportPointTo(int index, float x, float y);

	/**
	 * @param index - the index of the SupportPoint which tangent point before
	 *        should be moved
	 * @param x - the new x coordinate of the SupportPoint tangent point before
	 * @param y - the new y coordinate of the SupportPoint tangent point before
	 * @return the SupportPoint if move operation was (maybe partially)
	 *         successful, null otherwise
	 */
	Vector2D moveTangentPointBeforeTo(int index, float x, float y);

	/**
	 * @param index - the index of the SupportPoint which tangent point after
	 *        should be moved
	 * @param x - the new x coordinate of the SupportPoint tangent point after
	 * @param y - the new y coordinate of the SupportPoint tangent point after
	 * @return the SupportPoint if move operation was (maybe partially)
	 *         successful, null otherwise
	 */
	Vector2D moveTangentPointAfterTo(int index, float x, float y);

	/**
	 * Add a new SupportPoint at the given position.
	 *
	 * @param x - the x coordinate of the new SupportPoint
	 * @param y - the y coordinate of the new SupportPoint
	 * @return the new SupportPoint if insertion was successful, null otherwise
	 */
	int addSupportPoint(float x, float y);

	/**
	 * Remove the SupportPoint at the given index if possible.<br>
	 * <b>Note:</b> The first and last SupportPoint can not be removed due to
	 * function restrictions.
	 *
	 * @param index - the index of the SupportPoint to remove
	 * @return the removed SupportPoint if removal was successful, null otherwise
	 */
	ISupportPoint removeSupportPoint(int index);
}