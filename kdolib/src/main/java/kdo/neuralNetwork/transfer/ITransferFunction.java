/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.transfer;

/**
 * Represents all possible transfer or activation functions
 */
public interface ITransferFunction {
	/**
	 * Transfers the passed value to an activation of a neuron
	 * @param value the net activation to transfer
	 * @return the output of a neuron
	 */
	float transfer(float value);

	/**
	 * @param x the activation that was input to the activation function
	 * @param fx the function value (output) of the activation function
	 * @return the first derivative of the transfer function at the specified
	 *         value
	 */
	float transferDerivative(float x, float fx);
}
