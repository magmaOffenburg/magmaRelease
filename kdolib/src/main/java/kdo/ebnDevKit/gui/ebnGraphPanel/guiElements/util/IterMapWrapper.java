/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util;

import java.util.Map;

public class IterMapWrapper<T> implements IIterWrapper<T>
{
	private final Map<?, ? extends T> map;

	public IterMapWrapper(Map<?, ? extends T> map)
	{
		this.map = map;
	}

	@Override
	public Iterable<? extends T> getIterable()
	{
		return map.values();
	}
}
