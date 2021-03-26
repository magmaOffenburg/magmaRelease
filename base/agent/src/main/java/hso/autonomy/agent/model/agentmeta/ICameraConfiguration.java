/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta;

import hso.autonomy.util.geometry.Angle;

/**
 * Interface for a camera sensor configuration.
 *
 * @author Stefan Glaser
 */
public interface ICameraConfiguration extends ISensorConfiguration {
	/**
	 * Retrieve the horizontal field of view range (+/- angle).
	 *
	 * @return the horizontal field of view angle
	 */
	Angle getHorizontalFoVRange();

	/**
	 * Retrieve the vertical field of view range (+/- angle).
	 *
	 * @return the vertical field of view angle
	 */
	Angle getVerticalFoVRange();
}
