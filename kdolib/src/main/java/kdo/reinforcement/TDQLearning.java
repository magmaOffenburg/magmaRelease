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
 * Performs the update of the q function according to the temporal difference
 * approach.
 *
 * @author KDorer
 */
public class TDQLearning extends TemporalDifference implements IValueFunctionUpdate
{
	private IQTableAccess qTable;

	public TDQLearning()
	{
		super();
	}

	public TDQLearning(ILearnrateStrategy learnrateStrategy)
	{
		super(learnrateStrategy);
	}

	public void setTableAccess(IQTableAccess qTable)
	{
		this.qTable = qTable;
	}

	@Override
	public void update(IProblemState currentState, List<IProblemState> percepts, IOperator lastAction)
	{
		if (currentState.checkGoalState()) {
			// for goal states we can simply calculate the running average
			float reinforcement = currentState.getReinforcement();
			IOperator bestAction = qTable.getBestAction(currentState);
			float oldUtility = qTable.getUtility(currentState, bestAction);
			float count = qTable.getExplorationCount(currentState, bestAction);
			float newUtility = (oldUtility * count + reinforcement) / (count + 1);
			qTable.update(currentState, bestAction, newUtility);
		}

		if (percepts.size() > 1) {
			// we have a state transition so apply TD update rule
			IProblemState penultimateState = percepts.get(percepts.size() - 2);
			float learnrate = learnrateStrategy.getAlpha(qTable.getExplorationCount(penultimateState, lastAction));

			float oldUtility = qTable.getUtility(penultimateState, lastAction);
			float successorUtility = qTable.getBestUtility(currentState);
			float reinforcement = penultimateState.getReinforcement();
			float newUtility = oldUtility + learnrate * (reinforcement + successorUtility - oldUtility);
			qTable.update(penultimateState, lastAction, newUtility);
		}
	}
}
