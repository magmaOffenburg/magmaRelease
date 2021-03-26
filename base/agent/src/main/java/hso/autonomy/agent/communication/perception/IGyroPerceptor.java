/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * The Gyro perceptor measures the orientation in the three-dimensional space.
 *
 * @author Simon Raffeiner
 */
public interface IGyroPerceptor extends IPerceptor {
	/**
	 * Get gyro values
	 */
	Vector3D getGyro();
}