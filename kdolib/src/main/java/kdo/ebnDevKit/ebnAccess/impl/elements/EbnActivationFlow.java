/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;

/**
 * represents an activation flow
 * @author Thomas Rinklin
 */
public abstract class EbnActivationFlow implements IEbnActivationFlow
{
	/** excitation or inhibition */
	protected final boolean isExcitation;

	/** target competence */
	private final IEbnCompetence targetCompetence;

	/**
	 * constructor for the activation flow
	 * @param isExcitation excitation (true) or inhibition (false)
	 * @param comp target competence
	 */
	public EbnActivationFlow(boolean isExcitation, IEbnCompetence comp)
	{
		this.isExcitation = isExcitation;
		this.targetCompetence = comp;
	}

	@Override
	public boolean isExcitation()
	{
		return isExcitation;
	}

	@Override
	public IEbnCompetence getTargetCompetence()
	{
		return targetCompetence;
	}
}
