/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for representing a line feature configuration.
 *
 * @author Stefan Glaser
 */
public interface ILineFeatureConfiguration extends IGeometricFeatureConfiguration {
	/**
	 * Retrieve the first known position of this line feature in the environment.
	 *
	 * @return the first known position
	 */
	Vector3D getKnownPosition1();

	/**
	 * Retrieve the second known position of this line feature in the environment.
	 *
	 * @return the second known position
	 */
	Vector3D getKnownPosition2();
}
