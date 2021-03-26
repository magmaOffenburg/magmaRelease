/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

/**
 * Tests the Exponential search implementation
 *
 * @author Klaus Dorer
 */
public class ExponentialSearchTest extends SearchAlgorithmTest
{
	protected ISearchAlgorithm createSearchMethod()
	{
		return new ExponentialSearch();
	}
}
