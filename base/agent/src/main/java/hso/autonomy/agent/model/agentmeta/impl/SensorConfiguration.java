/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;

/**
 * Base class for all sensor configurations.
 *
 * @author Stefan Glaser
 */
public class SensorConfiguration implements ISensorConfiguration
{
	/** The name of the sensor. */
	protected final String name;

	/** The name of the perceptor corresponding to this sensor. */
	protected final String perceptorName;

	/** The pose of this sensor relative to the body part it is mounted on. */
	protected final IPose3D pose;

	public SensorConfiguration(String name, String perceptorName)
	{
		this(name, perceptorName, new Pose3D());
	}

	public SensorConfiguration(String name, String perceptorName, IPose3D pose)
	{
		this.name = name;
		this.perceptorName = perceptorName;
		this.pose = pose;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getPerceptorName()
	{
		return perceptorName;
	}

	@Override
	public IPose3D getPose()
	{
		return pose;
	}
}
