/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.parameter;

import java.util.Map;

/**
 *
 * @author kdorer
 */
public interface IParameterList {
	Map<String, Parameter> getParameters();

	float get(String key);

	void put(String key, float value);

	/**
	 * @return a String version of the params that is easy to paste into code
	 */
	String getParamsString();
}