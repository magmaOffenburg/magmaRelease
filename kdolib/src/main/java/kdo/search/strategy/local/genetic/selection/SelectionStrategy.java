/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.search.strategy.local.genetic.selection;

import java.util.List;
import kdo.domain.IIndividuum;
import kdo.search.strategy.base.RandomStrategy;
import kdo.search.strategy.local.genetic.ISelection;
import kdo.util.IRandomSource;

/**
 * @author klaus
 *
 */
public abstract class SelectionStrategy extends RandomStrategy implements ISelection
{
	/**
	 * for statistic purposes the number of times individuum i is selected during
	 * one selection
	 */
	protected int selected[];

	/**
	 * for statistic purposes the number of times individuum i is selected during
	 * all selections
	 */
	public int sumSelected[];

	/** statistics only, the average fitness of the sorted nth individuum */
	public float[] averageFitness;

	public float noOfAvgFitnessValues;

	/**
	 * @param name the name of the strategy
	 * @param rand source for random numbers
	 */
	public SelectionStrategy(String name, IRandomSource rand)
	{
		super(name, rand);
	}

	/**
	 * Called before Selection process for next generation is run. Default
	 * implementation does nothing.
	 * @param oldIndividuums the sorted list of individuums of the old population
	 */
	@Override
	public void onBeforeSelection(List<IIndividuum> oldIndividuums)
	{
		int size = oldIndividuums.size();
		selected = new int[size];
		if (sumSelected == null || sumSelected.length != size) {
			sumSelected = new int[size];
			averageFitness = new float[size];
			noOfAvgFitnessValues = 0;
		}
	}
}
