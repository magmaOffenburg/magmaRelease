/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IPerceptor;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Class for simplifying the handling of perceptors in a map.
 *
 * @author Stefan Glaser
 */
public class PerceptorMap extends HashMap<String, IPerceptor> implements IPerceptorMap
{
	public PerceptorMap()
	{
	}

	public PerceptorMap(IPerceptor perceptor)
	{
		put(perceptor);
	}

	public PerceptorMap(IPerceptor... perceptors)
	{
		Arrays.stream(perceptors).forEach(p -> put(p));
	}

	public PerceptorMap(List<IPerceptor> perceptors)
	{
		perceptors.forEach(p -> put(p));
	}

	@Override
	public void put(IPerceptor perceptor)
	{
		put(perceptor.getName(), perceptor);
	}

	@Override
	public void put(IPerceptorMap map)
	{
		putAll(map);
	}

	@Override
	public <T extends IPerceptor> T get(String name, Class<T> clazz)
	{
		IPerceptor effector = get(name);

		if (clazz.isInstance(effector)) {
			return clazz.cast(effector);
		}

		return null;
	}
}
