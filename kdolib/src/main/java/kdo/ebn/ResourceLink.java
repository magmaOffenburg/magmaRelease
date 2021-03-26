/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Represents a link from a resource module to a competence module
 */
public class ResourceLink extends Connection
{
	/** the source resource node */
	private final Resource sourceNode;

	/**
	 * Calls parent constructor
	 * @param params the network parameters that define the runtime dynamics of
	 *        the network
	 * @param source the source resource node of the resource
	 * @param destination the destination resource proposition of the competence
	 *        module
	 */
	public ResourceLink(NetworkParams params, Resource source, Proposition destination)
	{
		super(params, destination, destination);
		sourceNode = source;
	}

	@Override
	public NetworkNode getSourceModule()
	{
		return sourceNode;
	}

	@Override
	public Proposition getSourceProposition()
	{
		throw new RuntimeException("getSourceProposition should never be called for a ResourceLink");
	}
}
