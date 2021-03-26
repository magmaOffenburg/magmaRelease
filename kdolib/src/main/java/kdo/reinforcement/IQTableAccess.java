/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package kdo.reinforcement;

import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * @author kdorer
 *
 */
public interface IQTableAccess {
	int getExplorationCount(IProblemState penultimateState, IOperator lastAction);

	void update(IProblemState penultimateState, IOperator lastAction, float newUtility);

	float getBestUtility(IProblemState currentState);

	float getUtility(IProblemState penultimateState, IOperator lastAction);

	IOperator getBestAction(IProblemState currentState);
}
