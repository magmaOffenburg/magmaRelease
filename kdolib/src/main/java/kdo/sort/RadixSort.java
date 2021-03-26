/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.sort;

/**
 * Implementation of a radix sort algorithm for int. This demonstration version
 * only works for positive values!
 *
 * @author Klaus Dorer
 */
public class RadixSort
{
	public static void sort(int[] a)
	{
		final int m = 2;
		int[][] bucket = new int[m][a.length];
		int[] bucketCount = new int[m];

		// loop to the maximal number of digits of an int
		for (int i = 0; i < 32; i++) {
			bucketCount[0] = 0;
			bucketCount[1] = 0;

			// partitioning
			for (int j = 0; j < a.length; j++) {
				// get bucket number
				int bucketID = (a[j] >> i) & 1;
				// put into bucket
				bucket[bucketID][bucketCount[bucketID]++] = a[j];
			}

			// collecting
			System.arraycopy(bucket[0], 0, a, 0, bucketCount[0]);
			System.arraycopy(bucket[1], 0, a, bucketCount[0], bucketCount[1]);
		}
	}
}
