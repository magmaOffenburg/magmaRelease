/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain;

import java.util.Comparator;

/**
 * @author klaus
 *
 */
public interface IGeneticProblem extends IProblem {
	/**
	 * Factory method for creating an individuum using the chromosoms passed
	 * @param chromosom the chromosoms of the individuum
	 * @return an individuum using the chromosoms passed
	 */
	IIndividuum createIndividuum(float[] chromosom);

	/**
	 * @return a random individuum of this domain
	 */
	IIndividuum getRandomIndividuum();

	/**
	 * Returns a new random value for the passed gene. The chromosom is not
	 * changed! It is passed as parameter for strategies that depend on other
	 * genes.
	 * @param index the 0 based index of the gene
	 * @param chromosom the underlying chromosom
	 * @return a new random value for the passed gene
	 */
	float getRandomAlele(int gene, float[] chromosom);

	/**
	 * @return the comparator to use to compare individuums in this domain
	 */
	Comparator<IIndividuum> getIndividuumComparator();
}
