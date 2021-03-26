/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic;

import kdo.domain.IIndividuum;
import kdo.search.strategy.IStrategy;

/**
 * @author klaus
 *
 */
public interface IReproduction extends IStrategy {
	/**
	 * performs a crossing over of the chromosoms of the two parents
	 * @param parents the array of parents for reproduction
	 * @return a new child Individuum
	 */
	IIndividuum crossOver(IIndividuum[] parents);
}
