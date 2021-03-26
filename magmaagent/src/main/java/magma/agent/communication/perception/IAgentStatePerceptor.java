/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception;

import hso.autonomy.agent.communication.perception.IPerceptor;

/**
 * The Agent State Perceptor represents the internal state of the robot agent.
 * At the moment the only information is environment temperature and remaining
 * battery level.
 *
 * @author Simon Raffeiner
 */
public interface IAgentStatePerceptor extends IPerceptor {
	/**
	 * Get Temperature (in degrees)
	 */
	int getTemperature();

	/**
	 * Get Battery level (in percent)
	 */
	int getBattery();
}