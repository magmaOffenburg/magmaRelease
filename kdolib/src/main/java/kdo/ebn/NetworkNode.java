/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Base class of all network nodes
 */
abstract class NetworkNode
{
	/** the name of the node */
	private String name;

	/**
	 * Constructor
	 *
	 * @param name Node name
	 */
	NetworkNode(String name)
	{
		this.name = name;
	}

	/**
	 * Get node name
	 *
	 * @return Name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Set node name
	 *
	 * @param name New name
	 */
	void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Default implementation does nothing
	 */
	public void perform()
	{
	}

	/**
	 * Returns the activation of this network node
	 *
	 * @return Activation value
	 */
	public abstract double getActivation();

	/**
	 * Serialize this object into a string (with indentation). The default
	 * implementation just returns the node name.
	 *
	 * @param tabs Tab width
	 * @return Serialized representation as string
	 */
	public String toString(int tabs)
	{
		return getName();
	}
}
