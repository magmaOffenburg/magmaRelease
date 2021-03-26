/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.sort;

/**
 * Solution for exercise 2 sorting using selection sort
 *
 * @author Klaus Dorer
 */
public class SelectionSort extends SortAlgorithmBase
{
	/**
	 * Default constructor
	 */
	public SelectionSort()
	{
		super("SelectionSort");
	}

	/**
	 * Sorts the passed array using insertion sort
	 *
	 * @param a the array to sort
	 *
	 * @see exercise1.SortAlgorithmBase#sort(java.lang.Comparable[])
	 */
	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		// run through all elements
		for (int index = 0; index < a.length - 1; index++) {
			// search minimum of unsorted part of array
			int minIndex = minimum(a, index, a.length);
			// exchange with the current object
			swap(a, index, minIndex);
		}
	}

	/**
	 * Searches the position of the smallest element
	 *
	 * @param a the array to search
	 * @param start the index from where to start search (including)
	 * @param end the index where to end search (excluding)
	 * @return the index of the smallest element in the search range
	 */
	private <T extends Comparable<? super T>> int minimum(T[] a, int start, int end)
	{
		int minIndex = start;
		for (int index = start + 1; index < end; index++) {
			if (isLessThan(a[index], a[minIndex])) {
				minIndex = index;
			}
		}
		return minIndex;
	}
}
