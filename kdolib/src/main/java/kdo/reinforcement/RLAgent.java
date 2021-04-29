/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.reinforcement;

import java.util.ArrayList;
import java.util.List;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Base class of all kdo.reinforcement learning agents.
 *
 * @author KDorer
 */
public abstract class RLAgent
{
	/** the strategy to use for value function update */
	protected final IValueFunctionUpdate valueFunctionUpdateStrategy;

	/**
	 * History of perceived states. Elements are of type ProblemState. This is a
	 * simplification assuming that the problem is accessible. If it was not
	 * accessible this list should only contain the sequence of perceptions and a
	 * separate function mapping from perceptions to states needs to be used.
	 */
	protected final List<IProblemState> percepts;

	/** the last action the agent performed */
	protected IOperator lastAction;

	/** maps real states to learning states */
	private IStateMapper stateMapper;

	/** how many explorations of a state utility is added **/
	private long explorativeness;

	/**
	 * Default Constructor to create the agent
	 *
	 * @param vlaueUpdateStrategy the strategy to use for value function update
	 * @param mapper a mapping function from observations to states
	 * @param explorativeness how many explorations of a state utility is added
	 */
	public RLAgent(IValueFunctionUpdate vlaueUpdateStrategy, IStateMapper mapper, long explorativeness)
	{
		valueFunctionUpdateStrategy = vlaueUpdateStrategy;
		percepts = new ArrayList<>();
		lastAction = null;
		stateMapper = mapper;
		this.explorativeness = explorativeness;
	}

	/**
	 * Decides on the next action of the agent. For an active RL agent this is
	 * the agent that produces the highest expected utility, but also produces
	 * the highest information gain to learn correct utility values.
	 * @param currentState the current state the agent perceives (only for
	 *        accessible environments).
	 * @return the operator / action the agent decided to take
	 */
	public IOperator decide(IProblemState currentState)
	{
		IProblemState mappedState = currentState;
		if (stateMapper != null) {
			mappedState = stateMapper.map(currentState);
		}

		// add new perception to the list of perceptions
		onNewState(mappedState, lastAction);

		// update the value function
		valueFunctionUpdateStrategy.update(mappedState, percepts, lastAction);

		if (mappedState.checkGoalState()) {
			percepts.clear();
		}

		lastAction = decideOnAction(mappedState);
		return lastAction;
	}

	/**
	 * @param currentState the state we are in
	 * @param lastAction the action performed to reach currentState
	 */
	protected void onNewState(IProblemState currentState, IOperator lastAction)
	{
		percepts.add(currentState);
	}

	public abstract IOperator decideOnAction(IProblemState currentState);

	protected float utilityFunction(float utility, int count)
	{
		// greedy version
		if (count >= explorativeness) {
			return utility;
		}

		// explorative version
		// in case of too few exploration we assume each unknown state is
		// beautiful with some scatter
		float explorative = (explorativeness - count) * (float) Math.random();
		return utility + explorative;
	}
}
