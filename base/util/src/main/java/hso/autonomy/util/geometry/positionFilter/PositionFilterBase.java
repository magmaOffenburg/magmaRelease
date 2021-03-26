/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import java.util.LinkedList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public abstract class PositionFilterBase extends BaseFilter
{
	protected static final int DEFAULT_BUFFER_SIZE = 5;

	protected final LinkedList<Vector3D> filterBuffer = new LinkedList<>();

	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	public PositionFilterBase()
	{
	}

	/**
	 * Constructor
	 *
	 * @param i Internal buffer size for past position values
	 */
	public PositionFilterBase(int i)
	{
		bufferSize = i;
	}

	/**
	 * Execute a filter pass: takes the current position of an object, adds it to
	 * the internal buffer, and interpolates a (hopefully) more reliable
	 * estimate.
	 *
	 * @return Filtered position
	 */
	@Override
	public Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition, Vector3D speed)
	{
		if (newPosition == null)
			return null;

		filterBuffer.push(newPosition);
		if (filterBuffer.size() > bufferSize)
			filterBuffer.pollLast();

		return calculateNewPosition();
	}

	protected abstract Vector3D calculateNewPosition();

	/**
	 * Clears the history of the position filter.
	 */
	@Override
	public void reset()
	{
		filterBuffer.clear();
	}
}
