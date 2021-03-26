/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import java.io.File;

public class SystemUtil
{
	public static final File NULL_FILE = new File(isWindows() ? "NUL" : "/dev/null");

	public static boolean isWindows()
	{
		return System.getProperty("os.name").toLowerCase().contains("win");
	}
}
