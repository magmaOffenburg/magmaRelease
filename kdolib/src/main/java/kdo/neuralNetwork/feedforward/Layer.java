/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.feedforward;

import java.io.Serializable;
import java.util.Arrays;
import kdo.neuralNetwork.optimizer.IOptimizer;
import kdo.neuralNetwork.transfer.ITransferFunction;
import kdo.util.IRandomSource;

/**
 * Represents a layer of nodes and their incoming weights in a neural network
 */
public abstract class Layer implements Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	/** the weights of the layer (learnable) */
	protected float[][] weight;

	/** the bias which is added (learnable) */
	protected float[] bias;

	/** the output of each node (after activation function) */
	protected final float[] outputLayer;

	/** the activation of each node (before activation function) */
	protected float[] summedInputs;

	/** the input coming from previous layer */
	protected final float[] inputLayer;

	/** the transfer function used for the nodes in this layer */
	protected final ITransferFunction transferFunction;

	/**
	 * the index of this layer, starting at 0 for the input to first hidden (or
	 * output) layer
	 */
	private int index;

	/**
	 * Creates a new Layer and initializes its weights randomly
	 * @param index the zero based index of this layer starting from input
	 * @param noOfInputs the number of input neurons (including bias)
	 * @param noOfOutputs the number of output neurons
	 * @param transfer the transfer function to use
	 * @param rand the source for random numbers
	 */
	public Layer(int index, int noOfInputs, int noOfOutputs, ITransferFunction transfer, IRandomSource rand)
	{
		this(index, noOfInputs, noOfOutputs, transfer);
		if (noOfInputs < 1) {
			throw new IllegalArgumentException("Need at least one input but had: " + noOfInputs);
		}
		if (noOfOutputs < 1) {
			throw new IllegalArgumentException("Need at least one output but had: " + noOfOutputs);
		}
		weight = new float[noOfInputs][noOfOutputs];
		bias = new float[noOfOutputs];
		initializeWeights(0.0f, 1.0f, rand);
	}

	/**
	 * Creates a new layer and uses the passed weights and bias to setup the layer
	 * @param index the index this layers has (counting from input to first layer = 0)
	 * @param weights array of weights
	 * @param bias the bias for each output neuron
	 * @param transfer the transfer (activation) function to use for this layer
	 */
	public Layer(int index, float[][] weights, float[] bias, ITransferFunction transfer)
	{
		this(index, weights.length, bias.length, transfer);
		this.weight = weights;
		this.bias = bias;
	}

	private Layer(int index, int noOfInputs, int noOfOutputs, ITransferFunction transfer)
	{
		this.index = index;
		transferFunction = transfer;
		summedInputs = new float[noOfOutputs];
		outputLayer = new float[noOfOutputs];
		inputLayer = new float[noOfInputs];
	}

	/**
	 * Initializes all weights of this layer with values that are uniformly
	 * distributed between standardValue - delta (including) and standardValue +
	 * delta (not including)
	 * @param standardValue mean value for the weights
	 * @param delta maximal delta to the standardValue
	 */
	private void initializeWeights(float standardValue, float delta, IRandomSource random)
	{
		for (float[] row : weight) {
			for (int j = 0; j < row.length; j++) {
				row[j] = random.nextFloat() * 2.0f * delta + standardValue - delta;
			}
		}
		for (int j = 0; j < bias.length; j++) {
			bias[j] = random.nextFloat() * 2.0f * delta + standardValue - delta;
		}
	}

	/**
	 * Calculates the output of this layer for the input passed
	 * @param input the input for which to calculate the output. The length has
	 *        to fit to the number of inputs.
	 * @return the output of this layer
	 */
	public float[] recall(float[] input)
	{
		checkInputLength(input.length);

		// store input
		System.arraycopy(input, 0, inputLayer, 0, input.length);

		// calculate output
		int outputs = weight[0].length;
		for (int i = 0; i < outputs; i++) {
			summedInputs[i] = 0;
			for (int j = 0; j < weight.length; j++) {
				summedInputs[i] += weight[j][i] * input[j];
			}
			summedInputs[i] += bias[i];
			outputLayer[i] = transferFunction.transfer(summedInputs[i]);
		}
		return outputLayer;
	}

	/**
	 * Adjusts the weights of this layer using the learn strategy
	 * @param gradient the delta of the output
	 * @param learnrate the current learnrate
	 * @return the error in this pattern as the mean of all delta squares of
	 *         output and expectedOutput
	 */
	protected float adjustWeights(IOptimizer optimizer, float[] gradient)
	{
		// checkOutputLength(deltaOutput.length);

		// adjust weights
		float error = 0.0f;
		for (int i = 0; i < gradient.length; i++) {
			float delta = gradient[i];
			error += delta * delta;
			for (int j = 0; j < weight.length; j++) {
				optimizer.apply(weight[j], inputLayer[j] * delta, index, j, i);
			}
			// adjust bias
			optimizer.apply(bias, delta, index, weight.length, i);
		}

		// System.out.println("Weights: " + Arrays.deepToString(weight));

		error /= gradient.length;
		return error;
	}

	/**
	 * @return the number of input neurons to this layer
	 */
	public int getInputLength()
	{
		return weight.length;
	}

	/**
	 * @return the number of output neurons of this layer
	 */
	public int getOutputLength()
	{
		return weight[0].length;
	}

	/**
	 * @param inputLength the number of neurons in the input
	 */
	private void checkInputLength(int inputLength)
	{
		if (inputLength != weight.length) {
			throw new IllegalArgumentException(
					"Input size " + inputLength + " does not match network input size " + weight.length);
		}
	}

	/**
	 * @param expectedOutput
	 * @return
	 */
	public abstract float[] calculateDelta(float[] expectedOutput);

	/**
	 * @param deltaNextLayer
	 * @return
	 */
	public float[] propagateDelta(float[] deltaNextLayer)
	{
		float[] result = new float[weight.length];
		for (int i = 0; i < weight.length; i++) {
			float sum = 0.0f;
			for (int k = 0; k < deltaNextLayer.length; k++) {
				sum += deltaNextLayer[k] * weight[i][k];
			}
			// no bias propagation?
			result[i] = sum;
		}
		return result;
	}

	@Override
	public String toString()
	{
		return Arrays.deepToString(weight) + " bias: " + Arrays.toString(bias) + "\n";
	}
}
