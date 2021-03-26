/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.orientationFilter;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

public interface IOrientationFilter {
	/**
	 * Execute a filter pass: takes the current orientation of an object, adds it
	 * to the internal buffer, and interpolates a (hopefully) more reliable
	 * estimate.
	 *
	 * @param newOrientation an estimation of the new orientation
	 * @return Filtered position
	 */
	Rotation filterOrientation(Rotation newOrientation);

	/**
	 * Clears the history of the position filter.
	 */
	void reset();
}
