/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.sort;

/**
 * Base implementation of a merge algorithm for all merge sorts.
 * @author Klaus Dorer
 */
public abstract class MergeSortBase extends SortAlgorithmBase
{
	/**
	 * Creates an instance for merging numbers
	 */
	public MergeSortBase(String name)
	{
		super(name);
	}

	/**
	 * Merges the left half of the specified array part with the right half to
	 * produce a sorted array part
	 * @param a the array containing the data
	 * @param left the leftmost index of the array part we are interested in
	 * @param middle split point of the data
	 * @param right the rightmost index of the array part we are interested in
	 */
	@SuppressWarnings("unchecked")
	protected <T extends Comparable<? super T>> void merge(T a[], int left, int middle, int right)
	{
		// extra space needed for merge sort
		int size = right - left + 1;
		T buffer[] = (T[]) new Comparable[size];
		int positionLeft = left;
		int positionRight = middle + 1;

		for (int i = 0; i < size; i++) {
			if (positionRight <= right && isLessThan(a[positionRight], a[positionLeft])) {
				buffer[i] = a[positionRight];
				positionRight++;
				if (positionRight > right) {
					// copy rest of left half
					System.arraycopy(a, positionLeft, buffer, i + 1, middle - positionLeft + 1);
					break;
				}
			} else {
				buffer[i] = a[positionLeft];
				positionLeft++;
				if (positionLeft > middle) {
					// copy rest of right half
					System.arraycopy(a, positionRight, buffer, i + 1, right - positionRight + 1);
					break;
				}
			}
		}

		// copy back to a
		System.arraycopy(buffer, 0, a, left, size);
	}
}
