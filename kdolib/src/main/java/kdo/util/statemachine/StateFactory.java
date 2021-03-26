/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */

package kdo.util.statemachine;

import java.util.Map;

/**
 * Generic factory class for state machine pattern
 * @author klaus
 *
 * @param <K> the enum used to identify states
 * @param <E> the enum used to identify events
 */
public abstract class StateFactory<K, E> implements IStateFactory<K, E>
{
	/** map containing all states */
	protected Map<K, ObjectState<K, E>> states;

	/**
	 * Default constructor creating the states and transitions of the state
	 * machine
	 */
	public StateFactory()
	{
		createStates();
		createTransitions();
	}

	/**
	 * Called to create the states that are allowed in this state machine
	 */
	protected abstract void createStates();

	/**
	 * Called to create the state transitions that are allowed in this state
	 * machine
	 */
	protected abstract void createTransitions();

	/*
	 * (non-Javadoc)
	 *
	 * @see IStateFactory#getState(K)
	 */
	public ObjectState<K, E> getState(K state)
	{
		return states.get(state);
	}
}
