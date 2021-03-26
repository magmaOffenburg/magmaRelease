/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action;

/**
 * Interface for a  light effector.
 *
 * @author Stefan Glaser
 */
public interface ILightEffector extends IEffector {
	/**
	 * Retrieve the intended brightness of the light.
	 *
	 * @return the intended brightness
	 */
	double getBrightness();
}
