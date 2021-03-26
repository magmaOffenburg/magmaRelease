/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy.informed;

import java.util.List;
import kdo.domain.IProblemState;
import kdo.domain.model.ProblemState;
import kdo.search.representation.Node;
import kdo.search.strategy.ITreeSearchStrategy;

/**
 * Represents the A* search strategy that optimistically only inserts the best
 * of new nodes in the hope that no backtracking is necessary. If it is, it
 * switches back to the passed search.
 */
public class AStarSearchOptimistic extends AStarSearch
{
	/** the backup strategy if we were too optimistic */
	private ITreeSearchStrategy decoratee;

	/** the best utility of those nodes we did not insert */
	private float bestUtilityOfOthers;

	/**
	 * Default constructor
	 */
	public AStarSearchOptimistic(ITreeSearchStrategy decoratee)
	{
		super("optimistic A* search");
		this.decoratee = decoratee;
	}

	/**
	 * @see ITreeSearchStrategy#search(ProblemState)
	 */
	@Override
	public List<Node> search(IProblemState initialState)
	{
		bestUtilityOfOthers = Float.POSITIVE_INFINITY;
		List<Node> search = super.search(initialState);
		if (search == null) {
			System.out.println("Switch to : " + decoratee.getName());
			return decoratee.search(initialState);
		}
		return search;
	}

	/**
	 * Adds a list of nodes to the collection of nodes. The nodes are not
	 * reordered.
	 * @param nodes the list of nodes to add
	 */
	@Override
	protected void addNodes(List<Node> newNodes)
	{
		Node bestNode = null;
		// calculate the utility value for all nodes
		for (Node node : newNodes) {
			// The utility will be calculated differently for various algorithms
			node.setUtility(getUtility(node));
			if (bestNode == null || node.getUtility() < bestNode.getUtility()) {
				// the previously bestNode is now not inserted
				if (bestNode != null && bestNode.getUtility() < bestUtilityOfOthers) {
					bestUtilityOfOthers = bestNode.getUtility();
				}
				bestNode = node;
			} else {
				// we do not insert current node
				if (node.getUtility() < bestUtilityOfOthers) {
					bestUtilityOfOthers = node.getUtility();
				}
			}
		}

		// now insert the best node sorted if better or equal to the best not
		// inserted to guarantee optimality
		if (bestNode.getUtility() <= bestUtilityOfOthers) {
			nodes.add(bestNode);
		}
	}
}
