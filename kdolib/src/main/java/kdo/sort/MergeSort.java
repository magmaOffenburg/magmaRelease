/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.sort;

/**
 * Implementation of a recursive merge sort algorithm
 * @author Klaus Dorer
 */
public class MergeSort extends MergeSortBase
{
	/**
	 * Creates an instance for merging numbers
	 */
	public MergeSort()
	{
		super("MergeSort-recursive");
	}

	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		sort(a, 0, a.length - 1);
	}

	/**
	 * Sorts that part of the passed array defined by left and right index
	 * @param a the array containing the data
	 * @param left the leftmost index of the array we are interested in
	 *        (including)
	 * @param right the rightmost index of the array we are interested in
	 *        (including)
	 */
	private <T extends Comparable<? super T>> void sort(T a[], int left, int right)
	{
		if (right > left) {
			int middle = (right + left) / 2;
			sort(a, left, middle);
			sort(a, middle + 1, right);
			merge(a, left, middle, right);
		}
	}
}
