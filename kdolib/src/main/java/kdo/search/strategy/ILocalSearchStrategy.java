/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.search.strategy;

import kdo.domain.IProblemState;

/**
 * Interface for all local search methods
 */
public interface ILocalSearchStrategy extends IStrategy {
	/**
	 * Performs a local search, i.e. a search where only the current state is
	 * stored.
	 * @param initialState the state to start the search from
	 * @return the last state in which the search ended
	 */
	IProblemState search(IProblemState initialState);
}