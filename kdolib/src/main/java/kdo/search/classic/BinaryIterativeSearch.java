/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import java.util.List;
import kdo.search.strategy.base.StrategyBase;

/**
 * Search making use of sorted data by stepping consecutively into the half of
 * the list
 *
 * @author Klaus Dorer
 */
public class BinaryIterativeSearch extends StrategyBase implements ISearchAlgorithm
{
	public BinaryIterativeSearch()
	{
		super("Binary Search");
	}

	/**
	 * Search making use of sorted data by stepping consecutively into the half
	 * of the list
	 *
	 * @param key the key to search
	 * @param data the data in which to search
	 * @return the index of the object represented by the passed key, -1 if not
	 *         found
	 */
	@Override
	public <T> int search(T key, List<? extends Comparable<? super T>> data)
	{
		assert key != null : "Search called with null key";
		assert data != null : "Search called with null data";

		int left = 0;
		int right = data.size() - 1;

		while (left <= right) {
			int middle = (right + left) / 2;
			Comparable<? super T> currentElement = data.get(middle);
			int result = currentElement.compareTo(key);
			if (result == 0) {
				// found
				return middle;
			} else if (result < 0) {
				// searched element must be in right half
				left = middle + 1;
			} else {
				// searched element must be in left half
				right = middle - 1;
			}
		}
		// not found
		return -1;
	}
}
