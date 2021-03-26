/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.uninformed;

import kdo.search.representation.Node;

/**
 * Represents the Best First Search strategy that explores the nodes in order of
 * smallest path cost
 */
public class BestFirstSearch extends NodeUtilitySearch
{
	/**
	 * Default constructor
	 */
	public BestFirstSearch()
	{
		super("BestFirst search");
	}

	/**
	 * Determines the value to use as utility function
	 * @param node the node for which we need a utility
	 * @return the utility to be used by the search method
	 */
	@Override
	protected float getUtility(Node node)
	{
		return node.getPathCost();
	}
}
