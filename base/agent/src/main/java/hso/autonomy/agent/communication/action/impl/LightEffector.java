/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.ILightEffector;

/**
 * Default implementation of a {@link ILightEffector}.
 *
 * @author Stefan Glaser
 */
public class LightEffector extends Effector implements ILightEffector
{
	/** The intended brightness of the light. */
	private final double brightness;

	public LightEffector(String name, double brightness)
	{
		super(name);

		this.brightness = brightness;
	}

	@Override
	public double getBrightness()
	{
		return brightness;
	}
}
