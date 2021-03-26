/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.reinforcement;

import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Interface that separates the value function learning agent from possible
 * value function update strategies
 *
 * @author KDorer
 */
public interface IValueFunctionUpdate {
	/**
	 * Called by a kdo.reinforcement agent to update the value function of a
	 * problem domain.
	 * @param currentState the current state the agent is in
	 * @param percepts a list of perceptions the agent made so far starting from
	 *        the initial state
	 * @param lastAction the action that has been performed to get to the
	 *        currentState
	 */
	void update(IProblemState currentState, List<IProblemState> percepts, IOperator lastAction);
}
