/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import java.util.Iterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interpolate a more reliable position value for an object from a list of past,
 * less reliable values, but accept high position changes unfiltered
 *
 * @author Klaus Dorer
 */
public class LowFrequencyPositionFilter extends PositionFilter
{
	public LowFrequencyPositionFilter()
	{
		super();
	}

	/**
	 * Constructor
	 *
	 * @param i Internal buffer size for past position values
	 */
	public LowFrequencyPositionFilter(int i)
	{
		super(i);
	}

	/**
	 * Execute a filter pass: takes the current position of an object, adds it to
	 * the internal buffer, and interpolates a (hopefully) more reliable
	 * estimate.
	 *
	 * @return Filtered position
	 */
	@Override
	public Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition)
	{
		if (newPosition == null)
			return null;

		Vector3D speed = getAverageSpeed();
		if (filterBuffer.size() > 1) {
			Vector3D oldPos = filterBuffer.peek();
			if (newPosition.subtract(oldPos.add(speed)).getNorm() > 0.2) {
				// we accept very high changes
				// System.out.println("clear filterbuffer: " + oldPos.add(speed)
				// + " new: " + position);
				reset();
				filterBuffer.push(oldPos);
			}
		}
		return super.filterPosition(newPosition, oldPosition);
	}

	private Vector3D getAverageSpeed()
	{
		Vector3D speed = new Vector3D(0, 0, 0);
		if (filterBuffer.size() < 2) {
			return speed;
		}

		Iterator<Vector3D> filterIterator = filterBuffer.iterator();
		Vector3D newPosition = filterIterator.next();
		while (filterIterator.hasNext()) {
			Vector3D oldPosition = filterIterator.next();
			speed = speed.add(newPosition.subtract(oldPosition));
			newPosition = oldPosition;
		}
		return speed.scalarMultiply(1f / (filterBuffer.size() - 1));
	}
}
