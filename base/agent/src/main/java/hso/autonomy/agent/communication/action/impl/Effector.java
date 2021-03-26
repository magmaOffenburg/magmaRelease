/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IEffector;

/**
 * Base class for all effectors.
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public abstract class Effector implements IEffector
{
	/** The name of the effector. */
	private final String name;

	public Effector(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
