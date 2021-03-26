/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action;

import java.awt.Color;

/**
 * Interface for a colored light effector.
 *
 * @author Stefan Glaser
 */
public interface IRGBLightEffector extends ILightEffector {
	/**
	 * Retrieve the intended color of the light.
	 *
	 * @return the intended color
	 */
	Color getColor();
}
