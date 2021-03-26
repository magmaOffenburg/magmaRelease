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
 * Performs the update of the value function according to the least mean squares
 * approach described by Widrow and Hoff (1960)
 *
 * @author KDorer
 */
public class LeastMeanSquares implements IValueFunctionUpdate
{
	@Override
	public void update(IProblemState currentState, List<IProblemState> percepts, IOperator lastAction)
	{
		if (currentState.checkGoalState()) {
			float rewardToGo = 0.0f;
			for (int i = percepts.size() - 1; i >= 0; i--) {
				IProblemState state = percepts.get(i);
				// calculate the reward to go
				rewardToGo += state.getReinforcement();

				// calculate the running average
				state.onExploration();
				int count = state.getExplorationCount();
				state.setUtility((state.getUtility() * (count - 1) + rewardToGo) / count);
			}
		}
	}
}
