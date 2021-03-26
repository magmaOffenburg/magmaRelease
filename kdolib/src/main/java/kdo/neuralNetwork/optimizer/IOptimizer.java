/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.optimizer;

import java.io.Serializable;

public interface IOptimizer extends Serializable {
	/**
	 * Called before optimization starts to allow the optimizer to initialize
	 * additional variables.
	 * @param layerSizes the number of neurons of each layer of the network.
	 * @return the optimizer itself to allow chaining
	 */
	IOptimizer prepare(int[] layerSizes);

	/**
	 * Call this method to apply a single parameter value update
	 * @param parameters an array containing the parameter (to allow write
	 *        access)
	 * @param gradient the calculated, negative gradient
	 * @param layerIndex the layer in which the parameter resides
	 * @param inIndex the index input neuron
	 * @param outIndex the index of the output neuron and the parameter to adjust
	 */
	void apply(float[] parameters, float gradient, int layerIndex, int inIndex, int outIndex);

	/**
	 * Adjust the learnrate during runtime.
	 * @param rate
	 */
	void setLearnrate(float rate);

	/**
	 * @return the name of the optimizer
	 */
	String getName();
}
