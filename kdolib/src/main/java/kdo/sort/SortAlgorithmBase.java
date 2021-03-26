/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.sort;

import kdo.search.strategy.base.StrategyBase;

/**
 * Base class for all sorting algorithms
 *
 * @author Klaus Dorer
 */
abstract public class SortAlgorithmBase extends StrategyBase implements ISortAlgorithm
{
	/** the number of swaps conducted */
	private long swaps;

	/** the number of key comparisons conducted */
	private long comparisons;

	/**
	 * @param name the name of this strategy
	 */
	public SortAlgorithmBase(String name)
	{
		super(name);
		swaps = 0;
		comparisons = 0;
	}

	/**
	 * Sorts the passed array ascending using the objects' compareTo method
	 *
	 * @param a the array to be sorted
	 */
	@Override
	abstract public <T extends Comparable<? super T>> void sort(T a[]);

	/**
	 * Exchanges the position of objects i and j in the passed array
	 *
	 * @param a the array containing the objects
	 * @param i index of the first object to exchange
	 * @param j index of the second object to exchange
	 */
	protected void swap(Object a[], int i, int j)
	{
		// System.out.println("Swap: " + a[i] + " and " + a[j]);
		Object o = a[i];
		a[i] = a[j];
		a[j] = o;
		swaps++;
	}

	/**
	 * Compares the two passed Comparables.
	 * @param a the first comparable
	 * @param b the second comparable
	 * @return true if the first comparable is less than (and not equal) the
	 *         second
	 */
	protected <T extends Comparable<? super T>> boolean isLessThan(T a, T b)
	{
		comparisons++;
		return a.compareTo(b) < 0;
	}

	/**
	 * Prints the contents of the array to console
	 *
	 * @param a the array to print
	 */
	public void printArray(Object a[])
	{
		System.out.print("Swaps: " + swaps + " comparisons: " + comparisons + " Data: ");
		if (a.length > 50) {
			// prevent printing too large arrays
			System.out.println();
			return;
		}
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		System.out.println();
	}

	/**
	 * @return the number of key comparisons made
	 */
	public long getComparisons()
	{
		return comparisons;
	}

	/**
	 * @return the number of swaps made
	 */
	public long getSwaps()
	{
		return swaps;
	}
}
