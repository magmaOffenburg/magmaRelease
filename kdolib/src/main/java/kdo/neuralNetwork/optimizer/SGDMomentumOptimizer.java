/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.optimizer;

/**
 * Simple stochastic gradient descent optimizer with momentum.
 *
 * @author klaus
 */
public class SGDMomentumOptimizer extends Optimizer
{
	/** we have to store the previous gradient of each parameter to learn */
	private float previousGradient[][][];

	/** the momentum (sometimes called beta) in [0..1) */
	private float momentum;

	public SGDMomentumOptimizer(float learnrate, float momentum)
	{
		super("SGD with Momentum", learnrate);
		this.momentum = momentum;
	}

	@Override
	public IOptimizer prepare(int[] layerSizes)
	{
		previousGradient = new float[layerSizes.length - 1][][];
		for (int i = 0; i < layerSizes.length - 1; i++) {
			// + 1 for the bias
			previousGradient[i] = new float[layerSizes[i] + 1][layerSizes[i + 1]];
		}
		return super.prepare(layerSizes);
	}

	@Override
	public void apply(float[] parameters, float gradient, int layerIndex, int inIndex, int outIndex)
	{
		float delta = (gradient * learnrate) + (previousGradient[layerIndex][inIndex][outIndex] * momentum);
		previousGradient[layerIndex][inIndex][outIndex] = delta;
		parameters[outIndex] += delta;
	}

	@Override
	public String toString()
	{
		return super.toString() + "\nmomentum: " + momentum;
	}
}
