/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.decision.behavior.basic;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.agentmodel.IAgentModel;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.worldmodel.IWorldModel;
import java.io.Serializable;

/**
 * Abstract base class for all "simple" behaviors.
 *
 * @author Klaus Dorer
 */
public abstract class Behavior implements IBehavior, Serializable
{
	/** the name of this behavior */
	protected String name;

	/** link to the thought model */
	private transient final IThoughtModel thoughtModel;

	/** number of times this behavior was performed since creation */
	private int performs;

	private int performedCycles;

	private int consecutivePerforms;

	public Behavior(String name, IThoughtModel thoughtModel)
	{
		this.name = name;
		this.thoughtModel = thoughtModel;

		performs = 0;
		performedCycles = 0;
		consecutivePerforms = 0;
	}

	public IThoughtModel getThoughtModel()
	{
		return thoughtModel;
	}

	public IWorldModel getWorldModel()
	{
		return thoughtModel.getWorldModel();
	}

	public IAgentModel getAgentModel()
	{
		return thoughtModel.getAgentModel();
	}

	@Override
	public int getPerforms()
	{
		return performs;
	}

	@Override
	public int getPerformedCycles()
	{
		return performedCycles;
	}

	@Override
	public int getConsecutivePerforms()
	{
		return consecutivePerforms;
	}

	@Override
	public final String getName()
	{
		return name;
	}

	@Override
	public void init()
	{
		performs++;
		consecutivePerforms = 1;
	}

	@Override
	public void perform()
	{
		performedCycles++;
	}

	@Override
	public boolean isFinished()
	{
		return true;
	}

	@Override
	public void abort()
	{
		init();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
			return this;
		}

		return actualBehavior;
	}

	@Override
	public void stayIn()
	{
		if (isFinished()) {
			consecutivePerforms++;
		}
	}

	@Override
	public void onLeavingBehavior(IBehavior newBehavior)
	{
		// just to make sure to not leave the new behavior
		if (newBehavior != this) {
			init();
		}
	}

	@Override
	public void preDecisionUpdate()
	{
	}

	/**
	 * @return the default is to return the behavior itself
	 */
	@Override
	public IBehavior getRootBehavior()
	{
		return this;
	}

	@Override
	public String toString()
	{
		return getName();
	}
}
