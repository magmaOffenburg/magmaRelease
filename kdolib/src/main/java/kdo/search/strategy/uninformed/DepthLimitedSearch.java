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
import java.util.Stack;
import kdo.search.impl.TreeSearchStrategy;
import kdo.search.representation.Node;

/**
 * Implementation of the depth first search strategy using a LIFO Stack.
 */
public class DepthLimitedSearch extends TreeSearchStrategy
{
	/** the limit up to which to search */
	private final int depthLimit;

	/**
	 * Constructor creating the node collection representation.
	 */
	public DepthLimitedSearch(int depthLimit)
	{
		super("DepthLimited search");
		this.depthLimit = depthLimit;
	}

	/**
	 * DepthLimitedSearch uses a Stack for its efficiency in removing the last
	 * element and adding elements at the end of the collection.
	 * @see kdo.search.strategy.ITreeSearchStrategy#createDataStructure()
	 */
	@Override
	public Collection<Node> createDataStructure()
	{
		return new Stack<>();
	}

	/**
	 * Adds a single node to the collection of nodes.
	 * @param node the node to add
	 */
	@Override
	protected void addRootNode(Node node)
	{
		if (node.getDepth() <= depthLimit) {
			super.addRootNode(node);
		}
	}

	/**
	 * Adds a list of nodes to the collection of nodes. The nodes are not
	 * reordered. Assumes that all nodes have the same depth and at least one
	 * node is added.
	 * @param nodes the list of nodes to add
	 */
	@Override
	protected void addNodes(List<Node> nodes)
	{
		if ((nodes.get(0)).getDepth() <= depthLimit) {
			this.nodes.addAll(nodes);
			checkMaxNodes();
		}
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

		return ((Stack<Node>) nodes).pop();
	}
}
