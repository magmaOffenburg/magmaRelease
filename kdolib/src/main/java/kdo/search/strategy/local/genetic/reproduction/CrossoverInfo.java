/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.reproduction;

/**
 * Holds the information created in a crossover
 * @author dorer
 */
public class CrossoverInfo
{
	private float[] chromosom;

	private int[] parentID;

	/**
	 * @param chromosom
	 * @param parentID
	 */
	public CrossoverInfo(float[] chromosom, int[] parentID)
	{
		super();
		this.chromosom = chromosom;
		this.parentID = parentID;
	}

	/**
	 * @return the chromosom
	 */
	public float[] getChromosom()
	{
		return chromosom;
	}

	/**
	 * @return the parentID
	 */
	public int[] getParentID()
	{
		return parentID;
	}
}
