/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.feedforward;

import kdo.neuralNetwork.transfer.ITransferFunction;
import kdo.util.IRandomSource;

/**
 * Represents a layer of nodes and their incoming weights in a neural network
 */
public class BPGOutputLayer extends Layer
{
	/**	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an initializes a backpropagation output layer
	 * @param noOfInputs the number of input neurons (including bias)
	 * @param noOfOutputs the number of output neurons
	 * @param transfer the transfer function to use
	 * @param rand the source for random numbers
	 */
	public BPGOutputLayer(int index, int noOfInputs, int noOfOutputs, ITransferFunction transfer, IRandomSource rand)
	{
		super(index, noOfInputs, noOfOutputs, transfer, rand);
	}

	/**
	 * Creates a new output layer and uses the passed weights and bias to setup the layer
	 * @param index the index this layers has (counting from input to first layer = 0)
	 * @param weights array of weights
	 * @param bias the bias for each output neuron
	 * @param transfer the transfer (activation) function to use for this layer
	 */
	public BPGOutputLayer(int index, float[][] weights, float[] bias, ITransferFunction transfer)
	{
		super(index, weights, bias, transfer);
	}

	/**
	 * Calculates the delta value for an output layer as g'(ini) * (Ti - Oi)
	 * @param input
	 * @param batchDelta
	 * @return
	 */
	@Override
	public float[] calculateDelta(float[] batchDelta)
	{
		float[] result = new float[outputLayer.length];
		for (int i = 0; i < weight[0].length; i++) {
			result[i] = transferFunction.transferDerivative(summedInputs[i], outputLayer[i]) * batchDelta[i];
		}

		// System.out.println("OutputDelta: " + Arrays.toString(result));
		return result;
	}
}
