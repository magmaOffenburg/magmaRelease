/*
 * Copyright (c) 2008 Klaus Dorer Hochschule Offenburg
 */
package kdo.sort;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test for bubble sort sorting
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.1 $
 */
public class BubbleSortTest extends SortTestBase
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
		sorter = new BubbleSort();
	}
}
