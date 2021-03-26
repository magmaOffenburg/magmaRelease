/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.informed;

import kdo.search.representation.Node;
import kdo.search.strategy.uninformed.NodeUtilitySearch;

/**
 * Represents the A* search strategy
 */
public class AStarSearch extends NodeUtilitySearch
{
	/**
	 * Default constructor
	 */
	public AStarSearch()
	{
		this("A* search");
	}

	/**
	 * constructor with name
	 */
	public AStarSearch(String name)
	{
		super(name);
	}

	/**
	 * Determines the value to use as utility function
	 * @param node the node for which we need a utility
	 * @return the utility to be used by the search method
	 */
	@Override
	protected float getUtility(Node node)
	{
		return node.calculateEstimatedCost() + node.getPathCost();
	}
}
