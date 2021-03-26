/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action;

/**
 * Interface for a "HingeJoint" effector, used to move joints of a robot.
 *
 * @author Stefan Glaser
 */
public interface IHingeEffector extends IEffector {
	/**
	 * @return the desired angular speed of this effector in degrees
	 */
	float getSpeed();

	/**
	 * @return the desired angular position of this effector in degrees
	 */
	float getDesiredAngle();

	float getSpeedAtDesiredAngle();

	float getAccelerationAtDesiredAngle();
}
