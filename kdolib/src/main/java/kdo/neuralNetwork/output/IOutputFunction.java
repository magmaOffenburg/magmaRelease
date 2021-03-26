/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.output;

/**
 * Represents a possible output function to allow interaction between single
 * outputs
 */
public interface IOutputFunction {
	/**
	 * transforms the original output of the network
	 * @param output the original output
	 * @return the new output as transformed by the output function (does not
	 *         create a new object)
	 */
	float[] output(float[] output);
}
