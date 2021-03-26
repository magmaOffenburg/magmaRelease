/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.staubs.decision.behavior;

import kdo.ebnDevKit.staubs.model.IStaubs;

/**
 * Chill behavior
 *
 * @author Thomas Rinklin
 */
public class Chill extends Behavior
{
	public Chill(String name, IStaubs model)
	{
		super(name, model);
	}

	@Override
	public void perform()
	{
		model.chill(true);
	}
}
