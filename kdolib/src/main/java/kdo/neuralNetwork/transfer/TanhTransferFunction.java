/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.transfer;

import java.io.Serializable;

/**
 * A tanh transfer function sometimes used in deep learning
 */
public class TanhTransferFunction implements ITransferFunction, Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	public float transfer(float x)
	{
		return (float) Math.tanh(x);
	}

	public float transferDerivative(float x, float fx)
	{
		return 1.0f - fx * fx;
	}
}
