/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.impl.RGBLightEffector;
import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.agent.model.agentmodel.IActuator;
import hso.autonomy.agent.model.agentmodel.IRGBLight;
import hso.autonomy.util.geometry.IPose3D;
import java.awt.Color;

/**
 * Base implementation of an {@link IRGBLight} actuator.
 *
 * @author Stefan Glaser
 */
public class RGBLight extends Light implements IRGBLight
{
	/** The intended color of the light. */
	private Color color;

	public RGBLight(String name, String effectorName, IPose3D pose)
	{
		super(name, effectorName, pose);

		color = Color.BLUE;
	}

	public RGBLight(IActuatorConfiguration config)
	{
		this(config.getName(), config.getEffectorName(), config.getPose());
	}

	public RGBLight(RGBLight other)
	{
		super(other);

		this.color = other.color;
	}

	@Override
	public Color getColor()
	{
		return color;
	}

	@Override
	public void setColor(Color color)
	{
		this.color = color;
		setBrightnessFromColor();
	}

	@Override
	public void setBrightness(double brightness)
	{
		super.setBrightness(brightness);
		setColorFromBrightness();
	}

	private void setBrightnessFromColor()
	{
		brightness = (color.getRed() + color.getGreen() + color.getBlue()) / (3 * 255);
	}

	private void setColorFromBrightness()
	{
		float rgbValue = (float) brightness;
		color = new Color(rgbValue, rgbValue, rgbValue);
	}

	@Override
	public IEffector generateAction()
	{
		return new RGBLightEffector(effectorName, brightness, color);
	}

	@Override
	public IActuator copy()
	{
		return new RGBLight(this);
	}
}
