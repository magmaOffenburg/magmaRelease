/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kdo.ebn.Goal;
import kdo.ebnDevKit.ebnAccess.IEbnGoal;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;

/**
 * represents a goal
 * @author Thomas Rinklin
 */
public class EbnGoal implements IEbnGoal
{
	/** link to the goal */
	private final Goal goal;

	/** goal condition */
	private final IEbnProposition goalCondition;

	/** relevance propositions */
	private final List<EbnProposition> ebnRelevanceConditions;

	/**
	 * create a ebnGoal object
	 * @param goal the existing goal
	 * @param ebnProposition underlaying proposition
	 */
	public EbnGoal(Goal goal, IEbnProposition ebnProposition)
	{
		this.goalCondition = ebnProposition;
		this.goal = goal;
		// this.ebnAccess = ebnAccess;
		this.ebnRelevanceConditions = new ArrayList<EbnProposition>();
	}

	@Override
	public String getName()
	{
		return goal.getName();
	}

	@Override
	public int getIndex()
	{
		return goal.getIndex();
	}

	@Override
	public double getActivation()
	{
		return goal.getActivation();
	}

	@Override
	public double getImportance()
	{
		return goal.getImportance();
	}

	@Override
	public double getRelevance()
	{
		return goal.getRelevance();
	}

	@Override
	public double getGoalConditionTruthValue()
	{
		return goal.getGoalCondition().getTruthValue();
	}

	@Override
	public IEbnProposition getGoalCondition()
	{
		return goalCondition;
	}

	@Override
	public Iterator<? extends IEbnProposition> getRelevanceConditions()
	{
		return ebnRelevanceConditions.iterator();
	}

	/**
	 * returns goal link
	 * @return goal link
	 */
	public Goal getGoal()
	{
		return goal;
	}

	/**
	 * adds proposition
	 * @param ebnProp proposition to add
	 */
	public void addProposition(EbnProposition ebnProp)
	{
		ebnRelevanceConditions.add(ebnProp);
	}

	@Override
	public boolean isPerceptionUsed(IEbnPerception perception)
	{
		if (goalCondition.getPerception() == perception)
			return true;

		for (IEbnProposition prop : ebnRelevanceConditions) {
			if (prop.getPerception() == perception)
				return true;
		}
		return false;
	}
}
