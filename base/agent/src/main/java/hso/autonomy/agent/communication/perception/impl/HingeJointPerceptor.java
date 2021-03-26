/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;

/**
 * Hinge Joint Perceptor
 *
 * @author Simon Raffeiner
 */
public class HingeJointPerceptor extends Perceptor implements IHingeJointPerceptor
{
	private float axis;

	private float speed;

	private float load;

	private float voltage;

	private float temperature;

	private float calculatedTemperature;

	private float calculatedTemperatureCoil;

	private byte error;

	/**
	 * Default constructor, initializes the axis to (0.0)
	 *
	 * @param name Perceptor name
	 */
	public HingeJointPerceptor(String name)
	{
		this(name, 0.0f);
	}

	public HingeJointPerceptor(String name, float axis)
	{
		this(name, axis, 0, 0, 0, 0, 0, 0, (byte) 0);
	}

	/**
	 * @param name the name of this joint
	 * @param axis the angle of this joint (in degrees)
	 * @param speed the speed of this joint (in ???)
	 * @param load the torque???
	 * @param voltage the current voltage (in V???)
	 * @param temperature temperature (in degrees)
	 * @param calculatedTemperature ??? (in degrees)
	 * @param calculatedTemperatureCoil ??? (in degrees)
	 */
	public HingeJointPerceptor(String name, float axis, float speed, float load, float voltage, float temperature,
			float calculatedTemperature, float calculatedTemperatureCoil, byte error)
	{
		super(name);
		this.axis = axis;
		this.speed = speed;
		this.load = load;
		this.voltage = voltage;
		this.temperature = temperature;
		this.calculatedTemperature = calculatedTemperature;
		this.calculatedTemperatureCoil = calculatedTemperatureCoil;
		this.error = error;
	}

	@Override
	public float getAxis()
	{
		return this.axis;
	}

	public void setAxis(float axis)
	{
		this.axis = axis;
	}

	@Override
	public float getSpeed()
	{
		return speed;
	}

	@Override
	public float getLoad()
	{
		return load;
	}

	@Override
	public float getVoltage()
	{
		return voltage;
	}

	@Override
	public float getTemperature()
	{
		return temperature;
	}

	@Override
	public float getCalculatedTemperature()
	{
		return calculatedTemperature;
	}

	public void setCalculatedTemperature(float value)
	{
		calculatedTemperature = value;
	}

	@Override
	public float getCalculatedTemperatureCoil()
	{
		return calculatedTemperatureCoil;
	}

	public void setCalculatedTemperatureCoil(float value)
	{
		calculatedTemperatureCoil = value;
	}

	@Override
	public byte getError()
	{
		return error;
	}
}
