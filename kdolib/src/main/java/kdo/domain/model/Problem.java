/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */

package kdo.domain.model;

import java.util.List;
import kdo.domain.IConcurrentProblem;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Represents a domain problem containing the initial state, operator
 * descriptions a goal test and cost functions
 */
public abstract class Problem implements IConcurrentProblem
{
	/** the initial state from which to start the search */
	protected IProblemState initialState;

	/** the current state of the problem */
	protected IProblemState currentState;

	/** the list of operators allowed for an agent */
	protected List<IOperator> operators;

	/** the current state of concurrent optimization */
	protected IProblemState concurrentState;

	/**
	 * Constructor creating a new domain problem instance.
	 * @param initialState the starting state from which to start the search
	 * @param operators a list of operators allowed for an agent
	 */
	public Problem(List<IOperator> operators)
	{
		this.operators = operators;
	}

	/**
	 * @see kdo.domain.IProblem#getInitialState()
	 */
	public IProblemState getInitialState()
	{
		return initialState;
	}

	/**
	 * @see kdo.domain.IProblem#setInitialState(kdo.domain.IProblemState)
	 */
	public void setInitialState(IProblemState state)
	{
		initialState = state;
		this.currentState = initialState;
		this.concurrentState = initialState;
	}

	/**
	 * @see kdo.domain.IProblem#getCurrentState()
	 */
	public IProblemState getCurrentState()
	{
		return currentState;
	}

	/**
	 * @see kdo.domain.IProblem#setCurrentState(kdo.domain.IProblemState)
	 */
	public void setCurrentState(IProblemState state)
	{
		currentState = state;
	}

	/*
	 * The default implementation does not return a random state, but the initial
	 * state
	 *
	 * @see domain.IProblem#getRandomState()
	 */
	public IProblemState getRandomState()
	{
		return initialState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see domain.IProblemState#getRandomOperator(int)
	 */
	public IOperator getRandomOperator(int neighborhoodRadius)
	{
		// just implemented until all domains support this
		return null;
	}

	/**
	 * @return the operators of this domain (for domains that have all operators
	 *         always available)
	 */
	public List<IOperator> getOperators()
	{
		return operators;
	}

	public IProblemState getConcurrentState()
	{
		return concurrentState;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kdo.domain.IConcurrentProblem#switchConcurrentState(kdo.domain.
	 * IProblemState , kdo.domain.IProblemState)
	 */
	public IProblemState switchConcurrentState(IProblemState oldState, IProblemState newState)
	{
		synchronized (concurrentState)
		{
			if (oldState != concurrentState) {
				// our state has changed meanwhile
				return null;
			}

			if (oldState == newState) {
				// no real change
				return null;
			}

			concurrentState = newState;
			// System.out.println("switch of " + Thread.currentThread().getName());
			return newState;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kdo.domain.IConcurrentProblem#concurrentStateHasChanged(kdo.domain.
	 * IProblemState)
	 */
	public boolean concurrentStateHasChanged(IProblemState currentState)
	{
		synchronized (concurrentState)
		{
			return currentState != concurrentState;
		}
	}
}
