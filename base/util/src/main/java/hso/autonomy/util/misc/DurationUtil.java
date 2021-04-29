/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

import java.time.Duration;

public class DurationUtil
{
	/** Formats a Duration in HH:MM:SS format */
	public static String format(Duration duration)
	{
		long seconds = duration.getSeconds();
		long absSeconds = Math.abs(seconds);
		return String.format("%02d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
	}

	/** Parses a HH:MM:SS-formatted String to a Duration */
	public static Duration parse(String s)
	{
		String[] components = s.split(":");
		int hours = Integer.parseInt(components[0]);
		int minutes = Integer.parseInt(components[1]);
		int seconds = Integer.parseInt(components[2]);
		return Duration.ofSeconds(hours * 3600 + minutes * 60 + seconds);
	}
}
