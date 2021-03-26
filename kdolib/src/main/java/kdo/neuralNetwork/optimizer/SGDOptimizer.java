/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.optimizer;

/**
 * Simple stochastic gradient descent optimizer without momentum.
 *
 * @author klaus
 */
public class SGDOptimizer extends Optimizer
{
	public SGDOptimizer(float learnrate)
	{
		super("Stochastic Gradient Descent SGD", learnrate);
	}

	@Override
	public void apply(float[] parameters, float gradient, int layerIndex, int inIndex, int outIndex)
	{
		parameters[outIndex] += learnrate * gradient;
	}
}
