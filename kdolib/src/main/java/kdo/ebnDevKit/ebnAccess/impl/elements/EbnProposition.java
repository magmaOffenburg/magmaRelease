/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;

/**
 * represents a proposition
 * @author Thomas Rinklin
 */
public class EbnProposition implements IEbnProposition
{
	/** link to the underlying perception */
	private final EbnPerception perception;

	/** if the proposition is negated */
	private final boolean isNegated;

	/**
	 * constructor
	 * @param perception link to the underlying perception
	 * @param isNeagated if the proposition is negated
	 */
	public EbnProposition(EbnPerception perception, boolean isNeagated)
	{
		this.isNegated = isNeagated;
		this.perception = perception;
	}

	@Override
	public EbnPerception getPerception()
	{
		return perception;
	}

	@Override
	public boolean isNegated()
	{
		return isNegated;
	}
}
