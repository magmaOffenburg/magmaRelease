/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a list of propositions logically connected by AND
 */
public class Condition
{
	/** List of preconditions comprising the condition */
	protected final List<Proposition> propositions;

	/**
	 * Default constructor
	 */
	public Condition()
	{
		propositions = new ArrayList<Proposition>();
	}

	/**
	 * Constructor
	 *
	 * @param proposition Proposition to add to this condition
	 */
	public Condition(Proposition proposition)
	{
		this();
		addProposition(proposition);
	}

	/**
	 * Retrieve an iterator to the propositions of this condition
	 *
	 * @return Iterator
	 */
	public Iterator<Proposition> getPropositions()
	{
		return propositions.iterator();
	}

	/**
	 * Calculate the total truth value of the condition from the truth values of
	 * all propositions connected by fuzzy and (product).
	 *
	 * @return Calculated truth value
	 */
	public double getTruthValue()
	{
		Proposition proposition;
		double result = 1.0;

		for (Proposition proposition1 : propositions) {
			proposition = proposition1;
			result *= proposition.getTruthValue();
		}

		return result;
	}

	/**
	 * Adds a proposition to this condition
	 * @param proposition the proposition to add
	 */
	public void addProposition(Proposition proposition)
	{
		propositions.add(proposition);
	}

	/**
	 * Returns the information about this goal.
	 *
	 * @param tabs - the number of tabs used to make indentation in the output.
	 * @return the information about this goal.
	 */
	public String toString(int tabs)
	{
		StringBuffer info = new StringBuffer(100);

		for (Proposition proposition : propositions) {
			if (info.length() > 0) {
				info.append(", ");
			}

			info.append(proposition.toString());
		}

		return info.toString();
	}
}
