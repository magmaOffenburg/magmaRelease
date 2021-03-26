/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.ICompassPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.ICompass;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose3D;

/**
 * Sweaty" sensor implementation.
 *
 * @author David Zimmermann
 */
public class Compass extends Sensor implements ICompass
{
	/** angular change to previous angle */
	private Angle angleToNorth;

	private Angle deviationAngle;

	/**
	 * Instantiates a new GyroRate sensor and initializes all values to their
	 * default
	 *
	 * @param name sensor name
	 * @param perceptorName unique name of the perceptor
	 * @param pose the sensor pose relative to the body part it is mounted to
	 */
	public Compass(String name, String perceptorName, IPose3D pose)
	{
		super(name, perceptorName, pose);
		angleToNorth = Angle.ZERO;
		deviationAngle = Angle.ZERO;
	}

	public Compass(ISensorConfiguration config)
	{
		this(config.getName(), config.getPerceptorName(), config.getPose());
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	public Compass(Compass source)
	{
		super(source);
		angleToNorth = source.angleToNorth;
		deviationAngle = source.deviationAngle;
	}

	@Override
	public Angle getAngle()
	{
		return angleToNorth.subtract(deviationAngle);
	}

	@Override
	public void setAngle(Angle angle)
	{
		this.angleToNorth = angle;
	}

	/**
	 * Updates this Compass from perception
	 * @param perception the result from server message parsing
	 */
	@Override
	public void updateFromPerception(IPerception perception)
	{
		ICompassPerceptor perceptor = perception.getCompassPerceptor(getPerceptorName());
		if (perceptor != null) {
			setAngle(perceptor.getAngle());
		}
	}

	@Override
	public ISensor copy()
	{
		return new Compass(this);
	}

	@Override
	public void reset()
	{
		deviationAngle = angleToNorth;
	}
}
