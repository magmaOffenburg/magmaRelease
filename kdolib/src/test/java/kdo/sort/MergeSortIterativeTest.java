/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.sort;

import org.junit.jupiter.api.BeforeEach;

/**
 * Test for merge sort sorting
 *
 * @author Klaus Dorer
 */
public class MergeSortIterativeTest extends SortTestBase
{
	/**
	 * Called before each test
	 * @throws Exception
	 */
	@Override
	@BeforeEach
	public void setUp() throws Exception
	{
		sorter = new MergeSortIterative();
	}
}
