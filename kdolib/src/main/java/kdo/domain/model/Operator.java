/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.domain.model;

import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Represents a state transition.
 */
public abstract class Operator implements IOperator
{
	/** the name of the operator */
	private final String name;

	/** the cost for applying this operator */
	protected float cost;

	/**
	 * Constructor creating a new operator object.
	 * @param cost the cost for applying this operator
	 */
	public Operator(String name, float cost)
	{
		this.name = name;
		this.cost = cost;
	}

	/**
	 * @see kdo.domain.IOperator#getName()
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @see kdo.domain.IOperator#getCost()
	 */
	public float getCost()
	{
		return cost;
	}

	/**
	 * set the cost of the operator
	 * @param cost of the operator
	 */
	public void setCost(float cost)
	{
		this.cost = cost;
	}

	/**
	 * @see kdo.domain.IOperator#getSuccessorState(kdo.domain.model.ProblemState)
	 */
	public abstract IProblemState getSuccessorState(IProblemState state);

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(IOperator o)
	{
		// Default implementation compares cost
		if (cost < o.getCost()) {
			return -1;
		} else if (cost > o.getCost()) {
			return 1;
		}
		return 0;
	}

	/**
	 * Returns a string representation of this object
	 * @return a string representation of this object
	 */
	@Override
	public String toString()
	{
		return name;
	}
}
