/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.feedforward;

import java.util.ArrayList;
import java.util.List;
import kdo.neuralNetwork.data.InOutPattern;
import kdo.neuralNetwork.output.IdentityOutputFunction;
import kdo.neuralNetwork.transfer.StepTransferFunction;
import kdo.util.IRandomSource;

/**
 * Represents a single layer Perceptron network
 */
public class Perceptron extends FeedForwardNetwork
{
	/**	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a perceptron network an initializes its weights
	 * @param noOfInputs the number of input neurons (including bias)
	 * @param noOfOutputs the number of output neurons
	 * @param rand the source for random numbers
	 */
	public Perceptron(int noOfInputs, int noOfOutputs, IRandomSource rand)
	{
		this();
		if (noOfInputs < 1) {
			throw new IllegalArgumentException("Need at least one input but had: " + noOfInputs);
		}
		if (noOfOutputs < 1) {
			throw new IllegalArgumentException("Need at least one output but had: " + noOfOutputs);
		}

		addLayer(new BPGOutputLayer(0, noOfInputs, noOfOutputs, new StepTransferFunction(0.0f), rand));
	}

	/**
	 * Creates a perceptron network without any layers. Layers should be added with the addLayer(...) method
	 */
	public Perceptron()
	{
		super(new IdentityOutputFunction());
		layers = new ArrayList<>();
	}

	@Override
	public float[] recall(float[] input)
	{
		float[] currentInput = input;
		// System.out.println("Layer Input: " + Arrays.toString(currentInput));
		currentInput = layers.get(0).recall(currentInput);
		// System.out.println("Output: " + Arrays.toString(currentInput));
		return outputFunction.output(currentInput);
	}

	@Override
	public float train(List<InOutPattern> batch)
	{
		float[] batchDeltas = recallAll(batch);
		float error = getRMS(batchDeltas);

		float[] newDelta = layers.get(0).calculateDelta(batchDeltas);
		layers.get(0).adjustWeights(optimizer, newDelta);

		return error;
	}

	@Override
	public String toString()
	{
		return layers.get(0).toString();
	}
}
