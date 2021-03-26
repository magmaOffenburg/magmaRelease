/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

/**
 * Base interface representing an observation of a {@link IGeometricFeature}.<br>
 * <br>
 * Depending on the underlying sensor information, features in the environment may be uniquely detected and / or labeled
 * with type information. In the latter case, some feature assignment process may uniquely assign observations to
 * features in the map.
 *
 * @author Stefan Glaser
 *
 * @see IGeometricFeature
 */
public interface IFeatureObservation {
	/**
	 * Retrieve the time when the observation was made.
	 *
	 * @return the time of this observation
	 */
	double getTime();

	/**
	 * @return the feature type
	 */
	String getType();

	/**
	 * Retrieve the unique name of the observed feature if available.<br>
	 * A valid name corresponds to the name of a successfully assigned feature of the map.
	 *
	 * @return the unique name of this feature if available, null otherwise
	 */
	String getName();

	/**
	 * Check if this feature has full 3D information (including depth) assigned to it.
	 *
	 * @return true, if this feature observation contains depth information, false otherwise
	 */
	boolean hasDepth();

	/**
	 * Assign the unique name of the observed feature to this observation.
	 *
	 * @param name the unique name of the feature
	 */
	void assign(String name);

	/**
	 * Check if this feature observation has been assigned to a {@link IGeometricFeature}.
	 *
	 * @return true, if this feature got assigned a feature name, false otherwise
	 */
	boolean isAssigned();
}
