/*
 * Copyright (c) 2001-2004 Living Systems(R) GmbH, Germany.
 * All rights reserved.
 * Original Author: kdorer
 *
 * $Source: /cvs/Research/lecture/AI/src/domain/IProblemState.java,v $
 * $Date: 2007/01/04 08:20:46 $
 */
package kdo.domain;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public interface IProblemState {
	/**
	 * Checks if this state is a goal state of this problem
	 *
	 * @return true if this state is a goal state of this problem
	 */
	boolean checkGoalState();

	/**
	 * Returns the list of operators applicable in this state. The default
	 * implementation returns a list of operators applicable in all states
	 *
	 * @return the list of operators applicable in this state
	 */
	List<IOperator> getOperators();

	/**
	 * Returns the problem (domain) to which this state belongs
	 *
	 * @return the problem (domain) to which this state belongs
	 */
	IProblem getProblem();

	/**
	 * Returns an iterator into the list of improvement operators applicable in
	 * this state. Other than getOperators() this method returns operators
	 * applicable in local search to transform a solution state into another
	 * solution state. The default implementation returns the list of all
	 * operators.
	 *
	 * @return an iterator over the list of improvement operators applicable in
	 *         this domain
	 */
	Iterator<IOperator> operatorIterator();

	/**
	 * @return an iterator that runs over all parameter variations possible
	 */
	Iterator<IOperator> fullSearchIterator();

	/**
	 * Returns a random operator from the list of all applicable operators in
	 * this state. The default implementation returns a random operator from the
	 * list of all operators of the problem.
	 *
	 * @return a random operator from the list of all operators of the problem
	 */
	IOperator getRandomOperator();

	/**
	 * Returns a random operator within a neighborhood radius passed
	 *
	 * @param neighborhoodRadius the radius of the neighborhood within which is
	 *        searched
	 * @return a random operator within a neighborhood radius passed
	 */
	IOperator getRandomOperator(int neighborhoodRadius);

	/**
	 * Displays this state. The default implementation prints toString() to the
	 * standard ouput
	 */
	void display();

	/**
	 * Returns an estimate for the remaining costs to get from this state to a
	 * goal state. The default implementation always returns 0.0. Overwrite this
	 * method to add heuristics information to informed search strategies.
	 *
	 * @return an estimate for the remaining costs to get from this state to a
	 *         goal state.
	 */
	float getHeuristicsValue();

	/**
	 * Calculates and returns the utility of this state
	 *
	 * @return the utility of this state
	 */
	float calculateUtility();

	/**
	 * Returns the utility of this state as calculated by the calculateUtility()
	 * method
	 *
	 * @return the utility of this state
	 */
	float getUtility();

	/**
	 * Sets the utility of this state. Usually used by learning agents to update
	 * utility values.
	 *
	 * @param util the new utility of this state
	 */
	void setUtility(float util);

	/**
	 * Sets the utility to be not calculated.
	 */
	void resetUtility();

	/**
	 * Returns the kdo.reinforcement the agents gets when exploring this state.
	 * The default implementation allways returns 0.
	 *
	 * @return the kdo.reinforcement the agents gets when exploring this state
	 */
	float getReinforcement();

	/**
	 * @return the number of times this agent explored this state
	 */
	int getExplorationCount();

	/**
	 * Called if this state is explored by the agent. The default implementation
	 * does nothing.
	 */
	void onExploration();

	/**
	 * Checks the consistency of this state to be defined by each problem state
	 * separately
	 */
	void checkConsistency();

	/**
	 * Called if this state was selected by the optimization
	 *
	 * @param bestOperator the operator that created this state
	 */
	void onSelection(IOperator bestOperator);

	/**
	 * Checks if this state is worse than the passed state
	 *
	 * @param successorState the state to check against
	 * @return true if this state is worse than the passed state
	 */
	boolean isWorseThan(IProblemState successorState);

	/**
	 * Writes this individuum to the passed file
	 *
	 * @param filename path and name of the file to write
	 * @throws IOException if writing is not possible
	 */
	void writeToFile(String filename) throws IOException;

	/**
	 * @return an array containing more properties/results of this problem state,
	 *         null if there are none (default)
	 */
	double[] getRuntimeProperties();

	/**
	 * @return the name of this state
	 */
	String getName();
}