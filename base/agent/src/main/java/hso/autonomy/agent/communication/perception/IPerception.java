/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import java.util.List;
public interface IPerception {
	/**
	 * Get a specific Accelerometer Perceptor
	 *
	 * @param name Perceptor name
	 * @return Acceleration perceptor
	 */
	IAccelerometerPerceptor getAccelerationPerceptor(String name);

	/**
	 * Get a specific Gyro Perceptor
	 *
	 * @param name Perceptor name
	 * @return Gyro perceptor
	 */
	IGyroPerceptor getGyroRatePerceptor(String name);

	/**
	 * Get a specific Compass Perceptor
	 *
	 * @param name Perceptor name
	 * @return Compass perceptor
	 */
	ICompassPerceptor getCompassPerceptor(String name);

	/**
	 * Get a specific IMU Perceptor
	 *
	 * @param name Perceptor name
	 * @return IMU perceptor
	 */
	IIMUPerceptor getIMUPerceptor(String name);

	/**
	 * Get a specific Force Resistance Perceptor
	 *
	 * @param name Perceptor name
	 * @return perceptor
	 */
	IForceResistancePerceptor getForceResistancePerceptor(String name);

	/**
	 * Get a specific Visible Object
	 *
	 * @param name Perceptor name
	 * @return perceptor
	 */
	IVisibleObjectPerceptor getVisibleObject(String name);

	/**
	 * Get the global pose perceptor
	 *
	 * @return perceptor
	 */
	IGlobalPosePerceptor getGlobalPose();

	/**
	 * Get the global time perceptor
	 *
	 * @return perceptor
	 */
	ITimerPerceptor getTime();

	List<ILinePerceptor> getVisibleLines();

	List<IReferencePointPerceptor> getReferencePointPerceptor();

	/**
	 * Get a specific Universal Joint Perceptor
	 *
	 * @param name Perceptor name
	 * @return perceptor
	 */
	ICompositeJointPerceptor getCompositeJointPerceptor(String name);

	/**
	 * Get a specific Hinge Joint Perceptor
	 *
	 * @param name Perceptor name
	 * @return perceptor
	 */
	IHingeJointPerceptor getHingeJointPerceptor(String name);

	/**
	 * Get a specific Transform Perceptor
	 *
	 * @param name Perceptor name
	 * @return perceptor
	 */
	ITransformPerceptor getTransformPerceptor(String name);

	/**
	 * @return true, if the current perception contains vision information
	 */
	boolean containsVision();

	/**
	 * @return true, if the current perception contains vision information
	 */
	boolean containsMotion();

	void updatePerceptors(IPerceptorMap perceptor);

	/**
	 * Generic method for fetching typed perceptors.
	 *
	 * @param name the name of the requested perceptor
	 * @param clazz the class of the perceptor
	 * @return a perceptor of the specified class with the given name if such a perceptor exists, or null otherwise
	 */
	<T extends IPerceptor> T getPerceptor(String name, Class<T> clazz);
}
