/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.decision.beliefs;

import kdo.ebn.IEBNPerception;
import kdo.ebnDevKit.staubs.model.IStaubs;

/**
 * Parent class for all beliefs
 * @author Thomas Rinklin
 *
 */
public abstract class Belief implements IEBNPerception
{
	final IStaubs model;

	private final String name;

	/**
	 * Constructor
	 *
	 * @param name Belief name
	 * @param model Model
	 */
	Belief(String name, IStaubs model)
	{
		this.model = model;
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
