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
import java.util.List;
import java.util.TreeSet;
import kdo.search.impl.TreeSearchStrategy;
import kdo.search.representation.Node;
import kdo.search.representation.Node.NodeUtilityIDComparator;

/**
 * Base class for all search methods that use some kind of node utility to
 * determine the next node to explore
 */
public abstract class NodeUtilitySearch extends TreeSearchStrategy
{
	/**
	 * @param name the name of this strategy
	 */
	public NodeUtilitySearch(String name)
	{
		super(name);
	}

	/**
	 * Uses a TreeSet for its efficiency in adding an element sorted into the
	 * collection.
	 * @see kdo.search.strategy.ITreeSearchStrategy#createDataStructure()
	 */
	@Override
	protected Collection<Node> createDataStructure()
	{
		return new TreeSet<>(new NodeUtilityIDComparator());
	}

	/**
	 * Adds a list of nodes to the collection of nodes. The nodes are not
	 * reordered.
	 * @param nodes the list of nodes to add
	 */
	@Override
	protected void addNodes(List<Node> nodes)
	{
		// calculate the utility value for all nodes
		for (Node node : nodes) {
			// The utility will be calculated differently for various algorithms
			node.setUtility(getUtility(node));
		}

		// now insert the nodes sorted
		super.addNodes(nodes);
	}

	/**
	 * Determines the value to use as utility function
	 * @param node the node for which we need a utility
	 * @return the utility to be used by the search method
	 */
	protected abstract float getUtility(Node node);

	/*
	 * @see search.strategy.ISearchStrategy#getNextNode()
	 */
	@Override
	protected Node getNextNode()
	{
		if (nodes.isEmpty()) {
			return null;
		}
		Node result = ((TreeSet<Node>) nodes).first();
		nodes.remove(result);
		// System.out.println("Depth: " + result.getDepth() + " util: " +
		// result.getUtility()
		// + " path: " + result.getPathCost() + " size: " + nodes.size());
		return result;
	}
}
