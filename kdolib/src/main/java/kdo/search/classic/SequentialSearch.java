/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import java.util.List;
import kdo.search.strategy.base.StrategyBase;

/**
 * Search by running sequentially across all Elements until the key is found or
 * the end of the List is reached. No sorted list required
 *
 * @author Klaus Dorer
 */
public class SequentialSearch extends StrategyBase implements ISearchAlgorithm
{
	public SequentialSearch()
	{
		super("Sequential Search");
	}

	/**
	 * Implements the interface method by running sequentially across all
	 * Elements until the key is found or the end of the List is reached. No
	 * sorted list required
	 *
	 * @see kdo.search.classic.ISearchAlgorithm#search(java.lang.Comparable,
	 *      java.util.List)
	 */
	@Override
	public <T> int search(T key, List<? extends Comparable<? super T>> data)
	{
		int i = 0;
		for (Comparable<? super T> element : data) {
			if (element.compareTo(key) == 0) {
				return i;
			}
			i++;
		}
		return -1;
	}
}
