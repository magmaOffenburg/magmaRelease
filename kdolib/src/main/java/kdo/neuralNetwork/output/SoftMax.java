/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.output;

import java.io.Serializable;

/**
 * Returns an output that is a probability distribution summing to 1
 * @author kdorer
 */
public class SoftMax implements IOutputFunction, Serializable
{
	@Override
	public float[] output(float[] output)
	{
		float[] result = new float[output.length];
		float sum = 0.0f;
		for (int i = 0; i < output.length; i++) {
			result[i] = (float) Math.exp(output[i]);
			sum += result[i];
		}
		for (int i = 0; i < output.length; i++) {
			result[i] /= sum;
		}
		return result;
	}
}
