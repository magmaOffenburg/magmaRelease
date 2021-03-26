/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.decision.behavior;

import kdo.ebn.IEBNAction;
import kdo.ebnDevKit.staubs.model.IStaubs;

/**
 * Parent class for all behaviors
 *
 * @author Thomas Rinklin
 */
public abstract class Behavior implements IEBNAction
{
	/** the name of this behavior */
	private final String name;

	/** link to the world model */
	final IStaubs model;

	/**
	 * Default constructor for Behaviors
	 *
	 * @param name Behavior name
	 * @param model World model
	 */
	Behavior(String name, IStaubs model)
	{
		this.name = name;
		this.model = model;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	/**
	 * By default, behaviors are finished immediately
	 * @see kdo.ebn.IEBNAction#isFinished()
	 */
	@Override
	public boolean isFinished()
	{
		return true;
	}

	@Override
	public float getIntensity()
	{
		return 0;
	}

	@Override
	public void setIntensity(float intensity)
	{
	}
}
