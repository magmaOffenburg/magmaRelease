/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.sort;

/**
 * Implementation of natural 2 ways merge sort algorithm
 * @author Klaus Dorer
 */
public class MergeSort2Ways extends MergeSortBase
{
	/**
	 * Creates an instance for merging numbers
	 */
	public MergeSort2Ways()
	{
		super("MergeSort-natural2ways");
	}

	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		int k = 0;
		int n = a.length;
		// loop to repeat merge runs over all data
		do {
			int i = 0;
			k = 0;

			// loop to repeat until whole array has been merged once
			while (i < n) {
				k = i;
				T x;
				// loop to find natural order
				do
					x = a[i++];
				while (i < n && !isLessThan(a[i], x));
				int mid = i - 1;
				if (i < n) {
					// same to find a second natural order
					do
						x = a[i++];
					while (i < n && !isLessThan(a[i], x));
				}

				merge(a, k, mid, i - 1);
			}
		} while (k != 0);
	}
}
