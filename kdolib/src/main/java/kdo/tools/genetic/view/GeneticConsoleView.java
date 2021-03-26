/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.tools.genetic.view;

/*
 * Copyright (c) 2007-2009 Klaus Dorer
 * Copyright (c) 2009 Simon Raffeiner
 *
 * Hochschule Offenburg
 */

import kdo.tools.genetic.model.GeneticOptimization;
import kdo.util.observer.IObserver;

/**
 * The frame containing all the elements to do in the exercise
 *
 * @author klaus
 */
public class GeneticConsoleView implements IObserver<Boolean>, IGeneticView
{
	/** reference to the model class (TODO: should be read only) */
	private GeneticOptimization model;

	/**
	 * Default constructor to create the frame
	 */
	public GeneticConsoleView(GeneticOptimization model)
	{
		this.model = model;
		model.attach(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kdo.domain.genetic.view.IGeneticView#update(java.lang.Boolean)
	 */
	@Override
	public void update(Boolean running)
	{
		StringBuffer text = new StringBuffer(200);
		text.append(model.getCurrentGeneration());
		text.append(" fitness (best): " + model.getBestFitness());
		text.append(" diversity: " + model.getAverageDiversity());
		text.append(" runtime: " + model.getRuntime());
		System.out.println(text.toString());
	}

	@Override
	public void setModel(GeneticOptimization model)
	{
		this.model = model;
		update(false);
	}

	@Override
	public void updateView()
	{
	}
}
