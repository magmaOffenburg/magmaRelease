/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * A model for describing the environment via various geometric features. It provides maps for point and line features.
 *
 * @author Stefan Glaser
 */
public interface IFeatureMap extends Serializable {
	/**
	 * Retrieve the map of known point features.
	 *
	 * @return the known points features
	 */
	Map<String, IPointFeature> getPointFeatures();

	/**
	 * Retrieve a collection of known point features with the given type.
	 *
	 * @return a collection of points features with the given type
	 */
	Collection<IPointFeature> getPointFeatures(String type);

	/**
	 * Retrieve the point feature with the given name.
	 *
	 * @return the point feature with the given name, or null if no such feature exists
	 */
	IPointFeature getPointFeature(String name);

	/**
	 * Set the map of point features.
	 *
	 * @param pointFeatures the new map of point features
	 */
	void setPointFeatures(Map<String, IPointFeature> pointFeatures);

	/**
	 * Add a new point feature to the list of point features.
	 *
	 * @param pointFeature the new point feature
	 */
	void addPointFeature(IPointFeature pointFeature);

	/** Clear the list of point features in this map. */
	void clearPointFeatures();

	/**
	 * Retrieve the map of known line features.
	 *
	 * @return the known line features
	 */
	Map<String, ILineFeature> getLineFeatures();

	/**
	 * Retrieve a collection of known line features with the given type.
	 *
	 * @return a collection of known line features with the given type
	 */
	Collection<ILineFeature> getLineFeatures(String type);

	/**
	 * Retrieve the line feature with the given name.
	 *
	 * @return the line feature with the given name, or null if no such feature exists
	 */
	ILineFeature getLineFeature(String name);

	/**
	 * Set the list of line features.
	 *
	 * @param lineFeatures the new map of line features
	 */
	void setLineFeatures(Map<String, ILineFeature> lineFeatures);

	/**
	 * Add a new point feature to the map of line features.
	 *
	 * @param lineFeature the new line feature
	 */
	void addLineFeature(ILineFeature lineFeature);

	/** Clear the map of line features in this map. */
	void clearLineFeatures();

	/** Clear feature maps in this map. */
	void clear();
}
