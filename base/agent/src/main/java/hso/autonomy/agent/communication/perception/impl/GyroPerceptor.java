/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IGyroPerceptor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Gyro perceptor
 *
 * @author Simon Raffeiner
 */
public class GyroPerceptor extends Perceptor implements IGyroPerceptor
{
	private Vector3D vector;

	/**
	 * Default constructor, initializes the gyro to (0, 0, 0)
	 *
	 * @param name Perceptor name
	 */
	public GyroPerceptor(String name)
	{
		this(name, 0.0f, 0.0f, 0.0f);
	}

	public GyroPerceptor(String name, float rotationX, float rotationY, float rotationZ)
	{
		super(name);
		this.vector = new Vector3D(rotationX, rotationY, rotationZ);
	}

	@Override
	public Vector3D getGyro()
	{
		return vector;
	}
}
