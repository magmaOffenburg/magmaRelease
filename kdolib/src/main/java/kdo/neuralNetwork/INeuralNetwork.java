/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork;

import java.util.List;
import java.util.Random;
import kdo.neuralNetwork.data.InOutDataList;
import kdo.neuralNetwork.data.InOutPattern;
import kdo.neuralNetwork.feedforward.Layer;
import kdo.neuralNetwork.optimizer.IOptimizer;

public interface INeuralNetwork {
	/**
	 *
	 * @param rate the learnrate to use while learning
	 */
	void setLearnrate(float rate);

	/**
	 * Calculates the output of this network for the input passed
	 * @param input the input for which to calculate the output. The length has
	 *        to fit to the number of inputs. The input has to include the bias
	 *        as 0th element
	 * @return
	 */
	float[] recall(float[] input);

	/**
	 * Recalls the full set of passed training data
	 * @param patterns the training patterns to recall
	 * @return the summed deltas per output
	 */
	float[] recallAll(List<InOutPattern> patterns);

	/**
	 * Recalls the full set of passed training data
	 * @param patterns the training patterns to recall
	 * @return the root of the mean squared errors
	 */
	float recallAllRMS(List<InOutPattern> patterns);

	/**
	 * Recalls the full set of passed training data and counts the number of miss
	 * classified patterns. Typically used with winner takes all output function.
	 * @param patterns the training patterns to recall
	 * @return the percentage of missclassified patterns
	 */
	float recallAllPercent(List<InOutPattern> patterns);

	/**
	 * Returns the sum of square errors of passed delta values
	 * @param deltas the delta values from recalling a batch of patterns
	 * @return the sum of square errors
	 */
	float getSquareError(float[] deltas);

	/**
	 * Trains the passed batch of patterns a single time
	 * @param batch a list of patterns to train
	 * @return the error in this pattern as mean of all delta squares of output
	 *         and expectedOutput
	 */
	float train(List<InOutPattern> batch);

	/**
	 * Trains the full set of passed training data sequentially
	 * @param data the training patterns to train
	 * @param batchSize the number of data items to batch together
	 * @param maxLoops the number of loops to run through all patterns
	 * @param stopError the loss below which to stop learning
	 * @param verbose print the error every nth epoch (-1 if not at all)
	 * @param rand the random source to use
	 * @return the root of the mean squared errors of the last epoch
	 */
	double trainAll(InOutDataList data, int batchSize, long maxLoops, double stopLoss, int verbose, Random rand);

	/**
	 * Stores this network (including optimizer state) using serialization
	 * @param filename the path and filename to store to
	 */
	void serializeNetwork(String filename);

	/**
	 * @return an array with the number of neurons forming each layer
	 */
	int[] getLayerSizes();

	/**
	 * @return the optimizer component
	 */
	IOptimizer getOptimizer();

	INeuralNetwork setOptimizer(IOptimizer createOptimizer);

	INeuralNetwork addLayer(Layer newLayer);

	int getNoOfLayers();
}