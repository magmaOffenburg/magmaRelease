/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * "GyroRate" sensor interface. Provides read-only access only.<br>
 * The GyroRate Sensor represents the orientation change of a body in the
 * 3-dimensional space, respective to the global (and other) coordinate system.
 * This orientation change is represented as quaternion in a {@link Rotation}
 * object. The perceived representation is Euler-angles given in the
 * gyro-vector.
 *
 * <p>
 * <strong>Note:</strong> Relative to the Robot (center of it's torso), the
 * x-axis points to the right, while the y-axis is pointing forward and the
 * z-axis go's straight up.<br>
 * </p>
 *
 * @author Stefan Glaser, Srinivasa Ragavan
 */
public interface IGyroRate extends ISensor {
	/**
	 * Retrieve gyro values <br>
	 * where &lt;x, y, z&gt; is a vector containing the angle-change around the
	 * corresponding axis of the previous cycle in degrees per second <br>
	 *
	 * @return The change in orientation of the body with respect to the global
	 *         coordinate system of the previous cycle
	 */
	Vector3D getGyro();

	Vector3D getPreviousGyro();

	/**
	 * Retrieve the current change in orientation
	 *
	 * @return The current change in orientation
	 */
	Rotation getOrientationChange(float cycleTime);
}