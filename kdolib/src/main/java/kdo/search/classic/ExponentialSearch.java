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
public class ExponentialSearch extends StrategyBase implements ISearchAlgorithm
{
	/** instance to do the binary search once limits are clear */
	BinarySearch binarySearcher = new BinarySearch();

	public ExponentialSearch()
	{
		super("Exponential Search");
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
		int left = 0;
		int right = 1;
		int size = data.size();
		while (right < size && data.get(right).compareTo(key) < 0) {
			right = right + right;
		}
		left = right / 2;

		right = (right < size) ? right : size - 1;
		return binarySearcher.search(key, data, left, right);
	}
}
