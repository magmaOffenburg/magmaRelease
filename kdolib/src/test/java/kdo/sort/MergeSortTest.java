/*
 * Copyright (c) 2007 Klaus Dorer Hochschule Offenburg
 */
package kdo.sort;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test for merge sort sorting
 *
 * @author Klaus Dorer
 */
public class MergeSortTest extends SortTestBase
{
	/**
	 * Called before each test
	 * @throws Exception
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		sorter = new MergeSort();
	}
}
