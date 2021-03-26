/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.sort;

/**
 * @author klaus
 *
 */
public class BubbleSort extends SortAlgorithmBase
{
	/**
	 * Default constructor
	 */
	public BubbleSort()
	{
		super("BubbleSort");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see sort.SortAlgorithmBase#sort(java.lang.Comparable[])
	 */
	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		for (int i = 0; i < a.length - 1; i++) {
			boolean swapped = false;
			for (int j = 1; j < a.length - i; j++) {
				if (isLessThan(a[j], a[j - 1])) {
					swap(a, j - 1, j);
					swapped = true;
				}
			}
			if (!swapped) {
				return;
			}
		}
	}
}
