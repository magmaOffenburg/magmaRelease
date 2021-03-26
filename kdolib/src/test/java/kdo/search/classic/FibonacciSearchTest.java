/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

/**
 * Tests the Fibonacci search implementation
 *
 * @author Klaus Dorer
 */
public class FibonacciSearchTest extends SearchAlgorithmTest
{
	protected ISearchAlgorithm createSearchMethod()
	{
		return new FibonacciSearch();
	}
}
