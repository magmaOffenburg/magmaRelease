/*
 * Copyright (c) 2008 Klaus Dorer Hochschule Offenburg
 */
package kdo.sort;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test for Insertion sort sorting
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.1 $
 */
public class HeapSortTest extends SortTestBase
{
	/**
	 * Called before each test
	 *
	 * @throws java.lang.Exception
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		sorter = new HeapSort();
	}
}
