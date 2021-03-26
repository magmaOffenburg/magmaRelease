/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.statemachine;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all states implementing a state machine
 * @author klaus
 * @param <K> the enum used to identify states
 * @param <E> the enum used to identify events
 */
public abstract class ObjectState<K, E>
{
	/**
	 * reference to the state factory that contains this state and the
	 * transitions
	 */
	protected final IStateFactory<K, E> stateFactory;

	/** map containing all allowed state transitions from this state */
	protected final List<StateTransition<K, E>> transitions;

	/**
	 * Default constructor
	 * @param factory the state factory that contains this state
	 */
	public ObjectState(IStateFactory<K, E> factory)
	{
		stateFactory = factory;
		transitions = new ArrayList<StateTransition<K, E>>(2);
	}

	/**
	 * Adds a transition to the list of transitions of this state
	 * @param transition the new transition to add
	 */
	public void addTransition(StateTransition<K, E> transition)
	{
		transitions.add(transition);
	}

	/**
	 * Returns the id of this state
	 * @return the id of this state
	 */
	public abstract K getStateID();

	/**
	 * A new event occurred and is now processed
	 * @param eventID the event that happened
	 * @param context the object on which the event happened
	 * @return the possibly new state we are in. Make sure to assign it to the
	 *         context.
	 */
	public ObjectState<K, E> tiggerEvent(E eventID, Object context)
	{
		for (StateTransition<K, E> transition : transitions) {
			if (transition.canTrigger(eventID, context)) {
				// we have a state change
				transition.transitionAction(context);
				ObjectState<K, E> newState = switchTo(transition.getDestination(), context);
				System.out.println("switch to: " + transition.getDestination());

				return newState;
			}
		}
		// event did not cause state change
		return this;
	}

	/**
	 * Switches from this state to the specified state. If the specified state is
	 * identical to this it triggers a self transition. It is assumed that it was
	 * checked before if the transition is valid.
	 * @param newStateID the state to which to switch.
	 * @param context the context object we are state from
	 * @return the possibly new state we are in. Make sure to assign it to the
	 *         context.
	 */
	protected ObjectState<K, E> switchTo(K newStateID, Object context)
	{
		if (newStateID == this.getStateID()) {
			// staying in the same state
			onStayInState(context);
			return this;
		}

		// change to different state
		ObjectState<K, E> newState = stateFactory.getState(newStateID);
		this.onLeavingState(newState, context);
		newState.onEnteringState(this, context);
		return newState;
	}

	/**
	 * Notification sent before this state is entered. The default implementation
	 * does nothing.
	 */
	protected void onEnteringState(ObjectState<K, E> previousState, Object context)
	{
	}

	/**
	 * Notification sent when a state change is called that returns to this state
	 * again. The default implementation generally allows transitions from one
	 * state to itself and calls onLeavingState() and onEnteringState().
	 */
	protected void onStayInState(Object context)
	{
		onLeavingState(this, context);
		onEnteringState(this, context);
	}

	/**
	 * Notification sent before this state is entered. The default implementation
	 * does nothing.
	 */
	protected void onLeavingState(ObjectState<K, E> nextState, Object context)
	{
	}
}