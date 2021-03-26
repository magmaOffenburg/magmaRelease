/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.informed;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import kdo.search.impl.TreeSearchStrategy;
import kdo.search.representation.Node;

/**
 * Represents the A* search strategy
 */
public class AStarSearchSpaceOptimized extends TreeSearchStrategy
{
	/**
	 * Default constructor
	 */
	public AStarSearchSpaceOptimized()
	{
		super("A* search space optimized");
	}

	/**
	 * AStarSearchSpaceOptimized uses a TreeSet for its efficiency in adding an
	 * element sorted into the collection.
	 * @see kdo.search.strategy.ITreeSearchStrategy#createDataStructure()
	 */
	@Override
	protected Collection<Node> createDataStructure()
	{
		return new TreeSet<>();
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
			// The utility for A* search is the sum of estimated costs and costs so
			// far
			node.setUtility(-(node.calculateEstimatedCost() + node.getPathCost()));
			// state information is not stored because of memory problems. it is
			// regenerated at getNextNode()
			node.setState(null);
		}

		// now insert the nodes sorted
		super.addNodes(nodes);
	}

	/*
	 * @see search.strategy.ISearchStrategy#getNextNode()
	 */
	@Override
	protected Node getNextNode()
	{
		if (nodes.isEmpty()) {
			return null;
		}
		Node result = ((TreeSet<Node>) nodes).last();
		nodes.remove(result);

		Node parent = result.getParent();
		if (parent == null) {
			// we did not delete the state for the root node
			return result;
		}

		// recreate the problem state of this node
		result.setState(result.getOperator().getSuccessorState(parent.getState()));

		return result;
	}
}
