/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IAccelerometerPerceptor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Acceleration perceptor
 *
 * @author Simon Raffeiner
 */
public class AccelerometerPerceptor extends Perceptor implements IAccelerometerPerceptor
{
	private Vector3D vector;

	/**
	 * Default constructor, initializes the accelerometer to (0, 0, 0)
	 *
	 * @param name Perceptor name
	 */
	public AccelerometerPerceptor(String name)
	{
		this(name, 0.0f, 0.0f, 0.0f);
	}

	public AccelerometerPerceptor(String name, float accelX, float accelY, float accelZ)
	{
		super(name);
		this.vector = new Vector3D(accelX, accelY, accelZ);
	}

	@Override
	public Vector3D getAcceleration()
	{
		return vector;
	}
}
