/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.domain.model;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kdo.domain.IOperator;
import kdo.domain.IProblemState;

/**
 * Base class for all operator iterators
 */
public abstract class OperatorIteratorBase implements Iterator<IOperator>
{
	/** the Operator that is next given back by next(); */
	protected IOperator nextElement;

	/** reference to the originating problem state */
	protected IProblemState state;

	/** flag indicating if the first call to fetchNext() has been done */
	private boolean initialized;

	/**
	 * Default constructor
	 * @param state the state on which this operator is applied
	 */
	public OperatorIteratorBase(IProblemState state)
	{
		this.state = state;
		initialized = false;
	}

	public boolean hasNext()
	{
		if (!initialized) {
			fetchNext();
			initialized = true;
		}
		return nextElement != null;
	}

	public IOperator next()
	{
		if (!initialized) {
			fetchNext();
			initialized = true;
		}

		// check if there is a next Operator
		if (nextElement == null) {
			throw new NoSuchElementException();
		}

		// save the next element to tmp since fetchNext will change nextElement
		IOperator tmp = nextElement;
		fetchNext();
		return tmp;
	}

	/**
	 * Sets nextElement to the next operator to return
	 */
	protected abstract void fetchNext();

	public void remove()
	{
		throw new UnsupportedOperationException("Remove not supported");
	}
}
