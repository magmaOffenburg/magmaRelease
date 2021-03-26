/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.transfer;

import java.io.Serializable;

/**
 * A sigmoidal transfer function usually used in backpropagation
 */
public class SigmoidTransferFunction implements ITransferFunction, Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	public float transfer(float value)
	{
		return (float) (1.0 / (1.0 + Math.exp(-value)));
	}

	public float transferDerivative(float x, float fx)
	{
		return fx * (1.0f - fx);
	}
}
