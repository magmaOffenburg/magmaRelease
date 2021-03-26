/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;
import hso.autonomy.agent.model.agentmodel.IMotor;
import hso.autonomy.util.misc.ValueUtil;
import java.io.Serializable;
import java.util.Map;

/**
 * Motor attached to a joint implementation
 *
 * @author Stefan Glaser, Klaus Dorer
 *
 */
public class Motor implements Serializable, IMotor
{
	private String name;

	private float speed;

	private float load;

	private float voltage;

	private float temperature;

	private float calculatedTemperature;

	private float calculatedTemperatureCoil;

	// self calculated
	/** the sensor reading of the angle of the motor */
	private float perceivedAngle;

	/** maximum angular speed */
	private final float maxSpeed;

	/** the servo error code */
	private byte error;

	/** The stiffness of the motor */
	private float stiffness;

	/** the maximum gain of the motor when active */
	private float maxGain;

	/**
	 * Instantiates a new Motor
	 *
	 * @param name Effector name
	 */
	Motor(String name, float maxSpeed, float maxGain)
	{
		this.name = name;
		this.perceivedAngle = 0;
		this.maxSpeed = maxSpeed;
		this.maxGain = maxGain;
		this.stiffness = 1;
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	Motor(Motor source)
	{
		name = source.name;
		// TODO: check this ???
		// we do not copy the original value but use current as new original
		this.perceivedAngle = source.perceivedAngle;
		this.maxSpeed = source.maxSpeed;
		this.speed = source.speed;
		this.load = source.load;
		this.voltage = source.voltage;
		this.temperature = source.temperature;
		this.calculatedTemperature = source.calculatedTemperature;
		this.calculatedTemperatureCoil = source.calculatedTemperatureCoil;
		this.error = source.error;
		this.maxGain = source.maxGain;
		this.stiffness = source.stiffness;
	}

	public IMotor copy()
	{
		return new Motor(this);
	}

	@Override
	public float getPerceivedAngle()
	{
		return perceivedAngle;
	}

	@Override
	public void setPerceivedAngle(float angle)
	{
		perceivedAngle = angle;
	}

	public void generateMotorAction(Map<String, float[]> actions, float angle, float speedAtAngle, float accAtAngle)
	{
		float nextAxisSpeed = getNextSpeed(angle);
		float[] values = {nextAxisSpeed, angle, speedAtAngle, accAtAngle, stiffness * maxGain};
		actions.put(name, values);
	}

	/**
	 * @param angle the new desired angle
	 * @return the speed to get to the desired angle
	 */
	@Override
	public float getNextSpeed(float angle)
	{
		return angle - perceivedAngle;
	}

	void updateFromPerception(IHingeJointPerceptor hingeJointPerceptor)
	{
		perceivedAngle = hingeJointPerceptor.getAxis();
		speed = hingeJointPerceptor.getSpeed();
		load = hingeJointPerceptor.getLoad();
		voltage = hingeJointPerceptor.getVoltage();
		temperature = hingeJointPerceptor.getTemperature();
		calculatedTemperature = hingeJointPerceptor.getCalculatedTemperature();
		calculatedTemperatureCoil = hingeJointPerceptor.getCalculatedTemperatureCoil();
		error = hingeJointPerceptor.getError();
	}

	void updateNoPerception(float angle)
	{
		perceivedAngle = angle;
	}

	@Override
	public float getMaxSpeed()
	{
		return maxSpeed;
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

	@Override
	public float getCalculatedTemperatureCoil()
	{
		return calculatedTemperatureCoil;
	}

	@Override
	public byte getError()
	{
		return error;
	}

	@Override
	public void setStiffness(float stiffness)
	{
		this.stiffness = ValueUtil.limitValue(stiffness, 0, 1);
	}

	@Override
	public float getStiffness()
	{
		return stiffness;
	}

	@Override
	public float getGain()
	{
		return maxGain;
	}

	@Override
	public void setGain(float gain)
	{
		maxGain = gain;
	}
}
