/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CommandUtil
{
	/**
	 * Checks if the passed string is part of the output of the passed process.
	 * @param ps the process to get output from
	 * @param text the text that we expect to be part of the output
	 */
	public static boolean checkForOutput(Process ps, String text)
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(ps.getInputStream()));
		String line;
		boolean found = false;
		try {
			line = reader.readLine();
			while (line != null) {
				if (line.contains(text)) {
					found = true;
					break;
				}
				line = reader.readLine();
			}
			reader.close();
			return found;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Process launch(String command, String[] params, File path)
	{
		try {
			return Runtime.getRuntime().exec(command, params, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Process launchAndConsume(String... command) throws IOException
	{
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.redirectOutput(SystemUtil.NULL_FILE);
		builder.redirectError(SystemUtil.NULL_FILE);
		return builder.start();
	}

	public static Process launchAndConsume(List<String> command) throws IOException
	{
		return launchAndConsume(command.toArray(new String[0]));
	}
}
