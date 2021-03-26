/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.sort;

import java.util.Arrays;
import java.util.Random;

public class SortMain
{
	static int NUMBERS[] = {1, 3, 2, 5, 7, 6, 4}; // "Standard"-Array

	public static void main(String args[])
	{
		// use this call to measure performance
		if (true) {
			sortOnce(args);
			testSort(args);
		}

		// int i;
		// for (i = 0; i < 1000; i++) {
		// testSort(args);
		// }
		// System.out.println(i + " tests finished");
	}

	/**
	 * @param args
	 */
	private static void sortOnce(String[] args)
	{
		Integer t[] = getRandomArray(args, 251);

		doSort(t, new BubbleSort());
		doSort(t, new InsertionSort());
		doSort(t, new ShellSort());
		doSort(t, new SelectionSort());
		doSort(t, new QuickSort());
		doSort(t, new HeapSort());
		doSort(t, new MergeSort());
		doSort(t, new MergeSortIterative());
		doSort(t, new MergeSort2Ways());
	}

	/**
	 * @param t
	 * @param sorter
	 */
	private static void doSort(Integer[] t, ISortAlgorithm sorter)
	{
		Integer copy[] = t.clone();
		long start = System.currentTimeMillis();
		sorter.sort(copy);
		long end = System.currentTimeMillis();
		System.out.println(sorter.getName() + ": elements: " + t.length + " Runtime: " + (end - start));

		start = System.currentTimeMillis();
		sorter.sort(copy);
		end = System.currentTimeMillis();
		System.out.println(
				sorter.getName() + ": elements: " + t.length + " Runtime: " + (end - start) + " on sorted data");
	}

	/**
	 * @param args
	 */
	private static void testSort(String[] args)
	{
		Integer t[] = getRandomArray(args, System.currentTimeMillis());
		Integer copy1[] = t.clone();
		Integer copy2[] = t.clone();

		SortAlgorithmBase sorter = new QuickSort();
		// SortAlgorithmBase sorter = new InsertionSort();
		// sorter.printArray(t);
		sorter.sort(copy1);
		Arrays.sort(copy2);
		if (!Arrays.deepEquals(copy1, copy2)) {
			System.out.println("Error: " + Arrays.toString(t) + "\nWas sorted to: " + Arrays.toString(copy1));
		}
		sorter.printArray(copy1);
	}

	/**
	 * If a number is passed in command line args, that number of elements is
	 * generated in random order, if not a standard array is used
	 *
	 * @param args the command line arguments
	 * @return an array of Integer elements to be sorted
	 */
	private static Integer[] getRandomArray(String[] args, long randomSeed)
	{
		Integer[] result;
		int vec[] = NUMBERS;
		if (args.length != 0) {
			// generate random number of Integers to sort
			int size = Integer.valueOf(args[0]).intValue();
			result = new Integer[size];
			Random rand = new Random(randomSeed);
			for (int i = 0; i < size; i++) {
				result[i] = Integer.valueOf(Math.abs(rand.nextInt()) % (size * 2));
			}
		} else {
			// use default array to sort
			result = new Integer[vec.length];
			for (int i = 0; i < vec.length; i++) {
				result[i] = Integer.valueOf(vec[i]);
			}
		}
		return result;
	}
}
