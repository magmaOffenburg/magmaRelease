/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Base test class for all classic search algorithms
 *
 * @author Klaus Dorer
 */
public abstract class SearchAlgorithmTestBase<T>
{
	protected ISearchAlgorithm searcher;

	@BeforeEach
	public void setup()
	{
		searcher = createSearchMethod();
	}

	protected abstract ISearchAlgorithm createSearchMethod();

	/**
	 * Test the method search() to work with empty argument.
	 *
	 * @throws Exception if an exception occurs
	 */
	@Test
	public void testSearchEmptyList() throws Exception
	{
		List<Comparable<T>> data = new ArrayList<Comparable<T>>();
		T key = createItem(5);
		int result = searcher.search(key, data);
		assertEquals(-1, result);
	}

	@Test
	public void testSearchOneElementListSuccess() throws Exception
	{
		List<Comparable<T>> data = createData(new int[] {5});
		T key = createItem(5);
		int result = searcher.search(key, data);
		assertEquals(0, result);
	}

	@Test
	public void testSearchOneElementListFailure() throws Exception
	{
		List<Comparable<T>> data = createData(new int[] {5});
		T key = createItem(4);
		int result = searcher.search(key, data);
		assertEquals(-1, result);
	}

	@Test
	public void testSearchTwoElementListSuccess() throws Exception
	{
		List<Comparable<T>> data = createData(new int[] {5, 7});
		T key = createItem(5);
		int result = searcher.search(key, data);
		assertEquals(0, result);

		key = createItem(7);
		result = searcher.search(key, data);
		assertEquals(1, result);
	}

	@Test
	public void testSearchTwoElementListFailure() throws Exception
	{
		List<Comparable<T>> data = createData(new int[] {5, 7});
		T key = createItem(4);
		int result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(6);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(8);
		result = searcher.search(key, data);
		assertEquals(-1, result);
	}

	@Test
	public void testSearchManyElementList() throws Exception
	{
		List<Comparable<T>> data = createData(new int[] {-5, 7, 9, 11, 13});
		T key = createItem(4);
		int result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(-10);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(1);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(8);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(12);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(14);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(14000);
		result = searcher.search(key, data);
		assertEquals(-1, result);

		key = createItem(-5);
		result = searcher.search(key, data);
		assertEquals(0, result);

		key = createItem(7);
		result = searcher.search(key, data);
		assertEquals(1, result);

		key = createItem(9);
		result = searcher.search(key, data);
		assertEquals(2, result);

		key = createItem(11);
		result = searcher.search(key, data);
		assertEquals(3, result);

		key = createItem(13);
		result = searcher.search(key, data);
		assertEquals(4, result);
	}

	@Test
	public void testSearchManyElementNotFound() throws Exception
	{
		List<Comparable<T>> data = createData(new int[] {1, 2, 3, 6, 7, 9, 10, 10, 11, 15, 16, 19, 21, 22, 24, 25});
		T key = createItem(12);
		int result = searcher.search(key, data);
		assertEquals(-1, result);
	}

	/**
	 * Creates a single element to test
	 *
	 * @param value the value to represent
	 * @return a new T from the value passed
	 */
	protected abstract T createItem(int value);

	@SuppressWarnings("unchecked")
	private List<Comparable<T>> createData(int[] numbers)
	{
		List<Comparable<T>> result = new ArrayList<Comparable<T>>(numbers.length);
		for (int i : numbers) {
			result.add((Comparable<T>) createItem(i));
		}
		return result;
	}
}
