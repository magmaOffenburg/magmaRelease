/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.selection;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.local.genetic.IPopulation;
import kdo.util.IRandomSource;

/**
 * Extends MonteCarloSelection by influencing selection of follow up individuum
 * by the diversity of the selected individuums
 * @author klaus
 */
public class MinDiversityMonteCarloSelection extends MonteCarloSelection
{
	/**
	 * the minimal relative diversity that a parent must have to all other
	 * parents to be selected
	 */
	private final float minimumDiversity;

	/**
	 * the number of tries to find an enough divers parent before a random
	 * individuum is generated
	 */
	private final int tryCount;

	/** the domain on which this selection strategy works */

	/**
	 *
	 * @param ascending TODO
	 * @param rand
	 */
	public MinDiversityMonteCarloSelection(boolean ascending, IRandomSource rand, float minimumDiversity, int tryCount)
	{
		super("MinDiversitySelection", rand, ascending);
		this.minimumDiversity = minimumDiversity;
		this.tryCount = tryCount;
	}

	/**
	 * @param population the population for which to do selection
	 * @param oldIndividuums the list of individuums from which to create new.
	 *        This list needs not to be identical to the individuums list of
	 *        population, e.g. the sorting may vary
	 * @param generated the number of individuums already generated for this
	 *        generation
	 * @param parents the so far selected individuums
	 * @param parentID the number of parents selected so far
	 * @return the new individuum that was created
	 */
	@Override
	public IIndividuum selectIndividuum(IPopulation population, List<IIndividuum> oldIndividuums, int generated,
			IIndividuum[] parents, int parentID)
	{
		IIndividuum selectedIndividuum =
				super.selectIndividuum(population, oldIndividuums, generated, parents, parentID);
		if (parentID > 0) {
			// TODO: check for all parents
			IIndividuum parent = parents[0];
			int tries = 0;
			while (tries < tryCount) {
				float diversity = parent.getRelativeDiversity(selectedIndividuum);
				if (diversity > minimumDiversity) {
					return selectedIndividuum;
				}
				tries++;
			}
			// no individuum was divers enough
			selectedIndividuum = (IIndividuum) population.createRandomState();
			// System.out.println("Random Individuum");
		}
		return selectedIndividuum;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see search.strategy.local.genetic.StrategyBase#getName()
	 */
	@Override
	public String toString()
	{
		return getName() + " minimumDiversity: " + minimumDiversity + " tryCount: " + tryCount;
	}
}
