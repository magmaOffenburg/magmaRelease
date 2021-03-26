/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.util.geometry.IPose3D;

/**
 * Base interface for all actuators.
 *
 * @author Stefan Glaser
 */
public interface IActuator {
	/**
	 * Retrieve the unique actuator name.
	 * @return actuator name
	 */
	String getName();

	/**
	 * Retrieve the pose of this actuator, relative to its parent body.
	 * @return actuator pose
	 */
	IPose3D getRelativePose();

	/**
	 * Trigger the generation of an actuator action based on the internal state of this actuator.
	 *
	 * @return the effector encapsulating the actuator action
	 */
	IEffector generateAction();

	/**
	 * @return a deep copy of this actuator
	 */
	IActuator copy();
}
