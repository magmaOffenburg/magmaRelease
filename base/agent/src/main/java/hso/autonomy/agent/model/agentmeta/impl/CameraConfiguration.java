/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.ICameraConfiguration;
import hso.autonomy.util.geometry.Angle;

/**
 * Class for representing a camera sensor configuration.
 *
 * @author Stefan Glaser
 */
public class CameraConfiguration extends SensorConfiguration implements ICameraConfiguration
{
	/** The horizontal field of view range (+/- angle). */
	private final Angle horizontal;

	/** The vertical field of view range (+/- angle). */
	private final Angle vertical;

	public CameraConfiguration(String name, String perceptorName, Angle horizontal, Angle vertical)
	{
		super(name, perceptorName);

		this.horizontal = horizontal;
		this.vertical = vertical;
	}

	@Override
	public Angle getHorizontalFoVRange()
	{
		return horizontal;
	}

	@Override
	public Angle getVerticalFoVRange()
	{
		return vertical;
	}
}
