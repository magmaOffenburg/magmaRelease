/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Exception thrown if network can not be setup
 */
public class NetworkConfigurationException extends Exception
{
	/** default SID */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 *
	 * @param msg Error message
	 */
	public NetworkConfigurationException(String msg)
	{
		super(msg);
	}
}
