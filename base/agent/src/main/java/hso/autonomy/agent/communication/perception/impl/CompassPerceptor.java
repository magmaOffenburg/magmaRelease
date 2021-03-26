/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.ICompassPerceptor;
import hso.autonomy.util.geometry.Angle;

/**
 * @author david
 *
 */
public class CompassPerceptor extends Perceptor implements ICompassPerceptor
{
	private final Angle angle;

	public CompassPerceptor(String name, Angle angle)
	{
		super(name);
		this.angle = angle;
	}

	@Override
	public Angle getAngle()
	{
		return angle;
	}
}
