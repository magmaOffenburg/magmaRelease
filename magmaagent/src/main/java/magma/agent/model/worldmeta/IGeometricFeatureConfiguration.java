/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta;

import java.io.Serializable;

/**
 * Base interface for all geometric feature configurations.
 *
 * @author Stefan Glaser
 */
public interface IGeometricFeatureConfiguration extends Serializable {
	/**
	 * Retrieve the unique name of the feature.
	 *
	 * @return the unique name
	 */
	String getName();

	/**
	 * Retrieve the feature type information.
	 *
	 * @return the type information
	 */
	String getType();
}
