/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.orientationFilter;

import hso.autonomy.util.geometry.Geometry;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

public class OrientationFilter extends OrientationFilterBase
{
	public OrientationFilter()
	{
	}

	public OrientationFilter(int i)
	{
		super(i);
	}

	@Override
	protected Rotation calculateNewOrientation()
	{
		return Geometry.getAverageRotation(filterBuffer);
	}
}
