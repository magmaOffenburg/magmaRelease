/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

/**
 * Base test class for all classic search algorithms
 *
 * @author Klaus Dorer
 */
public abstract class SearchAlgorithmTest extends SearchAlgorithmTestBase<Integer>
{
	/**
	 * Default implementation creates T Overwrite to create test case specific
	 * entries
	 *
	 * @param value the value to represent
	 * @return a new T
	 */
	@Override
	protected Integer createItem(int value)
	{
		return Integer.valueOf(value);
	}
}
