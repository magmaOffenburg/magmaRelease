/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.impl.LightEffector;
import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.agent.model.agentmodel.IActuator;
import hso.autonomy.agent.model.agentmodel.ILight;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.misc.ValueUtil;

/**
 * Default implementation of a {@link ILight} actuator.
 *
 * @author Stefan Glaser
 */
public class Light extends Actuator implements ILight
{
	/** The brightness of the light. */
	protected double brightness;

	public Light(String name, String effectorName, IPose3D pose)
	{
		super(name, effectorName, pose);
	}

	public Light(IActuatorConfiguration config)
	{
		this(config.getName(), config.getEffectorName(), config.getPose());
	}

	public Light(Light other)
	{
		super(other);

		this.brightness = other.brightness;
	}

	@Override
	public void turnOn()
	{
		setBrightness(1);
	}

	@Override
	public void turnOff()
	{
		setBrightness(0);
	}

	@Override
	public double getBrightness()
	{
		return brightness;
	}

	@Override
	public void setBrightness(double brightness)
	{
		this.brightness = ValueUtil.limitValue(brightness, 0, 1);
	}

	@Override
	public boolean isOn()
	{
		return !isOff();
	}

	@Override
	public boolean isOff()
	{
		return brightness == 0;
	}

	@Override
	public IEffector generateAction()
	{
		return new LightEffector(effectorName, brightness);
	}

	@Override
	public IActuator copy()
	{
		return new Light(this);
	}
}
