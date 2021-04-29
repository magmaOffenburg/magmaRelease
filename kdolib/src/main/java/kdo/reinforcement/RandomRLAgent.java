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
import kdo.util.IRandomSource;

/**
 * Represents a random kdo.reinforcement agent. This is an agent that perceives
 * the changes in its environment and learns the utility function of the states
 * by choosing random actions all the time.
 *
 * @author KDorer
 */
public class RandomRLAgent extends RLAgent
{
	/** source for random numbers */
	private IRandomSource rand;

	/**
	 * Default Constructor to create the agent
	 */
	public RandomRLAgent(IValueFunctionUpdate vlaueUpdateStrategy, IStateMapper mapper, IRandomSource rand)
	{
		super(vlaueUpdateStrategy, mapper, 0);
		this.rand = rand;
	}

	/**
	 * A passive agent always returns its default strategy action
	 * @param currentState the state on which to decide
	 * @return the default strategy action
	 */
	@Override
	public IOperator decideOnAction(IProblemState currentState)
	{
		List<IOperator> actions = currentState.getOperators();
		int random = rand.nextInt(actions.size());
		return actions.get(random);
	}
}
