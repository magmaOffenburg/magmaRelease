/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for representing a point feature configuration.
 *
 * @author Stefan Glaser
 */
public interface IPointFeatureConfiguration extends IGeometricFeatureConfiguration {
	/**
	 * Retrieve the known position of this point feature in the environment.
	 *
	 * @return the known position
	 */
	Vector3D getKnownPosition();
}
