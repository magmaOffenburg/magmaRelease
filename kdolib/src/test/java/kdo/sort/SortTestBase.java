/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Base class for sorting tests
 *
 * @author Last modified by $Author: KDorer $
 * @version $Revision: 1.1 $
 */
public class SortTestBase
{
	protected ISortAlgorithm sorter;

	/**
	 * Called before each test
	 *
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception
	{
		sorter = new InsertionSort();
	}

	/**
	 * Test the method sort() to work with empty argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testSortEmptyArray() throws Exception
	{
		Integer[] a = new Integer[0];
		sorter.sort(a);
		// should not get exceptions
	}

	/**
	 * Test the method sort() to work with one argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test //(timeout = 2000)
	public void testSortOneElement() throws Exception
	{
		final int NUMBERS[] = {15};
		Integer[] a = createArray(NUMBERS);
		sorter.sort(a);
		checkArray(NUMBERS, a);
	}

	/**
	 * Test the method sort() to work with one argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test //(timeout = 2000)
	public void testSortTwoElements() throws Exception
	{
		Integer[] a = new Integer[2];
		a[0] = Integer.valueOf(15);
		a[1] = Integer.valueOf(5);
		sorter.sort(a);
		assertEquals(5, (a[0]).intValue(), "Elements not sorted properly");
		assertEquals(15, (a[1]).intValue(), "Elements not sorted properly");
	}

	/**
	 * Test the method sort() to work with more arguments.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	@Timeout(2)
	public void testSortAllElements() throws Exception
	{
		final int NUMBERS[] = {15, 2, 43, 17, 4, 8, 47};
		final int NUMBERS_SORTED[] = {2, 4, 8, 15, 17, 43, 47};
		Integer[] a = createArray(NUMBERS);
		sorter.sort(a);
		checkArray(NUMBERS_SORTED, a);
	}

	/**
	 * Test the method sort() to work with more arguments.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	@Timeout(2)
	public void testSortAllElementsAlsoLast() throws Exception
	{
		final int NUMBERS[] = {15, 2, 43, 17, 4, 8, 5};
		final int NUMBERS_SORTED[] = {2, 4, 5, 8, 15, 17, 43};
		Integer[] a = createArray(NUMBERS);
		sorter.sort(a);
		checkArray(NUMBERS_SORTED, a);
	}

	/**
	 * Test the method sort() to work with more arguments.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	@Timeout(2)
	public void testSortWith3EqualPivotElements() throws Exception
	{
		final int NUMBERS[] = {15, 2, 43, 15, 17, 8, 15};
		final int NUMBERS_SORTED[] = {2, 8, 15, 15, 15, 17, 43};
		Integer[] a = createArray(NUMBERS);
		sorter.sort(a);
		checkArray(NUMBERS_SORTED, a);
	}

	/**
	 * Test the method sort() to work with more arguments.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	@Timeout(2)
	public void testSortMovingPivot() throws Exception
	{
		final int NUMBERS[] = {3, 2, 10, 17, 9, 15, 8, 47};
		final int NUMBERS_SORTED[] = {2, 3, 8, 9, 10, 15, 17, 47};
		Integer[] a = createArray(NUMBERS);
		sorter.sort(a);
		checkArray(NUMBERS_SORTED, a);
	}

	private void checkArray(int[] expected, Integer[] was)
	{
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], (was[i]).intValue(), "Elements not sorted properly");
		}
	}

	private Integer[] createArray(int[] numbers)
	{
		int size = numbers.length;
		Integer[] result = new Integer[size];
		for (int i = 0; i < size; i++) {
			result[i] = Integer.valueOf(numbers[i]);
		}
		return result;
	}
}
