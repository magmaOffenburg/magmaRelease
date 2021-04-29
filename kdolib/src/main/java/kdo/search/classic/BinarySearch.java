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
public class BinarySearch extends StrategyBase implements ISearchAlgorithm
{
	public BinarySearch()
	{
		super("Binary Search");
	}

	/**
	 * Search making use of sorted data by stepping consecutively into the half
	 * of the list
	 *
	 * @see kdo.search.classic.ISearchAlgorithm#search(java.lang.Comparable,
	 *      java.util.List)
	 */
	@Override
	public <T> int search(T key, List<? extends Comparable<? super T>> data)
	{
		return search(key, data, 0, data.size() - 1);
	}

	/**
	 * Search making use of sorted data by stepping consecutively into the half
	 * of the list
	 *
	 * @param key the key to search
	 * @param data the data in which to search
	 * @param left the index of the left border to search (including)
	 * @param right the index of the right border to search (including)
	 * @return the index of the object represented by the passed key, -1 if not
	 *         found
	 */
	public <T> int search(T key, List<? extends Comparable<? super T>> data, int left, int right)
	{
		if (left <= right) {
			int middle = (right + left) / 2;
			Comparable<? super T> middleElement = data.get(middle);
			int result = middleElement.compareTo(key);
			if (result > 0) {
				// continue search in left half
				return search(key, data, left, middle - 1);
			} else if (result < 0) {
				// continue search in right half
				return search(key, data, middle + 1, right);
			} else {
				// we found the element
				return middle;
			}
		}
		// element not found
		return -1;
	}
}
