/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import java.util.List;
import kdo.search.strategy.IStrategy;

public interface ISearchAlgorithm extends IStrategy {
	/**
	 * Searches the passed key in the passed List of data.
	 *
	 * @param key the key to search for in the passed data
	 * @param data the List containing the data to search in
	 * @return the index of the object represented by the passed key, -1 if not
	 *         found
	 */
	<T> int search(T key, List<? extends Comparable<? super T>> data);
}