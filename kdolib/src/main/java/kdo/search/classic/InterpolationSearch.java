/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.classic;

import java.util.List;
import kdo.search.representation.ArithmeticComparable;
import kdo.search.strategy.base.StrategyBase;

/**
 * Search making use of sorted data and arithmetic on keys by stepping
 * consecutively into the estimated position of the list. Runtime Complexity is
 * O(ld ld n) if data is uniformly distributed, O(n) in the worst case
 *
 * @author Klaus Dorer
 */
public class InterpolationSearch extends StrategyBase implements ISearchAlgorithm
{
	public InterpolationSearch()
	{
		super("Interpolation Search");
	}

	/**
	 * Search using interpolation of key values
	 *
	 * @see kdo.search.classic.ISearchAlgorithm#search(java.lang.Comparable,
	 *      java.util.List)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> int search(T key, List<? extends Comparable<? super T>> data)
	{
		return search(key, (List<? extends ArithmeticComparable<T>>) data, 0, data.size() - 1);
	}

	/**
	 * Search using interpolation of key values
	 *
	 * @param key the key to search
	 * @param data the data in which to search
	 * @param left the index of the left border to search
	 * @param right the index of the right border to search
	 * @return the index of the object represented by the passed key, -1 if not
	 *         found
	 */
	@SuppressWarnings("unchecked")
	public <T> int search(T key, List<? extends ArithmeticComparable<T>> data, int left, int right)
	{
		assert key != null : "Search called with null key";
		assert data != null : "Search called with null data";

		while (left <= right) {
			T leftElement = (T) data.get(left);
			ArithmeticComparable<T> rightElement = data.get(right);
			float factor = 0.5f;
			int delta1 = ((ArithmeticComparable<T>) key).subtract(leftElement);
			if (delta1 >= 0) {
				int delta2 = rightElement.subtract(leftElement);
				if (delta2 > 0 && delta1 <= delta2) {
					factor = delta1 / (float) delta2;
				} else if (rightElement.compareTo(key) == 0) {
					return right;
				} else {
					// not found, element is bigger than biggest, or list is not
					// sorted
					return -1;
				}
			}
			int i = (int) (left + (right - left) * factor);
			ArithmeticComparable<? super T> currentElement = data.get(i);
			int result = currentElement.compareTo(key);
			if (result == 0) {
				// found
				return i;
			} else if (result < 0) {
				// searched element must be in right half
				left = i + 1;
			} else {
				// searched element must be in left half
				right = i - 1;
			}
		}
		// not found
		return -1;
	}
}
