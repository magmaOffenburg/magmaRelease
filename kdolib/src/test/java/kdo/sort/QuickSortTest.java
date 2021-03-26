/*
 * Copyright (c) 2007 Klaus Dorer Hochschule Offenburg
 */
package kdo.sort;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test for quick sort sorting
 *
 * @author Klaus Dorer
 */
public class QuickSortTest extends SortTestBase
{
	/**
	 * Called before each test
	 * @throws Exception
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		sorter = new QuickSort();
	}
}
