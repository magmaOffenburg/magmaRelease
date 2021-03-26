/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * @author Stefan Glaser
 */
public interface IIMU extends ISensor {
	/**
	 * Retrieve the corrected IMU orientation.
	 *
	 * @return the corrected orientation of the IMU
	 */
	Rotation getOrientation();

	/**
	 * Retrieve the original (not corrected) IMU orientation.
	 *
	 * @return the original (not corrected) orientation of the IMU
	 */
	Rotation getOriginalOrientation();

	/**
	 * Retrieve the current gyro offset rotation.
	 *
	 * @return the current gyro offset rotation
	 */
	Rotation getOffset();

	/**
	 * Set a new gyro offset rotation.
	 *
	 * @param offset the new gyro offset rotation
	 * @return the orientation of the IMU after re-applying the new offset
	 */
	Rotation setOffset(Rotation offset);
}
