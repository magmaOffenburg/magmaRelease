/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.reinforcement;

import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Represents a passive kdo.reinforcement agent. This is an agent that perceives
 * the changes in its environment and learns the utility function of the states.
 *
 * @author KDorer
 */
public class PassiveRLAgent extends RLAgent
{
	/** the action the passive agent always decides on */
	IOperator passiveAction;

	/**
	 * Default Constructor to create the agent
	 *
	 * @param vlaueUpdateStrategy the strategy to use for value function update
	 * @param actionStrategy the action strategy the agent follows
	 */
	public PassiveRLAgent(IValueFunctionUpdate vlaueUpdateStrategy, IStateMapper mapper, IOperator actionStrategy)
	{
		super(vlaueUpdateStrategy, mapper, 0);
		passiveAction = actionStrategy;
	}

	/**
	 * A passive agent always returns its default strategy action
	 * @param currentState the state on which to decide
	 * @return the default strategy action
	 */
	@Override
	public IOperator decideOnAction(IProblemState currentState)
	{
		return passiveAction;
	}

	@Override
	public String toString()
	{
		return "Passive action: " + passiveAction;
	}
}
