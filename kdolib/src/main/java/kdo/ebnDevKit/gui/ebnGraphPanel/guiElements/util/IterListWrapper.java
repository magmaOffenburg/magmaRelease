/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util;

import java.util.List;

public class IterListWrapper<T> implements IIterWrapper<T>
{
	private final List<? extends T> list;

	public IterListWrapper(List<? extends T> list)
	{
		this.list = list;
	}

	@Override
	public Iterable<? extends T> getIterable()
	{
		return list;
	}
}
