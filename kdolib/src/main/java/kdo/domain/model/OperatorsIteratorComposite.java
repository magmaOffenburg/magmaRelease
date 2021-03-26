/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import kdo.domain.IOperator;

/**
 * Composite of operator iterators to combine different iterators into one
 * @author klaus
 */
public class OperatorsIteratorComposite implements Iterator<IOperator>
{
	private List<Iterator<IOperator>> components;

	private int currentComponentIndex;

	public OperatorsIteratorComposite()
	{
		components = new ArrayList<>();
		currentComponentIndex = 0;
	}

	public void add(Iterator<IOperator> newIterator)
	{
		components.add(newIterator);
	}

	@Override
	public boolean hasNext()
	{
		if (currentComponentIndex >= components.size()) {
			// no more elements
			return false;
		}
		Iterator<IOperator> iterator = components.get(currentComponentIndex);
		while (!iterator.hasNext()) {
			// no more elements on this iterator
			currentComponentIndex++;
			if (currentComponentIndex >= components.size()) {
				// no more elements
				return false;
			}
			iterator = components.get(currentComponentIndex);
		}

		return true;
	}

	@Override
	public IOperator next()
	{
		return components.get(currentComponentIndex).next();
	}
}