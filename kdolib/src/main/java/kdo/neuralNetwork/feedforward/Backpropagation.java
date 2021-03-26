/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.feedforward;

import java.util.ArrayList;
import java.util.List;
import kdo.neuralNetwork.data.InOutPattern;
import kdo.neuralNetwork.output.IOutputFunction;
import kdo.neuralNetwork.transfer.ITransferFunction;
import kdo.util.IRandomSource;

/**
 * Represents a multi-layer feed-forward network using backpropagation
 */
public class Backpropagation extends FeedForwardNetwork
{
	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param outputFunction the function for output layer
	 */
	public Backpropagation(IOutputFunction outputFunction)
	{
		super(outputFunction);
		layers = new ArrayList<>();
	}

	/**
	 * Use this method to create a newly randomly initialized networks with the
	 * passed layer structure
	 * @param noOfNodes the number of nodes in each layer (including input and
	 *        output)
	 * @param transfer the transfer function (activation function)
	 */
	public Backpropagation addLayers(int[] noOfNodes, ITransferFunction transfer, IRandomSource random)
	{
		int i;
		for (i = 0; i < noOfNodes.length - 2; i++) {
			addLayer(new BPGHiddenLayer(i, noOfNodes[i], noOfNodes[i + 1], transfer, random));
		}
		addLayer(new BPGOutputLayer(i, noOfNodes[i], noOfNodes[i + 1], transfer, random));
		return this;
	}

	@Override
	public float[] recall(float[] input)
	{
		float[] currentInput = input;
		for (Layer current : layers) {
			// System.out.println("Layer Input: " + Arrays.toString(currentInput));
			currentInput = current.recall(currentInput);
		}
		// System.out.println("Output: " + Arrays.toString(currentInput));
		return outputFunction.output(currentInput);
	}

	@Override
	public float train(List<InOutPattern> batch)
	{
		float[] batchDeltas = recallAll(batch);
		float error = getRMS(batchDeltas);

		for (int i = layers.size() - 1; i >= 0; i--) {
			Layer currentLayer = layers.get(i);
			float[] newDelta = currentLayer.calculateDelta(batchDeltas);
			batchDeltas = currentLayer.propagateDelta(newDelta);
			currentLayer.adjustWeights(optimizer, newDelta);
		}

		return error;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		for (Layer layer : layers) {
			builder.append(layer.toString());
		}
		return builder.toString();
	}
}
