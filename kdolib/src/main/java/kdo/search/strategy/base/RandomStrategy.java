/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.search.strategy.base;

import kdo.util.IRandomSource;

/**
 * Base class for all kinds of strategies used
 * @author klaus
 */
public abstract class RandomStrategy extends StrategyBase
{
	/** source for pseudo random numbers */
	protected IRandomSource rand;

	/**
	 * @param name the name of this strategy
	 * @param rand source for pseudo random numbers, null if this is a
	 *        deterministic strategy and does not need random numbers
	 */
	public RandomStrategy(String name, IRandomSource rand)
	{
		super(name);
		this.rand = rand;
	}
}
