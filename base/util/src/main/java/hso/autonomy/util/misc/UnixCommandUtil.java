/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * A collection of command line utilities for direct unix commands.
 * @author kdorer
 */
public class UnixCommandUtil
{
	/**
	 * Is {@code true} if this is running on a Unix-like operating system.
	 * (Assumption: every operating system except for Windows is Unix-like.)
	 */
	private static boolean IS_UNIX_OS = !System.getProperty("os.name").startsWith("Windows");

	/**
	 * Retrieves the pid of a unix process. (Since Java 9, this is also possible with {@link java.lang.Process#pid()}.)
	 * @param process the process object to get the pid from
	 * @return the pid, -1 if not able to get it
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static int getPID(Process process)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		if (IS_UNIX_OS) {
			/* get the PID on unix/linux systems */
			Field f = process.getClass().getDeclaredField("pid");
			f.setAccessible(true);
			return f.getInt(process);
		} else {
			return -1;
		}
	}

	/**
	 * Checks if the process with pid passed contains the passed name when run
	 * with ps. If so it is killed.
	 * @param pid the pid of the process to kill.
	 * @param name the name that has to appear when calling ps
	 * @return true if the process was killed.
	 */
	public static boolean killProcessConditional(String pid, String name)
	{
		String command = "ps " + pid;
		Process ps;
		try {
			ps = Runtime.getRuntime().exec(command);
			if (CommandUtil.checkForOutput(ps, name)) {
				Runtime.getRuntime().exec("kill -9 " + pid);
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Kills all processes with a given name. Use with care!
	 * @param name the name that has to appear when calling ps
	 */
	public static boolean killAll(String name)
	{
		try {
			Runtime.getRuntime().exec("killall -9 " + name);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
