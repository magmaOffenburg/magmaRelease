/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.misc;

import java.util.Arrays;

/**
 * Some general util methods for enums
 *
 * @author kdorer
 */
public class EnumUtil
{
	/**
	 * @param e the class of the enum to use
	 * @return all values in the enum as String array
	 */
	public static String[] getNames(Class<? extends Enum<?>> e)
	{
		return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[] ::new);
	}
}
