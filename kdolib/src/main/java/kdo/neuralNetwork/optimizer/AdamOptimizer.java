/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.optimizer;

/**
 * Adam gradient descent algorithm.
 *
 * @author klaus
 */
public class AdamOptimizer extends AdamBiased
{
	/** used to detect when a new pattern is learned */
	private int previousLayer;

	/** beta1 to the power of t */
	private float beta1Pow;

	/** beta2 to the power of t */
	private float beta2Pow;

	public AdamOptimizer(float learnrate)
	{
		this(learnrate, 0.9f, 0.999f, 1e-8f);
	}

	public AdamOptimizer(float learnrate, float beta1, float beta2, float epsilon)
	{
		super("Adam", learnrate, beta1, beta2, epsilon);
	}

	@Override
	public IOptimizer prepare(int[] layerSizes)
	{
		super.prepare(layerSizes);
		previousLayer = 0;
		beta1Pow = beta1;
		beta2Pow = beta2;
		return this;
	}

	@Override
	public void apply(float[] parameters, float gradient, int layerIndex, int inIndex, int outIndex)
	{
		// only increment time for each new pattern
		if (previousLayer < layerIndex) {
			beta1Pow *= beta1;
			beta2Pow *= beta2;
		}
		previousLayer = layerIndex;

		m[layerIndex][inIndex][outIndex] = beta1 * m[layerIndex][inIndex][outIndex] + (1 - beta1) * gradient;
		v[layerIndex][inIndex][outIndex] = beta2 * v[layerIndex][inIndex][outIndex] + (1 - beta2) * gradient * gradient;
		float m_unbiased = m[layerIndex][inIndex][outIndex] / (1 - beta1Pow);
		float v_unbiased = v[layerIndex][inIndex][outIndex] / (1 - beta2Pow);
		parameters[outIndex] += learnrate * m_unbiased / (Math.sqrt(v_unbiased) + epsilon);
	}

	@Override
	public String toString()
	{
		return super.toString() + "\nbeta1: " + beta1 + "\nbeta2: " + beta2 + "\nepsilon: " + epsilon;
	}
}
