/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.base;

import kdo.search.strategy.IStrategy;

/**
 * Base class for all kinds of strategies used
 * @author klaus
 */
public abstract class StrategyBase implements IStrategy
{
	/** the name of this strategy */
	private final String name;

	/**
	 * @param name the name of this strategy
	 */
	public StrategyBase(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}
}
