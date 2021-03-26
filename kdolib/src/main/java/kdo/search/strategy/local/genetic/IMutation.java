/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.IStrategy;

/**
 * Interface for mutation strategies
 * @author klaus
 */
public interface IMutation extends IStrategy {
	/**
	 * Mutates the passed individuums, i.e. changes randomly a (small) number of
	 * genes
	 * @param individuums the individuums to mutate
	 */
	void mutate(List<IIndividuum> individuums);

	/**
	 * @param individuumMutationProbability the probability by which an
	 *        individuum is selected for mutation
	 */
	void setIndividuumMutationProbability(float individuumMutationProbability);

	/**
	 * @param geneMutationProbability the probability with which a single gene is
	 *        mutated
	 */
	void setGeneMutationProbability(float geneMutationProbability);
}
