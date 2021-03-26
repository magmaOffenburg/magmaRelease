/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 * File: ShellSort.java
 *
 * @author S. Schuierer
 */

package kdo.sort;

public class ShellSort extends SortAlgorithmBase
{
	/**
	 * Default constructor
	 */
	public ShellSort()
	{
		super("ShellSort");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see sort.SortAlgorithmBase#sort(java.lang.Comparable[])
	 */
	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		int i, j = 0, k, h;
		T temp;
		int n = a.length;
		int[] spalten = {2147483647, 1131376761, 410151271, 157840433, 58548857, 21521774, 8810089, 3501671, 1355339,
				543749, 213331, 84801, 27901, 11969, 4711, 1968, 815, 271, 111, 41, 13, 4, 1};

		for (k = 0; k < 23; k++) {
			h = spalten[k];
			// Sort with insertion sort
			for (i = h; i < n; i++) {
				temp = a[i];
				j = i;
				while (j >= h && isLessThan(temp, a[j - h])) {
					a[j] = a[j - h];
					j = j - h;
				}
				a[j] = temp;
			}
			// System.out.print("h=" + h + " j=" + j + " :");
			// printArray(a);
		}
	}
}
