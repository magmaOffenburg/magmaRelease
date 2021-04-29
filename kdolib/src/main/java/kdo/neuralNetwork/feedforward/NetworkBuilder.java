/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.feedforward;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kdo.neuralNetwork.INeuralNetwork;
import kdo.neuralNetwork.optimizer.AdamOptimizer;
import kdo.neuralNetwork.optimizer.IOptimizer;
import kdo.neuralNetwork.optimizer.SGDMomentumOptimizer;
import kdo.neuralNetwork.optimizer.SGDOptimizer;
import kdo.neuralNetwork.output.IdentityOutputFunction;
import kdo.neuralNetwork.transfer.ITransferFunction;
import kdo.neuralNetwork.transfer.IdentityTransferFunction;
import kdo.neuralNetwork.transfer.ReluTransferFunction;
import kdo.neuralNetwork.transfer.SigmoidTransferFunction;
import kdo.neuralNetwork.transfer.StepTransferFunction;
import kdo.neuralNetwork.transfer.TanhTransferFunction;

/**
 * @author kdorer
 *
 */
public class NetworkBuilder
{
	private INeuralNetwork result;

	public NetworkBuilder(String type)
	{
		// TODO: add output function injection
		if (type.equals("Backpropagation")) {
			result = new Backpropagation(new IdentityOutputFunction());
		} else {
			result = new Perceptron();
		}
	}

	public NetworkBuilder addOptimizer(String name, Map<String, Float> parameters)
	{
		result.setOptimizer(createOptimizer(name, parameters));
		return this;
	}

	public NetworkBuilder addOptimizer(IOptimizer optimizer)
	{
		result.setOptimizer(optimizer);
		return this;
	}

	private NetworkBuilder addLayer(String transfer, String type, float[][] weights, float[] bias)
	{
		ITransferFunction transferFunction = createTransferFunction(transfer);
		if (type.equals("Hidden")) {
			result.addLayer(new BPGHiddenLayer(result.getNoOfLayers(), weights, bias, transferFunction));
		} else {
			result.addLayer(new BPGOutputLayer(result.getNoOfLayers(), weights, bias, transferFunction));
		}
		return this;
	}

	public INeuralNetwork create()
	{
		return result;
	}

	private IOptimizer createOptimizer(String name, Map<String, Float> parameters)
	{
		switch (name) {
		// TODO: add all hyper-parameters of Adam
		case "Adam":
			return new AdamOptimizer(parameters.get("learnrate"));
		case "SGD":
			return new SGDOptimizer(parameters.get("learnrate"));
		case "SGDMomentum":
			return new SGDMomentumOptimizer(parameters.get("learnrate"), parameters.get("momentum"));
		default:
			return null;
		}
	}

	private ITransferFunction createTransferFunction(String name)
	{
		switch (name) {
		case "Sigmoid":
			return new SigmoidTransferFunction();
		case "Tanh":
			return new TanhTransferFunction();
		case "Relu":
			return new ReluTransferFunction();
		case "Step":
			return new StepTransferFunction(0);
		default:
			return new IdentityTransferFunction();
		}
	}

	public static INeuralNetwork buildNetwork(List<String[]> fileContent)
	{
		NetworkBuilder builder = new NetworkBuilder(fileContent.get(0)[0]);

		// optimizer
		String[] optimizerLine = fileContent.get(1);
		String optimizerName = optimizerLine[0];
		builder.addOptimizer(optimizerName, convertToParameters(optimizerLine, 1));

		// layers
		int currentLineIndex = 2;
		while (currentLineIndex < fileContent.size()) {
			currentLineIndex = createLayer(builder, fileContent, currentLineIndex);
		}
		return builder.create();
	}

	private static int createLayer(NetworkBuilder builder, List<String[]> fileContent, int currentLineIndex)
	{
		// header
		String[] headerLine = fileContent.get(currentLineIndex);
		int inputSize = Integer.valueOf(headerLine[1]);
		int outputSize = Integer.valueOf(headerLine[2]);
		String transfer = headerLine[3];
		String type = headerLine[4];
		currentLineIndex++;

		// weights
		float[][] weights = new float[inputSize][outputSize];
		for (int i = 0; i < inputSize; i++) {
			if (currentLineIndex >= fileContent.size()) {
				System.err.println(
						"Input size of layer does not fit to number of lines in file. Lines: " + fileContent.size());
			}
			String[] currentLine = fileContent.get(currentLineIndex);
			currentLineIndex++;
			if (currentLine.length < outputSize) {
				System.err.println(
						"Output size of layer does not fit to number of values in line: " + currentLineIndex);
			}
			for (int j = 0; j < outputSize; j++) {
				weights[i][j] = Float.valueOf(currentLine[j]);
			}
		}

		// bias
		String[] currentLine = fileContent.get(currentLineIndex);
		if (currentLine.length < outputSize) {
			System.err.println("Output size of layer does not fit to number of values in line: " + currentLineIndex);
		}
		float[] bias = new float[outputSize];
		for (int j = 0; j < outputSize; j++) {
			bias[j] = Float.valueOf(currentLine[j]);
		}
		currentLineIndex++;

		builder.addLayer(transfer, type, weights, bias);
		return currentLineIndex;
	}

	private static Map<String, Float> convertToParameters(String[] optimizerLine, int startIndex)
	{
		Map<String, Float> result = new HashMap<>();
		for (int i = startIndex; i < optimizerLine.length; i += 2) {
			result.put(optimizerLine[i], Float.valueOf(optimizerLine[i + 1]));
		}
		return result;
	}
}
