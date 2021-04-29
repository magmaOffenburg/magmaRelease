/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.reinforcement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Represents an active kdo.reinforcement agent. This is an agent that decides
 * on its actions in order to maximize its long term utility.
 *
 * @author KDorer
 */
/**
 * @author dorer
 *
 */
public class ActiveRLAgent extends RLAgent
{
	/**
	 * the model of the world as transition probabilities (to be learned) for
	 * each state
	 */
	private Map<IProblemState, TransitionModel> model;

	/**
	 * Default Constructor to create the agent
	 *
	 * @param vlaueUpdateStrategy the strategy to use for value function update
	 */
	public ActiveRLAgent(IValueFunctionUpdate vlaueUpdateStrategy, IStateMapper mapper, long explorativeness)
	{
		super(vlaueUpdateStrategy, mapper, explorativeness);
		model = new HashMap<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kdo.reinforcement.RLAgent#onNewState(kdo.domain.IProblemState,
	 * kdo.domain.IOperator)
	 */
	@Override
	protected void onNewState(IProblemState currentState, IOperator lastAction)
	{
		super.onNewState(currentState, lastAction);

		if (percepts.size() > 1) {
			IProblemState previousState = percepts.get(percepts.size() - 2);
			TransitionModel transitionModel = model.get(previousState);
			if (transitionModel == null) {
				transitionModel = new TransitionModel();
				model.put(previousState, transitionModel);
			}

			transitionModel.update(lastAction, currentState);
		}
	}

	/**
	 * An active agent returns the action that produces highest expected utility
	 * @param currentState the state on which to decide
	 * @return the default strategy action
	 */
	@Override
	public IOperator decideOnAction(IProblemState currentState)
	{
		// learn model
		TransitionModel transitionModel = model.get(currentState);
		if (transitionModel == null) {
			transitionModel = new TransitionModel();
			model.put(currentState, transitionModel);
		}

		// determine best action
		List<IOperator> actions = currentState.getOperators();
		float bestExpectedUtility = Float.NEGATIVE_INFINITY;
		IOperator bestAction = actions.get(0);
		for (IOperator action : actions) {
			// check all possible successor states
			float expectedUtility = transitionModel.getExpectedUtility(action);

			if (expectedUtility > bestExpectedUtility) {
				bestAction = action;
				bestExpectedUtility = expectedUtility;
			}
		}
		return bestAction;
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append("Strategy\n");
		result.append(toStrategyString());
		result.append("\nModel\n");
		result.append(toModelString());
		return result.toString();
	}

	public String toStrategyString()
	{
		StringBuilder result = new StringBuilder();
		for (IProblemState state : model.keySet()) {
			result.append(state);
			result.append(" do: ");
			TransitionModel transmodel = model.get(state);
			result.append(transmodel.bestActionString());
			result.append("\n");
		}
		return result.toString();
	}

	public String toModelString()
	{
		StringBuilder result = new StringBuilder();
		for (IProblemState state : model.keySet()) {
			result.append(state);
			result.append(" do: ");
			result.append(getAllActionsString(state));
			result.append("\n");
		}
		return result.toString();
	}

	public String getAllActionsString(IProblemState state)
	{
		return model.get(state).allActionsString();
	}

	class TransitionModel
	{
		/** model for each action of this state */
		private Map<IOperator, TransitionModelAction> actions;

		/**
		 * Transition model for a single state of the domain
		 */
		public TransitionModel()
		{
			actions = new HashMap<>();
		}

		/**
		 * Returns the (learned) probability to get from this state using passed
		 * action to the new state
		 * @param action the action planned to take
		 * @param destinationState the state to be reached
		 * @return probability to get from this state using passed action to the
		 *         new state
		 */
		public float getProbability(IOperator action, IProblemState destinationState)
		{
			if (actions.containsKey(action)) {
				return actions.get(action).getProbability(destinationState);

			} else {
				// action is not known yet
				return 0.0f;
			}
		}

		/**
		 * Sets the new transition probability for passed action and passed
		 * destination state
		 * @param action the action performed
		 * @param destinationState the state reached
		 */
		public void update(IOperator action, IProblemState destinationState)
		{
			if (!actions.containsKey(action)) {
				actions.put(action, new TransitionModelAction());
			}

			actions.get(action).update(destinationState);
		}

		/**
		 * @return the known action with highest expected utility
		 */
		public IOperator getBestAction()
		{
			float bestUtility = Float.NEGATIVE_INFINITY;
			IOperator bestAction = null;
			for (IOperator action : actions.keySet()) {
				float expectedUtility = getExpectedUtility(action);
				if (expectedUtility > bestUtility) {
					bestUtility = expectedUtility;
					bestAction = action;
				}
			}
			return bestAction;
		}

		/**
		 * @param action the action for which to return the utility
		 * @return the expected utility using learned transition model
		 */
		public float getExpectedUtility(IOperator action)
		{
			Collection<IProblemState> successorStates = getSuccessorStates(action);
			if (successorStates.isEmpty()) {
				// we do not know any successor states of this action, so we return
				// a high utility with some random seed to try different actions
				return 3.0f + (float) Math.random();
			}

			float expectedUtility = 0.0f;
			for (IProblemState destinationState : successorStates) {
				expectedUtility +=
						getProbability(action, destinationState) *
						utilityFunction(destinationState.getUtility(), destinationState.getExplorationCount());
			}
			return expectedUtility;
		}

		public Collection<IProblemState> getSuccessorStates(IOperator action)
		{
			if (!actions.containsKey(action)) {
				return Collections.emptyList();
			} else {
				return actions.get(action).getSuccessorStates();
			}
		}

		public String bestActionString()
		{
			StringBuilder result = new StringBuilder();
			IOperator bestAction = getBestAction();
			result.append(bestAction + ": ");
			result.append(actions.get(bestAction));
			return result.toString();
		}

		public String allActionsString()
		{
			StringBuilder result = new StringBuilder();
			for (IOperator action : actions.keySet()) {
				result.append(action);
				result.append(actions.get(action) + "\n");
			}
			return result.toString();
		}
	}

	class TransitionModelAction
	{
		// the number the action has been performed
		int count;

		// the observed transition probabilities
		Map<IProblemState, Integer> visitCounts;

		/**
		 * Transition model for a single action
		 */
		public TransitionModelAction()
		{
			count = 0;
			visitCounts = new HashMap<>();
		}

		public int getCount()
		{
			return count;
		}

		/**
		 * @return the know set of successor states
		 */
		public Collection<IProblemState> getSuccessorStates()
		{
			return visitCounts.keySet();
		}

		/**
		 * Updates the relative count of the passed states visits for this action
		 * @param destinationState
		 */
		public void update(IProblemState destinationState)
		{
			count++;
			int visits = 1;
			if (visitCounts.containsKey(destinationState)) {
				visits = visitCounts.get(destinationState).intValue() + 1;
			}
			visitCounts.put(destinationState, Integer.valueOf(visits));
		}

		public float getProbability(IProblemState destinationState)
		{
			if (visitCounts.containsKey(destinationState)) {
				return visitCounts.get(destinationState).intValue() / (float) count;
			} else {
				// no probability stored for this destination state
				return 0.0f;
			}
		}

		@Override
		public String toString()
		{
			StringBuilder result = new StringBuilder();
			result.append(String.format("(%d): ", getCount()));
			for (IProblemState state : visitCounts.keySet()) {
				result.append(state.getName());
				result.append(String.format(" %5.2f ", getProbability(state)));
			}
			return result.toString();
		}
	}
}
