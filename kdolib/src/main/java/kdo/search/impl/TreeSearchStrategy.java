/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import kdo.domain.IProblemState;
import kdo.domain.model.ProblemState;
import kdo.search.representation.Node;
import kdo.search.strategy.ITreeSearchStrategy;
import kdo.search.strategy.base.StrategyBase;

/**
 * Implementation of the breadth first search strategy using a LIFO ArrayList.
 */
public abstract class TreeSearchStrategy extends StrategyBase implements ITreeSearchStrategy
{
	/** the list that stores the nodes to explore */
	protected Collection<Node> nodes;

	/** the maximal number of nodes held so far */
	private int maxNodes;

	/**
	 * Constructor creating the node collection representation.
	 */
	public TreeSearchStrategy()
	{
		this("Tree search");
	}

	/**
	 * Constructor creating the node collection representation.
	 * @param name the name of this strategy
	 */
	public TreeSearchStrategy(String name)
	{
		super(name);
		// create the underlying data structure for the nodes
		nodes = createDataStructure();

		// initialized with 1 to also count the root node
		maxNodes = 1;
	}

	/**
	 * @see ITreeSearchStrategy#search(ProblemState)
	 */
	@Override
	public List<Node> search(IProblemState initialState)
	{
		// Multiple calls should start with an empty node list
		nodes.clear();

		// create the root node and pass it to the search strategy
		Node rootNode = new Node(initialState);
		addRootNode(rootNode);

		while (true) {
			// get the next node to explore
			Node currentNode = getNextNode();

			// System.out.println("Checking: utility: " + currentNode.getUtility()
			// + " pathcost: "
			// + currentNode.getPathCost());

			if (currentNode == null) {
				// the search was not able to find a goal state
				return null;
			}

			// check if we reached a goal state
			if (currentNode.getState().checkGoalState()) {
				// we found a solution
				// nodes.clear();
				return currentNode.getSolutionPath();
			}

			// expand the current node and continue the search
			List<Node> newNodes = currentNode.expand();
			if (!newNodes.isEmpty()) {
				addNodes(newNodes);
			}
		}
	}

	/**
	 * Creates the underlying data structure for managing the nodes of the search
	 * tree
	 *
	 * @return a Collection data structure for managing the nodes
	 */
	protected Collection<Node> createDataStructure()
	{
		return new ArrayList<>();
	}

	/**
	 * Returns the next node to explore, which is the oldest node added.
	 * @return the next node to be explored, null if no more nodes are available
	 */
	protected abstract Node getNextNode();

	/**
	 * Adds a single node to the collection of nodes.
	 * @param node the node to add
	 */
	protected void addRootNode(Node node)
	{
		nodes.add(node);
	}

	/**
	 * Adds a list of nodes to the collection of nodes. The nodes are not
	 * reordered.
	 * @param newNodes the list of nodes to add
	 */
	protected void addNodes(List<Node> newNodes)
	{
		nodes.addAll(newNodes);
		checkMaxNodes();
	}

	/**
	 * Cares for counting the max number of nodes stored so far. Should be called
	 * by the addNodes() method of implementing classes
	 */
	protected void checkMaxNodes()
	{
		if (nodes.size() > maxNodes) {
			maxNodes = nodes.size();
		}
	}

	/**
	 * Returns the highest number of nodes stored so far
	 * @return the highest number of nodes stored so far
	 */
	@Override
	public int getMaxNumberOfNodesStored()
	{
		return maxNodes;
	}

	/**
	 * Returns the list of expanded but not yet explored nodes
	 * @return the list of expanded but not yet explored nodes
	 */
	public Collection<Node> getNodes()
	{
		return Collections.unmodifiableCollection(nodes);
	}
}
