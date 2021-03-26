/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IAccelerometerPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.IAccelerometer;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implementation of an accelerometer sensor
 *
 * @author Ingo Schindler
 */
public class Accelerometer extends Sensor implements IAccelerometer
{
	private Vector3D acceleration;

	private Vector3D previousAcceleration;

	/**
	 * Instantiates a new Accelerometer sensor
	 *
	 * @param name Sensor Name
	 * @param perceptorName unique name of the perceptor
	 * @param pose the sensor pose relative to the body part it is mounted to
	 */
	public Accelerometer(String name, String perceptorName, IPose3D pose)
	{
		super(name, perceptorName, pose);
		acceleration = Vector3D.ZERO;
		previousAcceleration = Vector3D.ZERO;
	}

	public Accelerometer(ISensorConfiguration config)
	{
		this(config.getName(), config.getPerceptorName(), config.getPose());
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	private Accelerometer(Accelerometer source)
	{
		super(source);
		acceleration = source.acceleration;
		previousAcceleration = source.previousAcceleration;
	}

	@Override
	public Vector3D getAcceleration()
	{
		return acceleration;
	}

	@Override
	public Vector3D getPreviousAcceleration()
	{
		return previousAcceleration;
	}

	/**
	 * Set 3-dimensional accelerations
	 *
	 * @param acceleration Accelerations
	 */
	void setAcceleration(Vector3D acceleration)
	{
		previousAcceleration = this.acceleration;

		double x = Double.isNaN(acceleration.getX()) ? 0 : acceleration.getX();
		double y = Double.isNaN(acceleration.getY()) ? 0 : acceleration.getY();
		double z = Double.isNaN(acceleration.getZ()) ? 0 : acceleration.getZ();

		this.acceleration = new Vector3D(x, y, z);
	}

	/**
	 * Updates this accelerometer from perception
	 * @param perception the result from server message parsing
	 */
	@Override
	public void updateFromPerception(IPerception perception)
	{
		IAccelerometerPerceptor perceptor = perception.getAccelerationPerceptor(getPerceptorName());
		if (perceptor != null) {
			setAcceleration(perceptor.getAcceleration());
		}
	}

	@Override
	public ISensor copy()
	{
		return new Accelerometer(this);
	}
}
