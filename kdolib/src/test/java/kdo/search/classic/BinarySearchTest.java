/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

/**
 * Tests the recursive binary search implementation
 *
 * @author Klaus Dorer
 */
public class BinarySearchTest extends SearchAlgorithmTest
{
	protected ISearchAlgorithm createSearchMethod()
	{
		return new BinarySearch();
	}
}
