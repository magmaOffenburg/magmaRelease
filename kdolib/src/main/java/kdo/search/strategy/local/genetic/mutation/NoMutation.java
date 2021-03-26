/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.mutation;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.base.StrategyBase;
import kdo.search.strategy.local.genetic.IMutation;

/**
 * Dummy Mutation strategy that does not mutate
 *
 * @author klaus
 */
public class NoMutation extends StrategyBase implements IMutation
{
	/**
	 * Default constructor
	 */
	public NoMutation()
	{
		super("NoMutation");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.local.genetic.IMutation#mutate()
	 */
	@Override
	public void mutate(List<IIndividuum> individuums)
	{
		return;
	}

	@Override
	public void setIndividuumMutationProbability(float individuumMutationProbability)
	{
	}

	@Override
	public void setGeneMutationProbability(float geneMutationProbability)
	{
	}
}
