/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.statemachine;

/**
 * Interface to access state factories
 * @author klaus
 *
 * @param <K> the enum used to identify states
 * @param <E> the enum used to identify events
 */
public interface IStateFactory<K, E> {
	/**
	 * @param state the state enum of the state to retrieve
	 * @return the state that is associated with the state enum
	 */
	ObjectState<K, E> getState(K state);

	/**
	 * Returns the state to start with
	 * @return the state that is the first state a context object is in
	 */
	ObjectState<K, E> getInitialState();
}