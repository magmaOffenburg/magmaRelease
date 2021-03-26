/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IRGBLightEffector;
import java.awt.Color;

/**
 * Default implementation of a {@link IRGBLightEffector}.
 *
 * @author Stefan Glaser
 */
public class RGBLightEffector extends LightEffector implements IRGBLightEffector
{
	/** The intended color of the light.  */
	private final Color color;

	public RGBLightEffector(String name, double brightness, Color color)
	{
		super(name, brightness);

		this.color = color;
	}

	@Override
	public Color getColor()
	{
		return color;
	}
}
