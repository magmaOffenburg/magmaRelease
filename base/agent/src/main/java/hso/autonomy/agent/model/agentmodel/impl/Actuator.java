/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.agent.model.agentmodel.IActuator;
import hso.autonomy.util.geometry.IPose3D;
import java.io.Serializable;

/**
 * Base class for all actuators.
 *
 * @author Stefan Glaser
 */
public abstract class Actuator implements Serializable, IActuator
{
	/** The name of the actuator. */
	protected final String name;

	/** The name of the effector corresponding to this actuator. */
	protected final String effectorName;

	/** The pose of the actuator (mounting point and orientation relative to parent body part). */
	protected final IPose3D pose;

	public Actuator(String name, String effectorName, IPose3D pose)
	{
		this.name = name;
		this.effectorName = effectorName;
		this.pose = pose;
	}

	public Actuator(IActuatorConfiguration config)
	{
		this(config.getName(), config.getEffectorName(), config.getPose());
	}

	public Actuator(Actuator other)
	{
		this.name = other.name;
		this.effectorName = other.effectorName;
		this.pose = other.pose;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public IPose3D getRelativePose()
	{
		return pose;
	}
}
