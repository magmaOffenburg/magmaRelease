/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

import java.util.Iterator;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;

/**
 * interface to a goal
 * @author Thomas Rinklin
 */
public interface IEbnGoal {
	String getName();

	int getIndex();

	double getActivation();

	/**
	 * returns situation independent importance
	 * @return situation independent importance
	 */
	double getImportance();

	double getRelevance();

	double getGoalConditionTruthValue();

	IEbnProposition getGoalCondition();

	Iterator<? extends IEbnProposition> getRelevanceConditions();

	/**
	 * checks if the perception is used by the goal
	 * @param perception perception to check
	 * @return true if it's used, false if not
	 */
	boolean isPerceptionUsed(IEbnPerception perception);
}