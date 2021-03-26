/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.IEffector;
import hso.autonomy.agent.communication.action.IEffectorMap;
import java.util.HashMap;

/**
 * Class for simplifying the handling of effectors in a map.
 *
 * @author Stefan Glaser
 */
public class EffectorMap extends HashMap<String, IEffector> implements IEffectorMap
{
	public EffectorMap()
	{
	}

	public EffectorMap(IEffector effector)
	{
		put(effector);
	}

	@Override
	public void put(IEffector effector)
	{
		put(effector.getName(), effector);
	}

	@Override
	public void put(IEffectorMap map)
	{
		putAll(map);
	}

	@Override
	public <T extends IEffector> T get(String name, Class<T> clazz)
	{
		IEffector effector = get(name);

		if (clazz.isInstance(effector)) {
			return clazz.cast(effector);
		}

		return null;
	}
}
