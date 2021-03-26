/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for a line feature that can be used for localization.
 *
 * @author Stefan Glaser
 */
public interface ILineFeature extends IGeometricFeature {
	/**
	 * @return the position at which we know the first reference point is located
	 */
	Vector3D getKnownPosition1();

	/**
	 * @return the position at which we know the second reference point is
	 *         located
	 */
	Vector3D getKnownPosition2();
}
