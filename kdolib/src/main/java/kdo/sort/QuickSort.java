/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.sort;

/**
 * @author Klaus Dorer
 *
 */
public class QuickSort extends SortAlgorithmBase
{
	// the number of recursions done
	private long recursions = 0;

	public QuickSort()
	{
		super("QuickSort");
	}

	/*
	 * (non-Javadoc)
	 * @see exercise1.SortAlgorithmBase#sort()
	 */
	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		recursions = -1;
		sort(a, 0, a.length - 1);
	}

	/**
	 * Sorts that part of the passed array defined by left and right indexex
	 * @param a the array containing the data
	 * @param left the leftmost index of the array we are interested in
	 *        (including)
	 * @param right the rightmost index of the array we are interested in
	 *        (including)
	 */
	public <T extends Comparable<? super T>> void sort(T a[], int left, int right)
	{
		recursions++;
		if (right > left) {
			int pivotIndex = divide(a, left, right);
			sort(a, left, pivotIndex - 1);
			sort(a, pivotIndex + 1, right);
		}
	}

	/**
	 * Divides the passed array between left and right using a pivot element
	 * @param a the array containing the data
	 * @param left the leftmost index of the array we are interested in
	 * @param right the rightmost index of the array we are interested in
	 * @return the index of the pivot element
	 */
	private <T extends Comparable<? super T>> int divide(T a[], int left, int right)
	{
		int i = left - 1;
		int j = right;
		T pivot = a[right];
		while (true) {
			do
				i++;
			while (i < j && isLessThan(a[i], pivot));

			do
				j--;
			while (i < j && isLessThan(pivot, a[j]));

			if (i >= j) {
				// finished to move elements
				if (i != right) {
					// move pivot element to proper position
					swap(a, i, right);
				}
				return i;
			}
			// found two candidates to move in other half
			swap(a, i, j);
		}
	}

	/**
	 * @return the recursions
	 */
	public long getRecursions()
	{
		return recursions;
	}
}
