/*
 * Copyright (c) 2001-2004 Living Systems(R) GmbH, Germany.
 * All rights reserved.
 * Original Author: kdorer
 *
 * $Source: /cvs/Research/lecture/AI/src/domain/IProblem.java,v $
 * $Date: 2007/03/14 17:25:15 $
 */
package kdo.domain;

import java.util.List;

/**
 * ADD CLASS/INTERFACE-COMMENT!!!
 * @author Klaus Dorer
 */
public interface IProblem {
	/**
	 * Returns the initial state from which to start
	 * @return the initial state from which to start
	 */
	IProblemState getInitialState();

	/**
	 * Sets the initial state from which to start
	 * @param state the initial state from which to start
	 */
	void setInitialState(IProblemState state);

	/**
	 * Returns the current state of the problem
	 * @return the current state of the problem
	 */
	IProblemState getCurrentState();

	/**
	 * Returns a random state of the problem
	 * @return a random state of the problem
	 */
	IProblemState getRandomState();

	/**
	 * Sets the current state of the problem
	 * @param state the current state of the problem
	 */
	void setCurrentState(IProblemState state);

	/**
	 * @return the list of all operators of the domain
	 */
	List<IOperator> getOperators();

	/**
	 *
	 * @param neighborhoodRadius the radius within which the operator can move
	 * @return a random operator within the passed step distance
	 */
	IOperator getRandomOperator(int neighborhoodRadius);
}