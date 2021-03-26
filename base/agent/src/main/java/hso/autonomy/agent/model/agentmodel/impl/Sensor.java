/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import java.io.Serializable;
import java.util.Map;

/**
 * Base class for all agent sensors
 *
 * @author Klaus Dorer
 */
public abstract class Sensor implements Serializable, ISensor
{
	/** The name of the sensor. */
	protected final String name;

	/** The name of the corresponding perceptor. */
	protected final String perceptorName;

	/** The pose of the sensor (mounting point and orientation relative to parent body part). */
	protected final IPose3D pose;

	/**
	 * Instantiate a new sensor
	 *
	 * @param name Sensor name
	 */
	public Sensor(String name, String perceptorName, IPose3D pose)
	{
		this.name = name;
		this.perceptorName = perceptorName;
		this.pose = pose;
	}

	/**
	 * Copy constructor
	 * @param source to copy from
	 */
	protected Sensor(Sensor source)
	{
		this.name = source.name;
		this.perceptorName = source.perceptorName;
		this.pose = source.pose;
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Get perceptor name
	 *
	 * @return Perceptor name
	 */
	public String getPerceptorName()
	{
		return perceptorName;
	}

	@Override
	public IPose3D getRelativePose()
	{
		return pose;
	}

	@Override
	public abstract void updateFromPerception(IPerception perception);

	@Override
	public abstract ISensor copy();

	@Override
	public void updateSensors(Map<String, ISensor> flatSensors, Map<String, ISensor> structuredSensors)
	{
		flatSensors.put(getName(), this);
		structuredSensors.put(getName(), this);
	}

	@Override
	public void updateNoPerception()
	{
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Sensor)) {
			return false;
		}
		return name.equals(((Sensor) obj).name);
	}

	@Override
	public int hashCode()
	{
		return name.hashCode() + super.hashCode();
	}

	@Override
	public String toString()
	{
		return name;
	}
}
