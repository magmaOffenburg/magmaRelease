/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.IStrategy;

/**
 * @author klaus
 *
 */
public interface ISelection extends IStrategy {
	/**
	 * Notification called before the selection of individuums start
	 * @param indiList the list of individuums from which to select later
	 */
	void onBeforeSelection(List<IIndividuum> indiList);

	/**
	 * Selects an individuum from the passed list for reproduction
	 * @param population the population for which to do selection
	 * @param indiList the list of individuums from which to select.
	 * @param generated the number of individuums already generated for this
	 *        generation
	 * @param parents the so far selected individuums
	 * @param parentID the number of parents selected so far
	 * @return the new individuum that was selected
	 */
	IIndividuum selectIndividuum(
			IPopulation population, List<IIndividuum> indiList, int generated, IIndividuum[] parents, int parentID);
}
