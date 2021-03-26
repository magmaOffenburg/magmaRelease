/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy.uninformed;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import kdo.search.impl.TreeSearchStrategy;
import kdo.search.representation.Node;

/**
 * Implementation of the breadth first search strategy using a FIFO ArrayList.
 */
public class BreadthFirstSearch extends TreeSearchStrategy
{
	/**
	 * Default constructor
	 */
	public BreadthFirstSearch()
	{
		super("BreadthFirst search");
	}

	/**
	 * BreadthFirstSearch uses a LinkedList for its efficiency in removing the
	 * first element and adding elements at the end of the collection.
	 * @see kdo.search.strategy.ITreeSearchStrategy#createDataStructure()
	 */
	@Override
	protected Collection<Node> createDataStructure()
	{
		// return new ArrayList<Node>();
		return new LinkedList<>();
	}

	/**
	 * Returns the next node to explore, which is the oldest node added.
	 * @return the next node to be explored, null if no more nodes are available
	 */
	@Override
	protected Node getNextNode()
	{
		if (nodes.isEmpty()) {
			return null;
		}

		return ((List<Node>) nodes).remove(0);
	}
}
