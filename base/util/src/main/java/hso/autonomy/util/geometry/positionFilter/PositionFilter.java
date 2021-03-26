/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Interpolate a more reliable position value for an object from a list of past,
 * less reliable values
 *
 * @author sturmflut
 */
public class PositionFilter extends PositionFilterBase
{
	public PositionFilter()
	{
		super();
	}

	/**
	 * Constructor
	 *
	 * @param i Internal buffer size for past position values
	 */
	public PositionFilter(int i)
	{
		super(i);
	}

	@Override
	protected Vector3D calculateNewPosition()
	{
		Vector3D sum = new Vector3D(0, 0, 0);
		for (Vector3D v : filterBuffer) {
			sum = sum.add(v);
		}

		return sum.scalarMultiply(1f / filterBuffer.size());
	}

	public Vector3D filterOdometry(Vector2D odomTrans, double height)
	{
		if (odomTrans == null) {
			return null;
		}

		Vector3D prevPos = Vector3D.ZERO;
		if (!filterBuffer.isEmpty()) {
			prevPos = filterBuffer.getFirst();
		}
		filterBuffer.push(new Vector3D(prevPos.getX() + odomTrans.getX(), prevPos.getY() + odomTrans.getY(), height));

		if (filterBuffer.size() > bufferSize) {
			filterBuffer.pollLast();
		}

		return calculateNewPosition();
	}

	public Vector3D getUnfilteredPosition()
	{
		if (!filterBuffer.isEmpty()) {
			return filterBuffer.getFirst();
		}

		return Vector3D.ZERO;
	}
}
