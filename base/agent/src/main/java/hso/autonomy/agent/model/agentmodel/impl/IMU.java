/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IIMUPerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.IIMU;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;

/**
 * Implementation of an IMU Sensor.
 *
 * @author Stefan Glaser
 *
 */
public class IMU extends Sensor implements IIMU
{
	/** The absolute IMU orientation. */
	private Rotation originalOrientation;

	/** The drift compensated absolute IMU orientation. */
	private Rotation orientation;

	/** The offset rotation for compensating Gyroscope drift. */
	private Rotation offsetRot;

	/**
	 * Instantiates a new IMU sensor and initializes all values to their default
	 *
	 * @param name sensor name
	 * @param perceptorName unique name of the perceptor
	 * @param pose the sensor pose relative to the body part it is mounted to
	 */
	public IMU(String name, String perceptorName, IPose3D pose)
	{
		super(name, perceptorName, pose);

		originalOrientation = Rotation.IDENTITY;
		orientation = Rotation.IDENTITY;
		offsetRot = Rotation.IDENTITY;
	}

	public IMU(ISensorConfiguration config)
	{
		this(config.getName(), config.getPerceptorName(), config.getPose());
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	public IMU(IMU source)
	{
		super(source);

		originalOrientation = source.originalOrientation;
		orientation = source.orientation;
		offsetRot = source.offsetRot;
	}

	/**
	 * Retrieve the original orientation without drift compensation.
	 *
	 * @return the original orientation measured by the sensor
	 */
	public Rotation getOriginalOrientation()
	{
		return originalOrientation;
	}

	@Override
	public Rotation getOrientation()
	{
		return orientation;
	}

	@Override
	public Rotation getOffset()
	{
		return offsetRot;
	}

	@Override
	public Rotation setOffset(Rotation offset)
	{
		offsetRot = offset;

		// re-apply offset
		orientation = offsetRot.applyTo(originalOrientation);

		return orientation;
	}

	@Override
	public void updateFromPerception(IPerception perception)
	{
		IIMUPerceptor perceptor = perception.getIMUPerceptor(getPerceptorName());

		if (perceptor != null) {
			originalOrientation =
					pose.getOrientation().applyTo(perceptor.getOrientation()).applyTo(pose.getOrientation().revert());
			orientation = offsetRot.applyTo(originalOrientation);
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof IMU)) {
			return false;
		}
		IMU other = (IMU) o;
		if (!super.equals(other)) {
			return false;
		}

		return Rotation.distance(offsetRot, other.offsetRot) < Math.PI / 1800 &&
				Rotation.distance(originalOrientation, other.originalOrientation) < Math.PI / 1800;
	}

	@Override
	public ISensor copy()
	{
		return new IMU(this);
	}
}
