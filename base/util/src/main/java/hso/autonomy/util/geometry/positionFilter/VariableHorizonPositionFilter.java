/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import hso.autonomy.util.misc.ValueUtil;

/**
 * Position filter with a variable filter horizon.
 *
 * @author Stefan Glaser
 */
public class VariableHorizonPositionFilter extends PositionFilter
{
	/** The minimum number of elements in the filter buffer. */
	protected final int minBufferSize;

	/** The maximum number of elements in the filter buffer. */
	protected final int maxBufferSize;

	public VariableHorizonPositionFilter(int minBufferSize, int maxBufferSize)
	{
		super(maxBufferSize);

		this.minBufferSize = minBufferSize;
		this.maxBufferSize = maxBufferSize;
	}

	public void setHorizon(double horizon)
	{
		horizon = ValueUtil.limitValue(horizon, 0, 1);
		bufferSize = (int) (minBufferSize + (maxBufferSize - minBufferSize) * horizon);

		// shrink buffer to new horizon if necessary
		while (filterBuffer.size() > bufferSize) {
			filterBuffer.pollLast();
		}
	}
}
