/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.util.statemachine;

/**
 * Represents a transition from one state of a state machine to another
 * @param <K> the enum used to identify states
 * @param <E> the enum used to identify events
 * @author dorer
 */
public class StateTransition<K, E>
{
	/** ID of the source state */
	private K source;

	/** ID of the destination state */
	private K destination;

	/** Event ID that triggers this transition */
	private E trigger;

	/**
	 * @param source id of the source state from which to transition
	 * @param destination id of the destination state to which to transition
	 * @param trigger the event id that triggers the transition
	 */
	public StateTransition(K source, K destination, E trigger)
	{
		super();
		this.source = source;
		this.destination = destination;
		this.trigger = trigger;
	}

	/**
	 * @return the source state ID
	 */
	public K getSource()
	{
		return source;
	}

	/**
	 * @return the destination state ID
	 */
	public K getDestination()
	{
		return destination;
	}

	/**
	 * Checks if this transition can be triggered. This is true if the
	 * corresponding event occurred and the guard condition is satisfied.
	 * @param eventID the event that triggered checking
	 * @param context the object that triggered the event
	 * @return true if this transition is allowed
	 */
	public boolean canTrigger(E eventID, Object context)
	{
		if (eventID != trigger) {
			// not the same event
			return false;
		}

		if (!checkGuardCondition(context)) {
			// guard condition not fulfilled
			return false;
		}

		return true;
	}

	/**
	 * Checks the guard condition of this transition. The default implementation
	 * always returns true implementing the situation where there is no guard
	 * condition
	 * @param context the context object
	 * @return true if the guard of this transition is fulfilled. The default
	 *         implementation always returns true
	 */
	protected boolean checkGuardCondition(Object context)
	{
		return true;
	}

	/**
	 * Performs the transition part of a state change (but not the state change
	 * itself)
	 * @param context the object that triggered the event
	 */
	public void transitionAction(Object context)
	{
	}
}
