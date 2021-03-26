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
 * Performs the update of the value function according to the temporal
 * difference approach.
 *
 * @author KDorer
 */
public class TDValueLearning extends TemporalDifference implements IValueFunctionUpdate
{
	public TDValueLearning()
	{
		super();
	}

	public TDValueLearning(ILearnrateStrategy learnrateStrategy)
	{
		super(learnrateStrategy);
	}

	@Override
	public void update(IProblemState currentState, List<IProblemState> percepts, IOperator lastAction)
	{
		if (currentState.checkGoalState()) {
			// for goal states we can simply calculate the running average
			currentState.onExploration();
			int count = currentState.getExplorationCount();
			currentState.setUtility(
					(currentState.getUtility() * (count - 1) + currentState.getReinforcement()) / count);
		}

		if (percepts.size() > 1) {
			// we have a state transition so apply TD update rule
			IProblemState penultimateState = percepts.get(percepts.size() - 2);
			penultimateState.onExploration();
			float learnrate = learnrateStrategy.getAlpha(penultimateState.getExplorationCount());
			float utility = penultimateState.getUtility();
			float reinforcement = penultimateState.getReinforcement();
			// td update rule
			penultimateState.setUtility(utility + learnrate * (reinforcement + currentState.getUtility() - utility));
		}
	}
}
