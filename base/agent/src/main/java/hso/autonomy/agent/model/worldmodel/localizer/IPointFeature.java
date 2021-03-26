/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for a point feature that can be used for localization.
 *
 * @author Stefan Glaser
 */
public interface IPointFeature extends IGeometricFeature {
	/**
	 * @return the position at which we know the reference point is located
	 */
	Vector3D getKnownPosition();
}