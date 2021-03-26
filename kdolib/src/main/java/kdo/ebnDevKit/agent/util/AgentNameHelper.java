/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.agent.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper Class to generate unique names
 *
 * @author Thomas Rinklin
 *
 */
public class AgentNameHelper
{
	private final Map<String, Integer> nameCounts = new HashMap<String, Integer>();

	/**
	 * generates unique names by adding an increasing number to the end of a
	 * string.
	 * @param agentId the string to make unique
	 * @return a string containing the passed agentId plus an increasing number
	 *         in brackets
	 */
	public String getName(String agentId)
	{
		Integer count = nameCounts.get(agentId);
		if (count == null) {
			count = 1;
		} else {
			count++;
		}
		nameCounts.put(agentId, count);

		return agentId + "(" + count + ")";
	}
}
