/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.representation;

/**
 * Adds arithmetic operations to the Comparable interface to allow calculation
 * on keys
 *
 * @author Klaus Dorer
 */
public interface ArithmeticComparable<T> extends Comparable<T> {
	/**
	 * Subtracts the passed ArithmeticComparable from this
	 *
	 * @param other the value to subtract from this
	 * @return int interpretation of the subtraction result
	 */
	int subtract(T other);

	/**
	 * Adds the passed ArithmeticComparable to this
	 *
	 * @param other the value to add to this
	 * @return int interpretation of the addition result
	 */
	int add(T other);
}
