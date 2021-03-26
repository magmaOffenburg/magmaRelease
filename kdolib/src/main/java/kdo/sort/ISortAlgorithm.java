/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.sort;

import kdo.search.strategy.IStrategy;

public interface ISortAlgorithm extends IStrategy {
	/**
	 * Sorts the passed array of Comparables according to their natural ordering
	 * @param a the array to sort and that is sorted (in situ)
	 */
	<T extends Comparable<? super T>> void sort(T a[]);
}