/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import java.util.List;
import kdo.search.strategy.base.StrategyBase;

/**
 * Search method for searching in a sorted list of data using the Fibonacci
 * sequence
 *
 * @author Klaus Dorer
 */
public class FibonacciSearch extends StrategyBase implements ISearchAlgorithm
{
	public FibonacciSearch()
	{
		super("Fibonacci Search");
	}

	/**
	 * Implements the interface by searching in a sorted list of data using the
	 * Fibonacci sequence
	 *
	 * @see kdo.search.classic.ISearchAlgorithm#search(java.lang.Comparable,
	 *      java.util.List)
	 */
	@Override
	public <T> int search(T key, List<? extends Comparable<? super T>> data)
	{
		int size = data.size();
		if (size == 0) {
			return -1;
		}

		int[] fib = getFittingFibonacciNumbers(size);
		int f1 = fib[0];
		int f2 = fib[1];
		int f3 = fib[2];
		// int f1 = 514229;
		// int f2 = 832040;
		// int f3 = 1346269;
		int left = 0;
		while (f3 > 1) {
			int i = (left + f1 - 1 < size - 1) ? left + f1 - 1 : size - 1;
			Comparable<? super T> currentElement = data.get(i);
			int result = currentElement.compareTo(key);
			if (result > 0) {
				// search in left part
				f3 = f1;
				f2 = f2 - f1;
				f1 = f3 - f2;
			} else if (result < 0) {
				// search in right part
				left = i + 1;
				f3 = f2;
				f2 = f1;
				f1 = f3 - f2;
			} else {
				// found
				return i;
			}
		}
		return -1;
	}

	/**
	 * Searches for the 3 Fibonacci numbers of which only the highest is greater
	 * than size
	 *
	 * @param size the upper limit below which the returned Fibonacci number have
	 *        to be
	 * @return the 3 Fibonacci numbers of which only the highest is greater than
	 *         size
	 */
	private int[] getFittingFibonacciNumbers(int size)
	{
		int[] result = new int[3];
		result[0] = 1;
		result[1] = 1;
		result[2] = 2;

		while (result[2] - 1 < size) {
			result[0] = result[1];
			result[1] = result[2];
			result[2] = result[0] + result[1];
		}
		return result;
	}
}
