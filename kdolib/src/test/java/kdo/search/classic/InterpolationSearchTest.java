/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import kdo.search.representation.IntegerAC;

/**
 * Tests the interpolation search implementation
 *
 * @author Klaus Dorer
 */
public class InterpolationSearchTest extends SearchAlgorithmTestBase<IntegerAC>
{
	protected ISearchAlgorithm createSearchMethod()
	{
		return new InterpolationSearch();
	}

	/**
	 * Creates IntegerAC
	 *
	 * @param value the value to represent
	 * @return a new IntegerAC
	 */
	@Override
	protected IntegerAC createItem(int value)
	{
		return new IntegerAC(value);
	}
}
