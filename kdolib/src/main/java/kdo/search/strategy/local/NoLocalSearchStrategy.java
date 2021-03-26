/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local;

import kdo.domain.IProblemState;

/**
 * Implementation of a local search strategy that does nothing. Provided for
 * simplicity of factory creation.
 */
public class NoLocalSearchStrategy extends LocalSearchBase
{
	public NoLocalSearchStrategy()
	{
		super("NoLocalSearchStrategy", null, 0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * kdo.search.strategy.ILocalSearchStrategy#search(kdo.domain.IProblemState)
	 */
	public IProblemState search(IProblemState initialState)
	{
		return initialState;
	}
}
