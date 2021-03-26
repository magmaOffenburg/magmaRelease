/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.agent.model.agentmeta.ICameraConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;

/**
 * Factory for creating sensor instances.
 *
 * @author Stefan Glaser
 */
public interface ISensorFactory {
	/**
	 * Create a new hinge joint instance from the given sensor configuration.
	 *
	 * @return a new hinge joint instance
	 */
	IHingeJoint createHingeJoint(IHingeJointConfiguration config);

	/**
	 * Create a new gyro-rate sensor instance from the given configuration.
	 *
	 * @return a new gyro-rate sensor instance
	 */
	IGyroRate createGyroRate(ISensorConfiguration config);

	/**
	 * Create a new accelerometer sensor instance from the given configuration.
	 *
	 * @return a new accelerometer sensor instance
	 */
	IAccelerometer createAccelerometer(ISensorConfiguration config);

	/**
	 * Create a new force-resistance sensor instance from the given configuration.
	 *
	 * @return a new force-resistance sensor instance
	 */
	IForceResistance createForceResistance(ISensorConfiguration config);

	/**
	 * Create a new IMU sensor instance from the given configuration.
	 *
	 * @return a new IMU sensor instance
	 */
	IIMU createIMU(ISensorConfiguration config);

	/**
	 * Create a new compass sensor instance from the given configuration.
	 *
	 * @return a new compass sensor instance
	 */
	ICompass createCompass(ISensorConfiguration config);

	/**
	 * Create a new camera sensor instance from the given configuration.
	 *
	 * @return a new camera sensor instance
	 */
	ICamera createCamera(ICameraConfiguration config);

	/**
	 * Create a new light actuator instance from the given configuration.
	 *
	 * @return a new light actuator instance
	 */
	ILight createLight(IActuatorConfiguration config);
}
