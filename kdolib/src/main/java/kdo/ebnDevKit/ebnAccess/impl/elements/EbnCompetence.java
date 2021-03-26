/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kdo.ebn.Competence;
import kdo.ebn.IEBNAction;
import kdo.ebnDevKit.ebnAccess.IEbnCompetence;
import kdo.ebnDevKit.ebnAccess.IEbnPerception;
import kdo.ebnDevKit.ebnAccess.IEbnPerception.IEbnProposition;
import kdo.ebnDevKit.ebnAccess.IEbnResource;

/**
 * represents a competence
 * @author Thomas Rinklin
 */
public class EbnCompetence implements IEbnCompetence
{
	/** link to the competence */
	private final Competence competence;

	/** precondition propositions */
	private final List<EbnProposition> ebnPreconditions;

	/** effects */
	private final List<EbnEffect> ebnEffects;

	/** list of resources */
	private final List<EbnResourceProposition> ebnResources;

	/**
	 * constructor
	 * @param comp link to the competence
	 */
	public EbnCompetence(Competence comp)
	{
		competence = comp;

		ebnPreconditions = new ArrayList<EbnProposition>();
		ebnEffects = new ArrayList<EbnEffect>();
		ebnResources = new ArrayList<EbnResourceProposition>();
	}

	@Override
	public String getName()
	{
		return competence.getName();
	}

	@Override
	public double getActivation()
	{
		return competence.getActivation();
	}

	@Override
	public double getExecutability()
	{
		return competence.getExecutability();
	}

	@Override
	public double getActivationAndExecutability()
	{
		return competence.getActivationAndExecutability();
	}

	@Override
	public double getMainActivation()
	{
		return competence.getMainActivation();
	}

	/**
	 * Returns competence link
	 *
	 * @return Competence link
	 */
	public Competence getCompetence()
	{
		return competence;
	}

	@Override
	public Iterator<EbnProposition> getPreconditions()
	{
		return ebnPreconditions.iterator();
	}

	public void addPrecondition(EbnProposition precond)
	{
		ebnPreconditions.add(precond);
	}

	@Override
	public Iterator<EbnEffect> getEffects()
	{
		return ebnEffects.iterator();
	}

	public void addEffect(EbnEffect ebnEffect)
	{
		ebnEffects.add(ebnEffect);
	}

	@Override
	public Iterator<EbnResourceProposition> getResources()
	{
		return ebnResources.iterator();
	}

	public void addResource(EbnResourceProposition ebnResource)
	{
		ebnResources.add(ebnResource);
	}

	@Override
	public boolean perceptionIsUsed(IEbnPerception perception)
	{
		for (IEbnProposition prop : ebnPreconditions) {
			if (prop.getPerception() == perception)
				return true;
		}

		for (EbnEffect eff : ebnEffects) {
			if (eff.getPerception() == perception)
				return true;
		}
		return false;
	}

	@Override
	public boolean isResourceUsed(IEbnResource resource)

	{
		for (EbnResourceProposition ebnResProp : ebnResources) {
			if (ebnResProp.isResource(resource))
				return true;
		}
		return false;
	}

	@Override
	public Iterator<IEBNAction> getActions()
	{
		return competence.getActions();
	}
}
