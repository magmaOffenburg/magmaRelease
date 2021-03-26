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
 * Implementation of the depth first search strategy using a FIFO Stack.
 */
public class IterativeDeepeningSearch extends TreeSearchStrategy
{
	/** the root node of this search tree used for reiteration */
	private Node rootNode;

	/** the limit up to which to search */
	private int depthLimit;

	/** the depth reached within this iteration */
	private int maxDepthReached;

	/**
	 * Constructor creating the node collection representation.
	 */
	public IterativeDeepeningSearch()
	{
		super("IterativeDeepening search");
		depthLimit = 1;
		maxDepthReached = 1;
	}

	/**
	 * IterativeDeepeningSearch uses a Stack for its efficiency in removing the
	 * last element and adding elements at the end of the collection.
	 * @see kdo.search.strategy.ITreeSearchStrategy#createDataStructure()
	 */
	@Override
	protected Collection<Node> createDataStructure()
	{
		return new Stack<>();
	}

	/**
	 * Adds the route node to the collection of nodes.
	 * @param node the root node to add
	 */
	@Override
	protected void addRootNode(Node node)
	{
		addNode(node);
		rootNode = node;
	}

	/**
	 * Adds a list of nodes to the collection of nodes. The nodes are not
	 * reordered. Assumes that all nodes have the same depth.
	 * @param nodes the list of nodes to add
	 */
	@Override
	protected void addNodes(List<Node> nodes)
	{
		for (Node nextNode : nodes) {
			addNode(nextNode);
		}
		checkMaxNodes();
	}

	/**
	 * Returns the next node to explore, which is the oldest node added.
	 * @return the next node to be explored, null if no more nodes are available
	 */
	@Override
	protected Node getNextNode()
	{
		if (nodes.isEmpty()) {
			// check if further search is sensible
			if (maxDepthReached < depthLimit) {
				// no solution possible
				return null;
			} else {
				// make next iteration
				maxDepthReached = 0;
				depthLimit++;
				((Stack<Node>) nodes).push(rootNode);
			}
		}

		return ((Stack<Node>) nodes).pop();
	}

	/**
	 * Adds a single node to the collection of nodes.
	 * @param node the node to add
	 */
	private void addNode(Node node)
	{
		if (node.getDepth() <= depthLimit) {
			((Stack<Node>) nodes).push(node);
			if (node.getDepth() > maxDepthReached) {
				maxDepthReached = node.getDepth();
			}
		}
	}
}
