/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

/**
 * General interface for geometric features that can be used for localization.
 *
 * @author Stefan Glaser
 */
public interface IGeometricFeature {
	/**
	 * Retrieve the unique name of the feature.
	 *
	 * @return the unique name of the feature
	 */
	String getName();

	/**
	 * Retrieve the type information associated with this feature.
	 *
	 * @return the type information
	 */
	String getType();
}
