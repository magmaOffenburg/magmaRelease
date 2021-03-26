/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta;

import hso.autonomy.util.geometry.IPose3D;

/**
 * Base interface for all actuator configurations.
 *
 * @author Stefan Glaser
 */
public interface IActuatorConfiguration {
	/**
	 * Retrieve the name of the actuator.
	 *
	 * @return the actuator name
	 */
	String getName();

	/**
	 * Retrieve the effector name.
	 *
	 * @return the name of the effector
	 */
	String getEffectorName();

	/**
	 * Retrieve the pose where this actuator is mounted on its corresponding body part.
	 *
	 * @return the pose relative to the body part the actuator is mounted to
	 */
	IPose3D getPose();
}
