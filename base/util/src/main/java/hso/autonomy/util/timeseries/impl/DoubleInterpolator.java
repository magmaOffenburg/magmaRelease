/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timeseries.impl;

import hso.autonomy.util.timeseries.IDataInterpolator;

/**
 * Interpolator for {@link Double} data.
 *
 * @author Stefan Glaser
 */
public class DoubleInterpolator implements IDataInterpolator<Double>
{
	@Override
	public Double interpolateData(Double d1, Double d2, double ratio)
	{
		if (ratio <= 0) {
			return d1;
		} else if (ratio >= 1) {
			return d2;
		}

		return d1 + (d2 - d1) * ratio;
	}
}
