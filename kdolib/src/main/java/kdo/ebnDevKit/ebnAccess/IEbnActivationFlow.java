/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess;

/**
 * interface to an activation flow
 * @author Thomas Rinklin
 */
public interface IEbnActivationFlow {
	/**
	 * @return excitation (true) or inhibition (false)
	 */
	boolean isExcitation();

	IEbnCompetence getTargetCompetence();

	/**
	 * interface to an activation flow between competences
	 * @author Thomas Rinklin
	 */
	interface IEbnCompetenceActivationFlow extends IEbnActivationFlow {
		IEbnCompetence getSourceCompetence();
	}

	/**
	 * interface to an activation flow from a goal to a competence
	 * @author Thomas Rinklin
	 */
	interface IEbnGoalActivationFlow extends IEbnActivationFlow {
		IEbnGoal getSourceGoal();
	}
}