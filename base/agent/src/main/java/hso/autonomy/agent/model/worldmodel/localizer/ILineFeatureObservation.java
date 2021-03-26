/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel.localizer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Line feature observation that can be used for localization.
 *
 * @author Stefan Glaser
 */
public interface ILineFeatureObservation extends IFeatureObservation {
	/**
	 * @return the first observed position under which we see the line (relative
	 *         to the camera)
	 */
	Vector3D getObservedPosition1();

	/**
	 * @return the second observed position under which we see the line (relative
	 *         to the camera)
	 */
	Vector3D getObservedPosition2();

	/**
	 * @param position1 the first position under which we see the first end of line feature (relative to the camera)
	 * @param position2 the first position under which we see the second end of line feature (relative to the camera)
	 */
	void assignDepthInfo(Vector3D position1, Vector3D position2);

	/**
	 * Assign the unique name of the observed feature to this observation.
	 *
	 * @param name the unique name of the feature
	 * @param swapEnds true, of the two observed positions should be swapped, false otherwise
	 */
	void assign(String name, boolean swapEnds);
}
