/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.parameter;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for sets of parameters used to modify Behaviors
 * @author Klaus Dorer
 */
@SuppressWarnings("serial")
public class ParameterList implements Serializable, IParameterList
{
	/** parameters used by the underlying behavior */
	protected Map<String, Parameter> parameters;

	public ParameterList()
	{
		parameters = new LinkedHashMap<>();
	}

	@Override
	public Map<String, Parameter> getParameters()
	{
		return Collections.unmodifiableMap(parameters);
	}

	final public float get(String key)
	{
		if (!parameters.containsKey(key)) {
			return 0;
		}
		return parameters.get(key).getValue();
	}

	final public void put(String key, float value)
	{
		if (!parameters.containsKey(key)) {
			parameters.put(key, new Parameter(key));
		}
		parameters.get(key).setValue(value);
	}

	@Override
	public String toString()
	{
		return parameters.toString();
	}

	@Override
	public String getParamsString()
	{
		StringBuilder builder = new StringBuilder(100);
		for (String key : parameters.keySet()) {
			builder.append("\nput(").append(getParamCode(key));
			builder.append(", ").append(get(key)).append("f);");
		}
		return builder.toString();
	}

	protected String getParamCode(String key)
	{
		return "Param." + key;
	}
}
