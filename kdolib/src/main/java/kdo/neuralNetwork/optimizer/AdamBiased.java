/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.optimizer;

/**
 * Adam gradient descent algorithm without bias reduction. This is slightly
 * faster, but with some initial bias towards zero.
 *
 * @author klaus
 */
public class AdamBiased extends Optimizer
{
	/** estimate of the first moment for each learnable parameter */
	protected float m[][][];

	/** estimate of the second moment for each learnable parameter */
	protected float v[][][];

	/** first exponential decay rate (default 0.9) */
	protected float beta1;

	/** second exponential decay rate (default 0.999) */
	protected float beta2;

	/** avoid division by zero parameter (default 1e-8) */
	protected float epsilon;

	public AdamBiased(float learnrate)
	{
		this(learnrate, 0.9f, 0.999f, 1e-8f);
	}

	public AdamBiased(float learnrate, float beta1, float beta2, float epsilon)
	{
		this("AdamBiased", learnrate, beta1, beta2, epsilon);
	}

	public AdamBiased(String name, float learnrate, float beta1, float beta2, float epsilon)
	{
		super(name, learnrate);
		this.beta1 = beta1;
		this.beta2 = beta2;
		this.epsilon = epsilon;
	}

	@Override
	public IOptimizer prepare(int[] layerSizes)
	{
		m = new float[layerSizes.length - 1][][];
		v = new float[layerSizes.length - 1][][];
		for (int i = 0; i < layerSizes.length - 1; i++) {
			// + 1 for the bias
			m[i] = new float[layerSizes[i] + 1][layerSizes[i + 1]];
			v[i] = new float[layerSizes[i] + 1][layerSizes[i + 1]];
		}
		return super.prepare(layerSizes);
	}

	@Override
	public void apply(float[] parameters, float gradient, int layerIndex, int inIndex, int outIndex)
	{
		m[layerIndex][inIndex][outIndex] = beta1 * m[layerIndex][inIndex][outIndex] + (1 - beta1) * gradient;
		v[layerIndex][inIndex][outIndex] = beta2 * v[layerIndex][inIndex][outIndex] + (1 - beta2) * gradient * gradient;
		parameters[outIndex] +=
				learnrate * m[layerIndex][inIndex][outIndex] / (Math.sqrt(v[layerIndex][inIndex][outIndex]) + epsilon);
	}

	@Override
	public String toString()
	{
		return super.toString() + "\nbeta1: " + beta1 + "\nbeta2: " + beta2 + "\nepsilon: " + epsilon;
	}
}
