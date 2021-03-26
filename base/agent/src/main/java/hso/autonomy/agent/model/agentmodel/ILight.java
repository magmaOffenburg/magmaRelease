/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

/**
 * Interface for a light actuator.
 *
 * @author Stefan Glaser
 */
public interface ILight extends IActuator {
	/**
	 * Turn the light off.<br>
	 * Corresponds to setting a brightness of one.
	 */
	void turnOn();

	/**
	 * Turn the light off.<br>
	 * Corresponds to setting a brightness of zero.
	 */
	void turnOff();

	/**
	 * Regulate the brightness of the light.
	 *
	 * @param brightness the intended brightness of the light
	 */
	void setBrightness(double brightness);

	/**
	 * Retrieve the intended brightness of the light.
	 *
	 * @return the intended brightness of the light
	 */
	double getBrightness();

	/**
	 * @return true if the brightness of this light greater than zero
	 */
	boolean isOn();

	/**
	 * @return true if the brightness of this light is zero
	 */
	boolean isOff();
}
