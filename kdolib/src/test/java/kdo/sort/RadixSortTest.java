/*
 * Copyright (c) 2008 Klaus Dorer Hochschule Offenburg
 */
package kdo.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Radix Sorter tests
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.1 $
 */
public class RadixSortTest
{
	/**
	 * Test the method sort() to work with empty argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testSortEmptyArray() throws Exception
	{
		int[] a = new int[0];
		RadixSort.sort(a);
		// should not get exceptions
	}

	/**
	 * Test the method sort() to work with one argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testSortOneElement() throws Exception
	{
		int a[] = {15};
		int expected[] = {15};
		RadixSort.sort(a);
		checkArray(expected, a);
	}

	/**
	 * Test the method sort() to work with one argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testSortTwoElements() throws Exception
	{
		int a[] = {15, 2};
		int expected[] = {2, 15};
		RadixSort.sort(a);
		checkArray(expected, a);
	}

	/**
	 * Test the method sort() to work with more arguments.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testSortAllElements() throws Exception
	{
		int a[] = {15, 2, 43, 17, 4, 8, 47};
		int expected[] = {2, 4, 8, 15, 17, 43, 47};
		RadixSort.sort(a);
		checkArray(expected, a);
	}

	private void checkArray(int[] expected, int[] was)
	{
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], was[i], "Elements not sorted properly");
		}
	}
}
