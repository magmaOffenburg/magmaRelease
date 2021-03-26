/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.sort;

/**
 * Implementation of the insertion sort algorithm
 *
 * @author Klaus Dorer
 */
public class InsertionSort extends SortAlgorithmBase
{
	/**
	 * Default constructor
	 */
	public InsertionSort()
	{
		super("InsertionSort");
	}

	/**
	 * Sorts the passed array using insertion sort. This version uses swap() to
	 * exchange elements in order to be able to calculate the number of swaps
	 * done. A faster version would store the currentSwapElement in the inner
	 * loop and just move elements right until proper place is reached to assign
	 * currentSwapElement to.
	 * @param a the array to sort
	 */
	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		// run through all unsorted elements
		for (int firstUnsortedIndex = 1; firstUnsortedIndex < a.length; firstUnsortedIndex++) {
			// swap the element to the proper place
			int currentSwapIndex = firstUnsortedIndex;
			while (currentSwapIndex > 0 && isLessThan(a[currentSwapIndex], a[currentSwapIndex - 1])) {
				// exchange with the current object
				swap(a, currentSwapIndex, currentSwapIndex - 1);
				currentSwapIndex--;
			}
		}
	}
}
