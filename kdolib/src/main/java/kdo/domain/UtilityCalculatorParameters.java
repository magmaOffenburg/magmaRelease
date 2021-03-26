/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain;

public class UtilityCalculatorParameters
{
	/** the id of this instance which is only unique over average out runs */
	private int instanceID;

	public UtilityCalculatorParameters(int instanceID)
	{
		this.instanceID = instanceID;
	}

	public int getInstanceID()
	{
		return instanceID;
	}
}
