/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Point feature observation that can be used for localization.
 *
 * @author Stefan Glaser
 */
public interface IPointFeatureObservation extends IFeatureObservation {
	/**
	 * @return the observed position under which we see the point feature
	 *         (relative to the camera)
	 */
	Vector3D getObservedPosition();

	/**
	 * @param position the observed position under which we see the point feature (relative to the camera)
	 */
	void assignDepthInfo(Vector3D position);
}
