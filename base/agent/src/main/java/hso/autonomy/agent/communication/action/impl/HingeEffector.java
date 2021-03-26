/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IHingeEffector;

/**
 * Implementation of a "HingeJoint" effector, used to move robot joints
 *
 * @author Klaus Dorer
 */
public class HingeEffector extends Effector implements IHingeEffector
{
	/**
	 * the speed of the effector in degrees per cycle - zero means the angle is
	 * not changed
	 */
	private float speed;

	private float lastSpeed;

	/** the desired angle of the effector in degrees */
	private float desiredAngle;

	/**
	 * the speed we want to have when reaching desired angle (in degrees per
	 * cycle)
	 */
	private float speedAtDesiredAngle;

	/**
	 * the acceleration we want to have when reaching desired angle (in degrees
	 * per cycle per cycle)
	 */
	private float accelerationAtDesiredAngle;

	/**
	 * Instantiates a new HingeJoint effector and initializes all fields to zero
	 *
	 * @param name Hinge Joint name
	 */
	public HingeEffector(String name)
	{
		super(name);

		speed = 0.0f;
		desiredAngle = 0.0f;
		lastSpeed = 0.0f;
	}

	public void setEffectorValues(float... values)
	{
		this.speed = values[0];
		this.desiredAngle = values[1];
		this.speedAtDesiredAngle = values[2];
		this.accelerationAtDesiredAngle = values[3];
	}

	public void set(float speed, float desiredAngle, float speedAtDesiredAngle, float accAtDesiredAngle)
	{
		this.speed = speed;
		this.desiredAngle = desiredAngle;
		this.speedAtDesiredAngle = speedAtDesiredAngle;
		this.accelerationAtDesiredAngle = accAtDesiredAngle;
	}

	public boolean hasChanged()
	{
		return speed != lastSpeed;
	}

	public void resetAfterAction()
	{
		lastSpeed = speed;
		speed = 0.0f;
	}

	@Override
	public float getSpeed()
	{
		return speed;
	}

	@Override
	public float getDesiredAngle()
	{
		return desiredAngle;
	}

	@Override
	public float getSpeedAtDesiredAngle()
	{
		return speedAtDesiredAngle;
	}

	@Override
	public float getAccelerationAtDesiredAngle()
	{
		return accelerationAtDesiredAngle;
	}
}
