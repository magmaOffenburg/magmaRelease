/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.impl.Perceptor;
import magma.agent.communication.perception.IAgentStatePerceptor;

/**
 * Agent state perceptor, currently features temperature and battery level
 *
 * @author Simon Raffeiner
 */
class AgentStatePerceptor extends Perceptor implements IAgentStatePerceptor
{
	private int temperature;

	private int battery;

	/**
	 * Default constructor, initializes temperature and battery level to 0.00
	 */
	public AgentStatePerceptor()
	{
		this(0, 0);
	}

	/**
	 * Assignment constructor
	 *
	 * @param temperature Temperature (in degrees)
	 * @param battery Battery level (in percent)
	 */
	public AgentStatePerceptor(int temperature, int battery)
	{
		super("AgentState");

		this.temperature = temperature;
		this.battery = battery;
	}

	@Override
	public int getTemperature()
	{
		return this.temperature;
	}

	@Override
	public int getBattery()
	{
		return battery;
	}
}
