/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for a body part configuration.
 *
 * @author Stefan Glaser
 */
public interface IBodyPartConfiguration extends Serializable {
	/**
	 * Returns the name of the body part.
	 *
	 * @return the name of the body part
	 */
	String getName();

	/**
	 * Returns the name of the parent body part.
	 *
	 * @return the name of the parent body part - or null if there is no parent body part.
	 */
	String getParent();

	/**
	 * Returns a vector that describes the position of the body part relative to it's parent.
	 *
	 * @return the body translation (meter) - or null if there is no parent body part.
	 */
	Vector3D getTranslation();

	/**
	 * Returns the mass of the body part.
	 *
	 * @return the body mass (kg)
	 */
	float getMass();

	/**
	 * Returns a vector, containing the geometry of the body part.
	 *
	 * @return the body geometry (meters)
	 */
	Vector3D getGeometry();

	/**
	 * Check if this body part is the root body part.
	 *
	 * @return true, if this body part is the root body part, false otherwise
	 */
	boolean isRootBody();

	/**
	 * Returns the JointConfiguration object to that body part.
	 *
	 * @return the joint configuration - or null if there is no joint attached
	 */
	IHingeJointConfiguration getJointConfiguration();

	/**
	 * Returns the anchor of the joint, relative to the body part.
	 *
	 * @return the anchor of the joint - or null if there is no joint attached
	 */
	Vector3D getJointAnchor();

	/**
	 * Returns the configuration of the gyro-rate sensor inside the body part.
	 *
	 * @return the configuration of the gyro-rate sensor - or null if the body contains no such sensor
	 */
	ISensorConfiguration getGyroRateConfiguration();

	/**
	 * Returns the configuration of the accelerometer inside the body part.
	 *
	 * @return the configuration of the accelerometer - or null if the body contains no such sensor
	 */
	ISensorConfiguration getAccelerometerConfiguration();

	/**
	 * Returns the configuration of the force-resistance sensor inside the body part.
	 *
	 * @return the configuration of the force-resistance sensor - or null if the body contains no such sensor
	 */
	ISensorConfiguration getForceResistanceConfiguration();

	/**
	 * Returns the configuration of the IMU sensor inside the body part.
	 *
	 * @return the configuration of the IMU sensor - or null if the body contains no such sensor
	 */
	ISensorConfiguration getIMUConfiguration();

	/**
	 * Returns the configuration of the compass sensor inside the body part.
	 *
	 * @return the configuration of the compass sensor - or null if the body contains no such sensor
	 */
	ISensorConfiguration getCompassConfig();

	/**
	 * Returns the configuration of the camera sensor inside the body part.
	 *
	 * @return the configuration of the camera sensor - or null if the body contains no such sensor
	 */
	ICameraConfiguration getCameraConfig();

	/**
	 * Returns the list of configurations of the light actuators inside the body part.
	 *
	 * @return the list ofconfigurations of the light actuators - or null if the body contains no such actuators
	 */
	List<IActuatorConfiguration> getLightConfigs();
}
