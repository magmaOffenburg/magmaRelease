/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timeseries.impl;

import hso.autonomy.util.timeseries.IDataInterpolator;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;

/**
 * Interpolator for {@link Rotation} data.
 *
 * @author Stefan Glaser
 */
public class RotationInterpolator implements IDataInterpolator<Rotation>
{
	@Override
	public Rotation interpolateData(Rotation d1, Rotation d2, double ratio)
	{
		if (ratio <= 0) {
			return d1;
		} else if (ratio >= 1) {
			return d2;
		}

		Rotation diffRot = d1.applyInverseTo(d2);
		diffRot = new Rotation(diffRot.getAxis(RotationConvention.VECTOR_OPERATOR), diffRot.getAngle() * ratio,
				RotationConvention.VECTOR_OPERATOR);
		return d1.applyTo(diffRot);
	}
}
