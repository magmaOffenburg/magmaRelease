/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.search.strategy;

import java.util.List;
import kdo.domain.IProblemState;
import kdo.search.representation.Node;

/**
 * Interface for search strategies.
 */
public interface ITreeSearchStrategy extends IStrategy {
	/**
	 * Search method to search using this strategy.
	 * @param initialState the state to start with searching
	 * @return the path of nodes leading to the goal state, null if no result was
	 *         found
	 */
	List<Node> search(IProblemState initialState);

	/**
	 * Returns the highest number of nodes stored so far. Just used for debugging
	 * and statistics
	 * @return the highest number of nodes stored so far
	 */
	int getMaxNumberOfNodesStored();
}
