/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.domain;

/**
 * Interface to all alele creation strategies.
 * @author kdorer
 */
public interface IChromosomStrategy {
	/**
	 * Returns a complete random chromosom
	 * @return a complete random chromosom
	 */
	float[] getRandomChromosom();

	/**
	 * Returns a new random value for the passed gene. The chromosom is not
	 * changed! It is passed as parameter for strategies that depend on other
	 * genes.
	 * @param index the 0 based index of the gene
	 * @param chromosom the underlying chromosom
	 * @return a new random value for the passed gene
	 */
	float getRandomAlele(int index, float[] chromosom);

	/**
	 * Returns the size of a chromosom
	 * @return the size of a chromosom
	 */
	int getChromosomSize();
}
