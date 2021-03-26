/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.representation;

/**
 * Integer implementation that implements ArithmeticComparable
 *
 * @author Klaus Dorer
 */
public class IntegerAC implements ArithmeticComparable<IntegerAC>
{
	/** the int value this class is representing */
	private final Integer value;

	/**
	 * Default constructor
	 *
	 * @param value the value to encapsulate
	 */
	public IntegerAC(int value)
	{
		this.value = Integer.valueOf(value);
	}

	/**
	 * Adds the passed int to this.
	 *
	 * @see kdo.search.representation.ArithmeticComparable#add(kdo.search.representation.ArithmeticComparable)
	 */
	public int add(IntegerAC other)
	{
		return value.intValue() + (other).value.intValue();
	}

	/**
	 * Subtracts the passed int from this.
	 *
	 * @see kdo.search.representation.ArithmeticComparable#subtract(kdo.search.representation.ArithmeticComparable)
	 */
	public int subtract(IntegerAC other)
	{
		return value.intValue() - (other).value.intValue();
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IntegerAC other)
	{
		return value.compareTo((other).value);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		return value.equals(((IntegerAC) other).value);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return value.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return value.toString();
	}
}
