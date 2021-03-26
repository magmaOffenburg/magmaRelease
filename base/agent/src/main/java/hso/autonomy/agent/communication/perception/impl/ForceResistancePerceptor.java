/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IForceResistancePerceptor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Force resistance perceptor
 *
 * @author Simon Raffeiner
 */
public class ForceResistancePerceptor extends Perceptor implements IForceResistancePerceptor
{
	// point where the force acts
	private Vector3D forceOrigin;

	// the force itself
	private Vector3D force;

	/**
	 * Default constructor, initializes origin and force to (0.0, 0.0, 0.0)
	 * @param name the name of the sensor
	 */
	public ForceResistancePerceptor(String name)
	{
		this(name, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}

	/**
	 * Assignment constructor
	 *
	 * @param name Perceptor name
	 * @param fox Force Origin - X
	 * @param foy Force Origin - Y
	 * @param foz Force Origin - Z
	 * @param fx Force - X
	 * @param fy Force - Y
	 * @param fz Force - Z
	 */
	public ForceResistancePerceptor(String name, float fox, float foy, float foz, float fx, float fy, float fz)
	{
		super(name);

		forceOrigin = new Vector3D(fox, foy, foz);
		force = new Vector3D(fx, fy, fz);
	}

	@Override
	public Vector3D getForceOrigin()
	{
		return forceOrigin;
	}

	@Override
	public Vector3D getForce()
	{
		return force;
	}
}
