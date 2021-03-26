/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain;

/**
 * Methods required for solving one problem with concurrent optimization
 * algorithms
 * @author dorer
 */
public interface IConcurrentProblem extends IProblem {
	/**
	 * @return the current state of concurrent optimization
	 */
	IProblemState getConcurrentState();

	/**
	 * Changes the current state if not meanwhile someone else has changed it.
	 * @param oldState the old state on which the transition is based.
	 * @param newState the new state to switch to
	 * @return the new state if the state change was performed, null if not e.g.
	 *         due to a previous change in the concurrent state
	 */
	IProblemState switchConcurrentState(IProblemState oldState, IProblemState newState);

	/**
	 * Checks if the passed state is identical with the concurrentState of the
	 * problem
	 * @param currentState the state to check against
	 * @return true if the passed state is the same instance as concurrentState
	 */
	boolean concurrentStateHasChanged(IProblemState currentState);
}
