/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnGoalActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;

/**
 * represents the activation flow from goal to competence
 * @author Thomas Rinklin
 *
 */
public class EbnGoalActivationFlow extends EbnActivationFlow implements IEbnGoalActivationFlow
{
	/** link to the source goal */
	private final IEbnGoal sourceGoal;

	/**
	 * constructor
	 * @param ebnGoal link to the source goal
	 * @param target link to the target competence
	 * @param isExciation true if the link is excitation, false if inhibition
	 */
	public EbnGoalActivationFlow(IEbnGoal ebnGoal, IEbnCompetence target, boolean isExciation)
	{
		super(isExciation, target);

		sourceGoal = ebnGoal;
	}

	@Override
	public IEbnGoal getSourceGoal()
	{
		return sourceGoal;
	}
}
