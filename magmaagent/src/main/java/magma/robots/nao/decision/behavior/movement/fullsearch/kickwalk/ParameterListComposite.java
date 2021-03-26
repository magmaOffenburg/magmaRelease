/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import kdo.util.parameter.IParameterList;
import kdo.util.parameter.Parameter;

public class ParameterListComposite implements IParameterList
{
	private List<IParameterList> list = new ArrayList<>();

	public static ParameterListComposite fromSingle(IParameterList singleParam)
	{
		ParameterListComposite result = new ParameterListComposite();
		result.add(singleParam);
		return result;
	}

	public void add(IParameterList newList)
	{
		list.add(newList);
	}

	public List<IParameterList> getList()
	{
		return list;
	}

	@Override
	public Map<String, Parameter> getParameters()
	{
		return list.get(0).getParameters();
	}

	@Override
	public float get(String key)
	{
		return 0;
	}

	@Override
	public void put(String key, float value)
	{
	}

	@Override
	public String getParamsString()
	{
		return list.get(0).getParamsString();
	}

	@Override
	public String toString()
	{
		return list.toString();
	}
}
