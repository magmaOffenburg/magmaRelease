/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IForceResistancePerceptor;
import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmeta.ISensorConfiguration;
import hso.autonomy.agent.model.agentmodel.IForceResistance;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.misc.FuzzyCompare;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Implementation of a "ForceResistance" sensor
 *
 * @author Klaus Dorer
 */
public class ForceResistance extends Sensor implements IForceResistance
{
	/** point where the force acts */
	private Vector3D forceOrigin;

	/** the force itself */
	private Vector3D force;

	private Vector3D previousForceOrigin;

	private Vector3D previousForce;

	/** counts the number of consecutive update cycles we had force on this sensor */
	private int cyclesWithForce;

	/**
	 * counts the number of consecutive update cycles we had no force on this
	 * sensor (not including the current cycle)
	 */
	private int previousCyclesWithoutForce;

	/**
	 * Instantiates a new ForceResistance sensor
	 *
	 * @param name sensor name
	 * @param perceptorName unique name of the perceptor
	 * @param pose the sensor pose relative to the body part it is mounted to
	 */
	public ForceResistance(String name, String perceptorName, IPose3D pose)
	{
		super(name, perceptorName, pose);
		forceOrigin = Vector3D.ZERO;
		force = Vector3D.ZERO;
		previousForceOrigin = Vector3D.ZERO;
		previousForce = Vector3D.ZERO;
	}

	public ForceResistance(ISensorConfiguration config)
	{
		this(config.getName(), config.getPerceptorName(), config.getPose());
	}

	/**
	 * Copy constructor
	 * @param source the object to copy from
	 */
	private ForceResistance(ForceResistance source)
	{
		super(source);
		forceOrigin = source.forceOrigin;
		force = source.force;
		previousForceOrigin = source.previousForceOrigin;
		previousForce = source.previousForce;
	}

	@Override
	public Vector3D getForceOrigin()
	{
		return forceOrigin;
	}

	/**
	 * Set the force origin
	 *
	 * @param forceOrigin Force origin point
	 */
	void setForceOrigin(Vector3D forceOrigin)
	{
		previousForceOrigin = this.forceOrigin;
		this.forceOrigin = forceOrigin;
	}

	@Override
	public Vector3D getForce()
	{
		return force;
	}

	@Override
	public Vector3D getPreviousForceOrigin()
	{
		return previousForceOrigin;
	}

	@Override
	public Vector3D getPreviousForce()
	{
		return previousForce;
	}

	/**
	 * Set the force vector
	 *
	 * @param force Force vector
	 */
	void setForce(Vector3D force)
	{
		previousForce = this.force;

		if (force.getNorm() >= 0.01) {
			cyclesWithForce++;
		} else {
			if (cyclesWithForce > 0) {
				previousCyclesWithoutForce = 0;
			}
			cyclesWithForce = 0;
			previousCyclesWithoutForce++;
		}
		this.force = force;
	}

	/**
	 * Updates this ForceResistances from perception
	 * @param perception the result from server message parsing
	 */
	@Override
	public void updateFromPerception(IPerception perception)
	{
		IForceResistancePerceptor frPerceptor = perception.getForceResistancePerceptor(getPerceptorName());

		if (frPerceptor == null) {
			setForce(Vector3D.ZERO);
			return;
		}

		// Fetch new Force Values
		setForce(frPerceptor.getForce());
		setForceOrigin(frPerceptor.getForceOrigin());
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof ForceResistance)) {
			return false;
		}
		ForceResistance other = (ForceResistance) o;
		if (!super.equals(other)) {
			return false;
		}
		if (!FuzzyCompare.eq(force, other.force, 0.00001)) {
			return false;
		}
		return FuzzyCompare.eq(forceOrigin, other.forceOrigin, 0.00001f);
	}

	@Override
	public ISensor copy()
	{
		return new ForceResistance(this);
	}

	/**
	 * @return the number of consecutive update cycles we had force on this
	 *         sensor
	 */
	public int getCyclesWithForce()
	{
		return cyclesWithForce;
	}

	/**
	 * @return the number of consecutive update cycles we had no force on this
	 *         sensor (not including the current cycle)
	 */
	public int getPreviousCyclesWithoutForce()
	{
		return previousCyclesWithoutForce;
	}
}
