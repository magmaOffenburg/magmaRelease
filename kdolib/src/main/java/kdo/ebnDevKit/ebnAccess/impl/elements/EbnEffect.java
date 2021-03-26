/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnEffect;

/**
 * represents an effect
 * @author Thomas Rinklin
 */
public class EbnEffect extends EbnProposition implements IEbnEffect
{
	/** probability on which the effect comes true */
	private final double probability;

	/**
	 * constructor
	 * @param ebnPerception reference to the underlying proposition
	 * @param negated if the proposition is negated
	 * @param probability probability on which the effect comes true
	 */
	public EbnEffect(EbnPerception ebnPerception, boolean negated, double probability)
	{
		super(ebnPerception, negated);
		this.probability = probability;
	}

	@Override
	public double getProbability()
	{
		return probability;
	}
}
