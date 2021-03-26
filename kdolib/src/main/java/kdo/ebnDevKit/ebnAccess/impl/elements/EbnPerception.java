/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebn.IEBNPerception;
import kdo.ebn.PerceptionNode;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;

/**
 * represents a perception
 * @author Thomas Rinklin
 */
public class EbnPerception implements IEbnPerception
{
	private final PerceptionNode perceptionNode;

	public EbnPerception(PerceptionNode perc)
	{
		perceptionNode = perc;
	}

	@Override
	public String getName()
	{
		return perceptionNode.getName();
	}

	@Override
	public double getActivation()
	{
		return perceptionNode.getActivation();
	}

	@Override
	public double getTruthValue()
	{
		return perceptionNode.getTruthValue();
	}

	/**
	 * @return perception link
	 */
	public PerceptionNode getPerception()
	{
		return perceptionNode;
	}

	@Override
	public EbnProposition createProposition(boolean bVal)
	{
		return new EbnProposition(this, bVal);
	}

	@Override
	public EbnEffect createEffect(boolean negated, double probability)
	{
		return new EbnEffect(this, negated, probability);
	}

	@Override
	public boolean isBelief(IEBNPerception iBelief)
	{
		return perceptionNode.getBeliefLink() == iBelief;
	}
}
