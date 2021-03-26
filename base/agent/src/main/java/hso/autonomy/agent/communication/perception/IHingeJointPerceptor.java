/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

/**
 * The hinge joint perceptor measures the angle of a single axis joint.
 *
 * @author Simon Raffeiner
 */
public interface IHingeJointPerceptor extends IPerceptor {
	float getAxis();

	void setAxis(float angle);

	float getSpeed();

	float getLoad();

	float getVoltage();

	float getTemperature();

	float getCalculatedTemperature();

	float getCalculatedTemperatureCoil();

	byte getError();
}