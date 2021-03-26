/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.optimizer;

public abstract class Optimizer implements IOptimizer
{
	/** the learnrate (step size) to adjust along a gradient */
	protected float learnrate;

	/** the readable name of this optimizer */
	private String name;

	public Optimizer(String name, float learnrate)
	{
		super();
		this.name = name;
		this.learnrate = learnrate;
	}

	@Override
	public void setLearnrate(float rate)
	{
		learnrate = rate;
	}

	@Override
	public IOptimizer prepare(int[] layerSizes)
	{
		return this;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return name + "\nlearnrate: " + learnrate;
	}
}
