/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.output;

import java.io.Serializable;

/**
 * Only the output with highest value is returned
 * @author kdorer
 */
public class WinnerTakesAll implements IOutputFunction, Serializable
{
	/**
	 * true if the winning output should return a value specified from outside
	 */
	private boolean useExternalValue;

	/** the value to use for winning output if useExternalValue is true */
	private float outsideWinnerValue;

	/**
	 * @param useExternalValue true if the winning output should return a value
	 *        specified from outside
	 * @param outsideWinnerValue the value to use for winning output if
	 *        useExternalValue is true
	 */
	public WinnerTakesAll(boolean useExternalValue, float outsideWinnerValue)
	{
		this.useExternalValue = useExternalValue;
		this.outsideWinnerValue = outsideWinnerValue;
	}

	@Override
	public float[] output(float[] output)
	{
		float candidate = output[0];
		int candidateIndex = 0;
		for (int i = 1; i < output.length; i++) {
			float next = output[i];
			if (next > candidate) {
				candidate = next;
				candidateIndex = i;
			}
		}

		float[] result = new float[output.length];
		for (int i = 0; i < output.length; i++) {
			if (i == candidateIndex) {
				if (useExternalValue) {
					result[i] = outsideWinnerValue;
				} else {
					result[i] = output[i];
				}
			} else {
				result[i] = 0;
			}
		}
		return result;
	}
}
