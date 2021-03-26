/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import kdo.search.representation.ArithmeticComparable;
import kdo.search.representation.IntegerAC;

/**
 * ADD CLASS/INTERFACE-COMMENT!!!
 *
 * @author Klaus Dorer
 */
public class ClassicSearchMain
{
	static int NUMBERS[] = {1, 2, 3, 4, 5, 6, 7};

	private static int LOOPS = 100;

	/**
	 * Runs some search strategies
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		String[] newArgs = new String[1];
		newArgs[0] = "" + LOOPS;
		List<ArithmeticComparable<IntegerAC>> data = getArrayToSearch(newArgs);

		ISearchAlgorithm searchStrategy;

		System.out.println("Starting...");

		searchStrategy = new SequentialSearch();
		testSearch(searchStrategy, data);

		searchStrategy = new BinarySearch();
		testSearch(searchStrategy, data);

		searchStrategy = new FibonacciSearch();
		testSearch(searchStrategy, data);

		searchStrategy = new ExponentialSearch();
		testSearch(searchStrategy, data);

		searchStrategy = new InterpolationSearch();
		testSearch(searchStrategy, data);
	}

	/**
	 * Tests the runtime of the passed search strategy by searching random
	 * elements
	 *
	 * @param searchStrategy
	 */
	private static void testSearch(ISearchAlgorithm searchStrategy, List<ArithmeticComparable<IntegerAC>> data)
	{
		int size = data.size();
		long start = System.currentTimeMillis();
		int failures = 0;

		// we always want to have the same elements
		Random rand = new Random(251);
		for (int i = 0; i < LOOPS; i++) {
			// we allow key to be 2 times bigger than size so that every second
			// search fails
			IntegerAC key = new IntegerAC(Math.abs(rand.nextInt()) % (size * 2));
			int result = searchStrategy.search(key, data);
			if (result < 0) {
				failures++;
			}
		}

		long end = System.currentTimeMillis();
		System.out.println(searchStrategy.getName() + ": Searching " + LOOPS + " times took: " + (end - start) +
						   " ms with " + failures + " failures");
	}

	/**
	 * If a number is passed in command line args, that number of elements is
	 * generated in ascending order, if not a standard array is used
	 *
	 * @param args the command line arguments
	 * @return an array of Integer elements to be sorted
	 */
	private static List<ArithmeticComparable<IntegerAC>> getArrayToSearch(String[] args)
	{
		List<ArithmeticComparable<IntegerAC>> result;
		int vec[] = NUMBERS;
		if (args.length != 0) {
			// generate random number of Integers to sort
			int size = Integer.valueOf(args[0]).intValue();
			result = new ArrayList<ArithmeticComparable<IntegerAC>>(size);
			for (int i = 0; i < size; i++) {
				result.add(new IntegerAC(2 * i));
			}
		} else {
			// use default array to sort
			result = new ArrayList<ArithmeticComparable<IntegerAC>>(vec.length);
			for (int i = 0; i < vec.length; i++) {
				result.add(new IntegerAC(vec[i]));
			}
		}
		return result;
	}
}
