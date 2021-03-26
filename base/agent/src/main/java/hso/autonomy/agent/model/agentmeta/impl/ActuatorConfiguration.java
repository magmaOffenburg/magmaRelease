/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import java.io.Serializable;

/**
 * Base class for all actuator configurations.
 *
 * @author Stefan Glaser
 */
public class ActuatorConfiguration implements Serializable, IActuatorConfiguration
{
	/** The name of the actuator. */
	protected final String name;

	/** The name of the effector corresponding to this actuator. */
	protected final String effectorName;

	/** The pose of this actuator relative to the body part it is mounted on. */
	protected final IPose3D pose;

	public ActuatorConfiguration(String name, String effectorName)
	{
		this(name, effectorName, new Pose3D());
	}

	public ActuatorConfiguration(String name, String effectorName, IPose3D pose)
	{
		this.name = name;
		this.effectorName = effectorName;
		this.pose = pose;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getEffectorName()
	{
		return effectorName;
	}

	@Override
	public IPose3D getPose()
	{
		return pose;
	}
}
