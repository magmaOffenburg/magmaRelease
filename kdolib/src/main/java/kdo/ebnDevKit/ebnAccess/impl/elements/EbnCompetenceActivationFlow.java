/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebnDevKit.ebnAccess.IEbnActivationFlow.IEbnCompetenceActivationFlow;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;

/**
 * represents activation flow from competence to competence
 * @author Thomas Rinklin
 */
public class EbnCompetenceActivationFlow extends EbnActivationFlow implements IEbnCompetenceActivationFlow
{
	/** link to the source competence */
	private final IEbnCompetence sourceCompetence;

	public EbnCompetenceActivationFlow(IEbnCompetence source, IEbnCompetence target, boolean isExciation)
	{
		super(isExciation, target);

		this.sourceCompetence = source;
	}

	@Override
	public IEbnCompetence getSourceCompetence()
	{
		return sourceCompetence;
	}
}
