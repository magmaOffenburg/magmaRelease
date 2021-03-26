/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IAccelerometerPerceptor;
import hso.autonomy.agent.communication.perception.ICompassPerceptor;
import hso.autonomy.agent.communication.perception.ICompositeJointPerceptor;
import hso.autonomy.agent.communication.perception.IForceResistancePerceptor;
import hso.autonomy.agent.communication.perception.IGlobalPosePerceptor;
import hso.autonomy.agent.communication.perception.IGyroPerceptor;
import hso.autonomy.agent.communication.perception.IHingeJointPerceptor;
import hso.autonomy.agent.communication.perception.IIMUPerceptor;
import hso.autonomy.agent.communication.perception.ILinePerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.communication.perception.IPerceptor;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import hso.autonomy.agent.communication.perception.IReferencePointPerceptor;
import hso.autonomy.agent.communication.perception.ITimerPerceptor;
import hso.autonomy.agent.communication.perception.ITransformPerceptor;
import hso.autonomy.agent.communication.perception.IVisibleObjectPerceptor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents all data the agent is able to perceive from its environment.
 * Should be updated in every simulation cycle.
 *
 * @author Klaus Dorer, Simon Raffeiner, Stefan Glaser
 */
public class Perception implements IPerception
{
	// Map for all named perceptors
	protected IPerceptorMap perceptors;

	// Flag - if the current perception contains vision information
	protected boolean containsVision;

	// Flag - if the current perception contains motor information
	protected boolean containsMotion;

	public Perception()
	{
		perceptors = new PerceptorMap();
	}

	@Override
	public ICompositeJointPerceptor getCompositeJointPerceptor(String name)
	{
		return perceptors.get(name, ICompositeJointPerceptor.class);
	}

	@Override
	public IHingeJointPerceptor getHingeJointPerceptor(String name)
	{
		return (IHingeJointPerceptor) perceptors.get(name);
	}

	@Override
	public IAccelerometerPerceptor getAccelerationPerceptor(String name)
	{
		return perceptors.get(name, IAccelerometerPerceptor.class);
	}

	@Override
	public ICompassPerceptor getCompassPerceptor(String name)
	{
		return perceptors.get(name, ICompassPerceptor.class);
	}

	@Override
	public IIMUPerceptor getIMUPerceptor(String name)
	{
		return perceptors.get(name, IIMUPerceptor.class);
	}

	@Override
	public IGyroPerceptor getGyroRatePerceptor(String name)
	{
		return perceptors.get(name, IGyroPerceptor.class);
	}

	@Override
	public IForceResistancePerceptor getForceResistancePerceptor(String name)
	{
		return perceptors.get(name, IForceResistancePerceptor.class);
	}

	@Override
	public IVisibleObjectPerceptor getVisibleObject(String name)
	{
		return perceptors.get(name, IVisibleObjectPerceptor.class);
	}

	@Override
	public IGlobalPosePerceptor getGlobalPose()
	{
		return perceptors.get("globalPose", IGlobalPosePerceptor.class);
	}

	@Override
	public ITimerPerceptor getTime()
	{
		return perceptors.get("time", ITimerPerceptor.class);
	}

	@Override
	public List<ILinePerceptor> getVisibleLines()
	{
		return perceptors.values()
				.stream()
				.filter(perceptor -> perceptor instanceof ILinePerceptor)
				.map(perceptor -> (ILinePerceptor) perceptor)
				.collect(Collectors.toList());
	}

	@Override
	public List<IReferencePointPerceptor> getReferencePointPerceptor()
	{
		return perceptors.values()
				.stream()
				.filter(perceptor -> perceptor instanceof IReferencePointPerceptor)
				.map(perceptor -> (IReferencePointPerceptor) perceptor)
				.collect(Collectors.toList());
	}

	@Override
	public ITransformPerceptor getTransformPerceptor(String name)
	{
		return (ITransformPerceptor) perceptors.get(name);
	}

	@Override
	public <T extends IPerceptor> T getPerceptor(String name, Class<T> clazz)
	{
		return perceptors.get(name, clazz);
	}

	@Override
	public boolean containsVision()
	{
		return containsVision;
	}

	@Override
	public boolean containsMotion()
	{
		return containsMotion;
	}

	@Override
	public void updatePerceptors(IPerceptorMap perceptors)
	{
		if (perceptors == null || perceptors.isEmpty()) {
			// nothing to do, might happen at disconnection
			return;
		}

		this.perceptors = perceptors;

		// Clear list of visible objects
		containsVision = false;
		containsMotion = false;

		// Process
		perceptors.values().forEach(this::processInputPerceptor);
	}

	protected void processInputPerceptor(IPerceptor perceptor)
	{
		// Handle sensor perceptors
		if (perceptor instanceof IHingeJointPerceptor || perceptor instanceof ICompositeJointPerceptor) {
			containsMotion = true;
		}

		if (perceptor instanceof IVisibleObjectPerceptor) {
			containsVision = true;
		}
	}

	@Override
	public String toString()
	{
		return perceptors.toString();
	}
}