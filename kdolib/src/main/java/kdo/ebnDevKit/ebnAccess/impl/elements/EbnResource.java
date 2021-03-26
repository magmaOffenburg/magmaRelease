/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebn.IResourceBelief;
import kdo.ebn.Resource;
import kdo.ebnDevKit.ebnAccess.IEbnResource;

/**
 * represents a resource
 * @author Thomas Rinklin
 */
public class EbnResource implements IEbnResource
{
	/** link to the resource */
	private final Resource resource;

	/**
	 * constructor
	 * @param resource link to the resource
	 */
	public EbnResource(Resource resource)
	{
		this.resource = resource;
	}

	@Override
	public boolean isResource(IResourceBelief iResourceBelief)
	{
		return (resource.getBeliefLink() == iResourceBelief);
	}

	@Override
	public String getName()
	{
		return resource.getName();
	}

	@Override
	public int getAmountAvailable()
	{
		return resource.getAmountAvailable();
	}

	@Override
	public int getMaximalAmount()
	{
		return resource.getMaximalAmount();
	}

	/**
	 * returns the resource
	 * @return the resource
	 */
	public Resource getResource()
	{
		return resource;
	}

	@Override
	public EbnResourceProposition creatProposition(int amountUsed)
	{
		return new EbnResourceProposition(amountUsed, this);
	}
}
