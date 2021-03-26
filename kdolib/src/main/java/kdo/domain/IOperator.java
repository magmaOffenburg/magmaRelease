/*
 * Copyright (c) 2001-2004 Living Systems(R) GmbH, Germany.
 * All rights reserved.
 * Original Author: kdorer
 *
 * $Source: /cvs/Research/lecture/AI/src/domain/IOperator.java,v $
 * $Date: 2007/03/14 17:25:15 $
 */
package kdo.domain;

/**
 * ADD CLASS/INTERFACE-COMMENT!!!
 * @author Klaus Dorer
 */
public interface IOperator extends Comparable<IOperator> {
	/**
	 * Returns the name of this operator
	 * @return the name of this operator
	 */
	String getName();

	/**
	 * Returns the cost for applying this operator
	 * @return the cost for applying this operator
	 */
	float getCost();

	/**
	 * Returns the state reached after applying the operator to the state passed.
	 * This method might be implemented in a non-deterministic manner.
	 * @param state the state to which to apply the operator
	 * @return the state reached after applying the operator to the state passed
	 */
	IProblemState getSuccessorState(IProblemState state);
}