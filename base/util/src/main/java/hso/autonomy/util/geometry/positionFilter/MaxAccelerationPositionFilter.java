/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interpolate a more reliable position value for an object from a list of past,
 * less reliable values
 *
 * @author sturmflut
 */
public class MaxAccelerationPositionFilter extends PositionFilterBase
{
	double oldSpeed;

	public MaxAccelerationPositionFilter()
	{
		super();
		oldSpeed = 0;
	}

	/**
	 * Constructor
	 *
	 * @param i Internal buffer size for past position values
	 */
	public MaxAccelerationPositionFilter(int i)
	{
		super(i);
		oldSpeed = 0;
	}

	@Override
	protected Vector3D calculateNewPosition()
	{
		Vector3D sum = new Vector3D(0, 0, 0);
		for (Vector3D v : filterBuffer) {
			sum = sum.add(v);
		}

		Vector3D newPos = sum.scalarMultiply(1f / filterBuffer.size());
		Vector3D prevPos = filterBuffer.peek();
		if (prevPos == null) {
			return newPos;
		}

		// check if we have an implausible acceleration
		Vector3D speed = newPos.subtract(prevPos);
		double newSpeed = speed.getNorm();
		if (Math.abs(newSpeed - oldSpeed) > 0.02) {
			newPos = prevPos.add(speed.scalarMultiply(0.8));
		}
		oldSpeed = newSpeed;
		return newPos;
	}
}
