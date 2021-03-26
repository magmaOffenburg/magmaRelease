/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.sort;

/**
 * Implementation of an iterative merge sort algorithm
 * @author Klaus Dorer
 */
public class MergeSortIterative extends MergeSortBase
{
	/**
	 * Creates an instance for merging numbers
	 */
	public MergeSortIterative()
	{
		super("MergeSort-iterative");
	}

	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		for (int width = 1; width <= a.length; width *= 2) {
			for (int i = 0; i < a.length; i += 2 * width) {
				int middle = i + width - 1;
				int right = i + 2 * width - 1;
				if (right > a.length - 1) {
					right = a.length - 1;
				}
				merge(a, i, middle, right);
			}
		}
	}
}
