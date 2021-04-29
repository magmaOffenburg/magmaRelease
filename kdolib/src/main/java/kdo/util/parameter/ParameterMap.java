/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple Map wrapper for specific IBehaviorParameterMap
 * @author kdorer
 */
public class ParameterMap
{
	private Map<String, IParameterList> parameterMap;

	public ParameterMap()
	{
		parameterMap = new HashMap<>(20);
	}

	public void put(String key, IParameterList value)
	{
		parameterMap.put(key, value);
	}

	public void putAll(ParameterMap other)
	{
		parameterMap.putAll(other.parameterMap);
	}

	public IParameterList get(String key)
	{
		return parameterMap.get(key);
	}

	public IParameterList getFirst()
	{
		if (parameterMap.isEmpty()) {
			return null;
		}
		return parameterMap.values().iterator().next();
	}

	public boolean contains(String key)
	{
		return parameterMap.containsKey(key);
	}
}
