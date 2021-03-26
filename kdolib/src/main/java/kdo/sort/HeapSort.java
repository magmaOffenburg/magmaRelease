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
public class HeapSort extends SortAlgorithmBase
{
	/**
	 * Default constructor
	 */
	public HeapSort()
	{
		super("HeapSort");
	}

	/**
	 * Sorts the passed array using heap sort.
	 * @param a the array to sort
	 */
	@Override
	public <T extends Comparable<? super T>> void sort(T[] a)
	{
		int n = a.length;

		// create the heap
		buildHeap(a);

		// now sort it
		while (n > 1) {
			n--;
			swap(a, 0, n);
			downHeap(a, 0, n);
		}
	}

	/**
	 * Transforms the passed array so that it satisfies the heap condition
	 * @param <T> the type of data to sort
	 * @param a the array to sort
	 */
	private <T extends Comparable<? super T>> void buildHeap(T[] a)
	{
		for (int i = a.length / 2 - 1; i >= 0; i--)
			downHeap(a, i, a.length);
	}

	/**
	 * Moves the specified element down the heap until it satisfies the heap
	 * condition
	 * @param <T> the type of data to sort
	 * @param a the array to sort
	 * @param current index of the element to be sunk down
	 * @param maxDown the index until which to sink down elements
	 */
	private <T extends Comparable<? super T>> void downHeap(T[] a, int current, int maxDown)
	{
		// first descendant of v
		int bestChild = 2 * current + 1;

		// keep percolating down
		while (bestChild < maxDown) {
			T childVal = a[bestChild];

			// is there a second descendant?
			if (bestChild + 1 < maxDown) {
				if (isLessThan(childVal, a[bestChild + 1])) {
					childVal = a[bestChild + 1];
					bestChild++;
				}
			}

			if (isLessThan(childVal, a[current])) {
				// element at proper place
				return;
			}
			swap(a, current, bestChild);
			current = bestChild;
			bestChild = 2 * current + 1;
		}
	}
}
