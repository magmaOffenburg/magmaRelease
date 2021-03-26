/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.ebnAccess.impl.elements;

import kdo.ebnDevKit.ebnAccess.IEbnResource;
import kdo.ebnDevKit.ebnAccess.IEbnResource.IEbnResourceProposition;

/**
 * represents a resource proposition
 * @author Thomas Rinklin
 */
public class EbnResourceProposition implements IEbnResourceProposition
{
	/** amount of resources which are used from this proposition */
	private final int amountUsed;

	/** link to the resource */
	private final EbnResource resource;

	/**
	 *
	 * @param amountUsed amount of resources which are used from this proposition
	 * @param resource link to the resource
	 */
	public EbnResourceProposition(int amountUsed, EbnResource resource)
	{
		super();
		this.amountUsed = amountUsed;
		this.resource = resource;
	}

	@Override
	public boolean isResource(IEbnResource resource)
	{
		return (this.resource == resource);
	}

	@Override
	public IEbnResource getResource()
	{
		return resource;
	}

	@Override
	public int getAmountUsed()
	{
		return amountUsed;
	}
}
