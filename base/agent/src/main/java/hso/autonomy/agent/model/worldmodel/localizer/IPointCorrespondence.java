/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for representing a point correspondence in 3D. A point correspondence consists of a known position relative
 * to the map and an corresponding observed position relative to the localization frame.
 *
 * @author Stefan Glaser
 */
public interface IPointCorrespondence {
	/**
	 * Retrieve the known position in the map.
	 *
	 * @return the known position in the map
	 */
	Vector3D getKnownPosition();

	/**
	 * Retrieve the observed location
	 *
	 * @return the observed location
	 */
	Vector3D getObservedPosition();
}
