/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/*
 * Lecture symbolic AI, FH-Furtwangen
 * Original Author: Klaus Dorer
 */
package kdo.reinforcement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Represents an q-learning kdo.reinforcement agent. This is an agent that
 * decides on q values learned for each action in each state (Watkins 89).
 *
 * @author KDorer
 */
public class QLearningAgent extends RLAgent implements IQTableAccess
{
	private Map<IProblemState, QMapState> qvalues;

	private boolean setStateUtilities;

	/**
	 * Default Constructor to create the agent
	 *
	 * @param valueUpdateStrategy the strategy to use for value function update
	 */
	public QLearningAgent(IValueFunctionUpdate valueUpdateStrategy, IStateMapper mapper, long explorativeness)
	{
		this(valueUpdateStrategy, mapper, false, explorativeness);
	}

	/**
	 * Default Constructor to create the agent
	 *
	 * @param valueUpdateStrategy the strategy to use for value function update
	 */
	public QLearningAgent(IValueFunctionUpdate valueUpdateStrategy, IStateMapper mapper, boolean setStateUtilities,
			long explorativeness)
	{
		super(valueUpdateStrategy, mapper, explorativeness);
		this.setStateUtilities = setStateUtilities;
		qvalues = new HashMap<>();
		((TDQLearning) valueUpdateStrategy).setTableAccess(this);
	}

	/**
	 * An active agent returns the action that produces highest expected utility
	 * @param currentState the state on which to decide
	 * @return the default strategy action
	 */
	@Override
	public IOperator decideOnAction(IProblemState currentState)
	{
		return getBestAction(currentState);
	}

	@Override
	public float getUtility(IProblemState state, IOperator action)
	{
		QMapState qMapState = getState(state);
		return qMapState.getUtility(action);
	}

	@Override
	public IOperator getBestAction(IProblemState currentState)
	{
		QMapState qMapState = getState(currentState);
		return qMapState.getBestAction(currentState.getOperators()).getOperator();
	}

	@Override
	public float getBestUtility(IProblemState currentState)
	{
		QMapState qMapState = getState(currentState);
		return qMapState.getBestAction(currentState.getOperators()).getUtility();
	}

	@Override
	public void update(IProblemState state, IOperator action, float utility)
	{
		QMapState qMapUpdateState = getState(state);
		qMapUpdateState.update(action, utility);
		if (setStateUtilities) {
			QMapAction bestAction = qMapUpdateState.getBestAction(state.getOperators());
			state.setUtility(bestAction.getUtility());
			state.onExploration();
		}
	}

	private QMapState getState(IProblemState state)
	{
		QMapState qMapUpdateState = qvalues.get(state);
		if (qMapUpdateState == null) {
			qMapUpdateState = new QMapState();
			qvalues.put(state, qMapUpdateState);
		}
		return qMapUpdateState;
	}

	@Override
	public int getExplorationCount(IProblemState state, IOperator action)
	{
		QMapState qMapState = getState(state);
		return qMapState.getCount(action);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append("Q-Learning\n");
		for (IProblemState state : qvalues.keySet()) {
			result.append(state);
			QMapState qMapState = qvalues.get(state);
			result.append(qMapState.toString());
			result.append("\n");
		}
		return result.toString();
	}

	class QMapState
	{
		/** q values for each action in this state */
		private Map<IOperator, QMapAction> actions;

		/**
		 * Transition model for a single state of the domain
		 */
		public QMapState()
		{
			actions = new HashMap<>();
		}

		public QMapAction getBestAction(List<IOperator> allActions)
		{
			// check all possible actions
			QMapAction bestAction = null;
			float bestExpectedUtility = Float.NEGATIVE_INFINITY;
			for (IOperator action : allActions) {
				QMapAction qmAction = getAction(action);
				float utility = utilityFunction(qmAction.getUtility(), qmAction.getCount());
				if (utility > bestExpectedUtility) {
					bestAction = qmAction;
					bestExpectedUtility = utility;
				}
			}
			return bestAction;
		}

		private QMapAction getAction(IOperator action)
		{
			QMapAction qMapAction = actions.get(action);
			if (qMapAction == null) {
				qMapAction = new QMapAction(action);
				actions.put(action, qMapAction);
			}
			return qMapAction;
		}

		public void update(IOperator action, float utility)
		{
			QMapAction qMapAction = getAction(action);
			qMapAction.update(utility);
		}

		/**
		 * @param action the action for which to return the utility
		 * @return the expected utility using learned transition model
		 */
		public float getUtility(IOperator action)
		{
			QMapAction qMapAction = getAction(action);
			float utility = qMapAction.getUtility();
			return utility;
		}

		public int getCount(IOperator action)
		{
			QMapAction qMapAction = getAction(action);
			return qMapAction.getCount();
		}

		@Override
		public String toString()
		{
			StringBuilder result = new StringBuilder();
			for (IOperator action : actions.keySet()) {
				result.append("\n" + action + ": ");
				result.append(actions.get(action));
			}
			return result.toString();
		}
	}

	class QMapAction
	{
		// the number the action has been performed
		int count;

		// the q value of this action in this state
		float utility;

		private IOperator operator;

		/**
		 * Transition model for a single action
		 */
		public QMapAction(IOperator operator)
		{
			this.operator = operator;
			count = 0;
			utility = 0;
		}

		public IOperator getOperator()
		{
			return operator;
		}

		public float getUtility()
		{
			return utility;
		}

		public void update(float utility)
		{
			count++;
			this.utility = utility;
		}

		public int getCount()
		{
			return count;
		}

		@Override
		public String toString()
		{
			return "count: " + count + " util: " + utility;
		}
	}
}
