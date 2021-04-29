/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.learningParameter;

import java.util.HashMap;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.ParameterList;
import kdo.util.parameter.ParameterMap;

/**
 * @author kdorer
 */
public class LearningParameterMap
{
	private ILearningParameterList parameter;

	private String[] mapNames;

	public LearningParameterMap(String mapName, ILearningParameterList parameter)
	{
		this.parameter = parameter;
		this.mapNames = new String[1];
		mapNames[0] = mapName;
	}

	/**
	 * Use this constructor, if the parameter set should be accessed by multiple
	 * instances (names).
	 * @param names array of names under which the parameters should be
	 *        accessible.
	 */
	public LearningParameterMap(String[] names, ILearningParameterList parameter)
	{
		this.parameter = parameter;
		this.mapNames = names;
	}

	/**
	 * Creates an empty dummy map.
	 */
	public LearningParameterMap()
	{
		parameter = new LearningParameterList(new ParameterList(), new HashMap<>());
		mapNames = new String[0];
	}

	public ILearningParameterList getParameter()
	{
		return parameter;
	}

	public IParameterList getDecoratedParameter()
	{
		return parameter.getDecoratedList();
	}

	/**
	 * Creates a parameter map from the information in this class.
	 * @return the corresponding ParameterMap instance
	 */
	public ParameterMap getParameterMap()
	{
		ParameterMap map = new ParameterMap();
		for (String name : mapNames) {
			map.put(name, getDecoratedParameter());
		}
		return map;
	}
}
