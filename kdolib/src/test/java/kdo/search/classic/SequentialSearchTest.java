/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

/**
 * Tests the Sequential search implementation
 *
 * @author Klaus Dorer
 */
public class SequentialSearchTest extends SearchAlgorithmTest
{
	protected ISearchAlgorithm createSearchMethod()
	{
		return new SequentialSearch();
	}
}
