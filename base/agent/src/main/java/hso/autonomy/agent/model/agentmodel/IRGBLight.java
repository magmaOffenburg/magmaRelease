/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import java.awt.Color;

/**
 * Interface for an colored (RGB) light actuator.
 *
 * @author Stefan Glaser
 */
public interface IRGBLight extends ILight {
	/**
	 * Set the intended color of the light.<br>
	 * <b>Note:</b> Also affects brightness of the light.
	 *
	 * @param color the intended color
	 */
	void setColor(Color color);

	/**
	 * Retrieve the intended color of the light.
	 *
	 * @return the intended color
	 */
	Color getColor();
}
