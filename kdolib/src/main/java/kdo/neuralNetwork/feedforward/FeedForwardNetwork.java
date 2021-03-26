/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.feedforward;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import kdo.neuralNetwork.INeuralNetwork;
import kdo.neuralNetwork.data.InOutDataList;
import kdo.neuralNetwork.data.InOutPattern;
import kdo.neuralNetwork.optimizer.IOptimizer;
import kdo.neuralNetwork.output.IOutputFunction;

/**
 * Base class of feed forward networks
 */
public abstract class FeedForwardNetwork implements INeuralNetwork, Serializable
{
	private static final long serialVersionUID = 1L;

	/** the output function used to output activation */
	protected final IOutputFunction outputFunction;

	/** the optimizer performing the gradient descent */
	protected IOptimizer optimizer;

	/** the layers of this network */
	protected List<Layer> layers;

	/**
	 *
	 * @param optimizer the optimizer to use
	 * @param rand the source for random numbers
	 */
	public FeedForwardNetwork(IOutputFunction outputFunction)
	{
		this.optimizer = null;
		this.outputFunction = outputFunction;
	}

	@Override
	public INeuralNetwork setOptimizer(IOptimizer optimizer)
	{
		this.optimizer = optimizer;
		return this;
	}

	@Override
	public IOptimizer getOptimizer()
	{
		return optimizer;
	}

	/**
	 *
	 * @param rate the learnrate to use while learning
	 */
	@Override
	public void setLearnrate(float rate)
	{
		optimizer.setLearnrate(rate);
	}

	/**
	 * Adds a new layer to the network.
	 * @param newLayer the layer to add
	 */
	@Override
	public INeuralNetwork addLayer(Layer newLayer)
	{
		layers.add(newLayer);
		return this;
	}

	@Override
	public int getNoOfLayers()
	{
		return layers.size();
	}

	/**
	 * @return an array with the number of neurons forming each layer
	 */
	@Override
	public int[] getLayerSizes()
	{
		int[] result = new int[layers.size() + 1];
		for (int i = 0; i < layers.size(); i++) {
			result[i] = layers.get(i).getInputLength();
		}
		result[layers.size()] = layers.get(layers.size() - 1).getOutputLength();
		return result;
	}

	public float[] recallDeltas(float[] input, float[] output, float[] result)
	{
		float[] out = recall(input);
		for (int i = 0; i < out.length; i++) {
			result[i] += output[i] - out[i];
		}
		return result;
	}

	@Override
	public float[] recallAll(List<InOutPattern> patterns)
	{
		float[] error = new float[patterns.get(0).getOutputCount()];
		for (InOutPattern current : patterns) {
			recallDeltas(current.getInput(), current.getOutput(), error);
		}
		return error;
	}

	@Override
	public float recallAllRMS(List<InOutPattern> patterns)
	{
		float error = 0.0f;
		for (InOutPattern current : patterns) {
			float[] deltas =
					recallDeltas(current.getInput(), current.getOutput(), new float[patterns.get(0).getOutputCount()]);
			error += getSquareError(deltas);
		}
		return (float) Math.sqrt(error / patterns.size());
	}

	@Override
	public float getSquareError(float[] deltas)
	{
		float result = 0.0f;
		for (int i = 0; i < deltas.length; i++) {
			result += deltas[i] * deltas[i];
		}
		return result;
	}

	public float getRMS(float[] deltas)
	{
		return (float) Math.sqrt(getSquareError(deltas));
	}

	@Override
	public float recallAllPercent(List<InOutPattern> patterns)
	{
		int missclassified = 0;
		for (InOutPattern current : patterns) {
			float[] deltas = recallDeltas(current.getInput(), current.getOutput(), new float[current.getOutputCount()]);
			float singleError = getSquareError(deltas);
			if (singleError > 0.01) {
				missclassified++;
			}
		}
		float error = (float) missclassified / patterns.size();
		// System.out.println("\nError: " + error);
		return error;
	}

	@Override
	public double trainAll(
			InOutDataList trainingData, int batchSize, long maxEpochs, double stopLoss, int verbose, Random rand)
	{
		double loss = 1.0f;
		int epoch = 0;
		for (; epoch < maxEpochs && loss > stopLoss; epoch++) {
			loss = 0.0f;
			// shuffle the data at each epoch
			trainingData.shuffle(rand);

			// run through each batch
			Iterator<List<InOutPattern>> iterator = trainingData.iterator(batchSize);
			while (iterator.hasNext()) {
				loss += train(iterator.next());
			}
			if (verbose > 0 && epoch % verbose == 0) {
				printError(loss, epoch);
			}
		}
		// System.out.println("\nError: " + error + " Loops: " + loops);
		return loss;
	}

	protected void printError(double error, long loops)
	{
		System.out.println("Epoch " + loops + " Loss " + error);
	}

	public static INeuralNetwork deserializeNetwork(String filename)
	{
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
			INeuralNetwork network = (INeuralNetwork) in.readObject();
			in.close();
			return network;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void serializeNetwork(String filename)
	{
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(this);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static INeuralNetwork loadNetwork(String filename)
	{
		try {
			List<String[]> fileContent = readCsvFile(filename, ";");
			return NetworkBuilder.buildNetwork(fileContent);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Reads the CSV file and populates the contents in an ArrayList of Strings.
	 * @throws IOException If the file could not be successfully opened and
	 *         parsed
	 */
	public static List<String[]> readCsvFile(String filePath, String separator) throws IOException
	{
		ClassLoader cl = FeedForwardNetwork.class.getClassLoader();
		BufferedReader br = new BufferedReader(new InputStreamReader(cl.getResourceAsStream(filePath)));
		String line;
		List<String[]> csvContents = new ArrayList<>();

		while ((line = br.readLine()) != null) {
			String[] csvLineContents = line.split(separator);
			csvContents.add(csvLineContents);
		}

		br.close();
		return csvContents;
	}
}
