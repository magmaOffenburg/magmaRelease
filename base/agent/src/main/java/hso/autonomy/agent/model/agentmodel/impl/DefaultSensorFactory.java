/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.model.agentmeta.IActuatorConfiguration;
import hso.autonomy.agent.model.agentmeta.ICameraConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.IAccelerometer;
import hso.autonomy.agent.model.agentmodel.ICamera;
import hso.autonomy.agent.model.agentmodel.ICompass;
import hso.autonomy.agent.model.agentmodel.IForceResistance;
import hso.autonomy.agent.model.agentmodel.IGyroRate;
import hso.autonomy.agent.model.agentmodel.IHingeJoint;
import hso.autonomy.agent.model.agentmodel.IIMU;
import hso.autonomy.agent.model.agentmodel.ILight;
import hso.autonomy.agent.model.agentmodel.ISensorFactory;

/**
 * Default implementation of a {@link ISensorFactory}.
 *
 * @author Stefan Glaser
 */
public class DefaultSensorFactory implements ISensorFactory
{
	@Override
	public IHingeJoint createHingeJoint(IHingeJointConfiguration config)
	{
		return new HingeJoint(config);
	}

	@Override
	public IGyroRate createGyroRate(ISensorConfiguration config)
	{
		return new GyroRate(config);
	}
	@Override
	public IAccelerometer createAccelerometer(ISensorConfiguration config)
	{
		return new Accelerometer(config);
	}

	@Override
	public IForceResistance createForceResistance(ISensorConfiguration config)
	{
		return new ForceResistance(config);
	}

	@Override
	public IIMU createIMU(ISensorConfiguration config)
	{
		return new IMU(config);
	}

	@Override
	public ICompass createCompass(ISensorConfiguration config)
	{
		return new Compass(config);
	}

	@Override
	public ICamera createCamera(ICameraConfiguration config)
	{
		return new Camera(config);
	}

	@Override
	public ILight createLight(IActuatorConfiguration config)
	{
		return new Light(config);
	}
}
