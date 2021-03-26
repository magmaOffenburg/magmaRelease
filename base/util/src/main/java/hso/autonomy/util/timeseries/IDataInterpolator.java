/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timeseries;

import java.io.Serializable;

/**
 * Interface for generic data interpolators.
 *
 * @author Stefan Glaser
 *
 * @param <D> The interpolated data type.
 */
public interface IDataInterpolator<D> extends Serializable {
	/**
	 * Interpolate between the data points <code>d1</code> and <code>d2</code> with the given ratio.
	 *
	 * @param d1 the first data point
	 * @param d2 the second data point
	 * @param ratio the interpolation ratio [0...1], where 0 corresponds to the value of <code>d1</code> and 1 to the
	 * value of <code>d2</code>
	 *
	 * @return the interpolated value
	 */
	D interpolateData(D d1, D d2, double ratio);
}
