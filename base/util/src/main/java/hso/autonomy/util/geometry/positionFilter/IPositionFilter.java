/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.positionFilter;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author kdorer
 */
public interface IPositionFilter {
	/**
	 * Execute a filter pass: takes the current position of an object, adds it to
	 * the internal buffer, and interpolates a (hopefully) more reliable
	 * estimate.
	 *
	 * @param newPosition an estimation of the new position
	 * @param oldPosition the old known position
	 * @param speed the speed of the object (in m/cycle) in the corresponding
	 *        coordinate frame
	 * @return Filtered position
	 */
	Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition, Vector3D speed);

	Vector3D filterPosition(Vector3D newPosition, Vector3D oldPosition);

	Vector3D filterPosition(Vector3D newPosition);

	/**
	 * Clears the history of the position filter.
	 */
	void reset();
}