/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.data;

/**
 * Represents a pair of input and expected output
 */
public class InOutPattern
{
	/** the input of the pair (including bias) */
	private final float[] input;

	/** the corresponding expected output */
	private final float[] output;

	/**
	 * @param input
	 * @param output
	 */
	public InOutPattern(float[] input, float[] output)
	{
		this.input = input;
		this.output = output;
	}

	/**
	 * @param index the index of the neuron for which to get the input
	 * @return the input of neuron index
	 */
	public float getInput(int index)
	{
		return input[index];
	}

	/**
	 * @param index the index of the neuron for which to get the output
	 * @return the output of neuron index
	 */
	public float getOutput(int index)
	{
		return output[index];
	}

	/**
	 * @return the input
	 */
	public float[] getInput()
	{
		return input;
	}

	/**
	 * @return the output
	 */
	public float[] getOutput()
	{
		return output;
	}

	/**
	 * @return the number of input neurons
	 */
	public int getInputCount()
	{
		return input.length;
	}

	/**
	 * @return the number of output neurons
	 */
	public int getOutputCount()
	{
		return output.length;
	}
}
