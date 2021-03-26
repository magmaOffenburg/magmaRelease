/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Utility to print Strings indented by tabs
 */
public class PrintUtil
{
	/**
	 * Creates and returns string containing new line character and specified
	 * number of tabs to provide indentation
	 *
	 * @param tabs number of tabs required
	 * @return string containing new line character and the required number of
	 *         tabs
	 */
	public static String addTabs(int tabs)
	{
		StringBuffer tabBuffer = new StringBuffer((3 * tabs) + 2);

		tabBuffer.append("\n");

		for (int i = 0; i < tabs; i++) {
			tabBuffer.append("   ");
		}

		return tabBuffer.toString();
	}
}
