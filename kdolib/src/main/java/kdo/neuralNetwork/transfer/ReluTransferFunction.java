/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.transfer;

import java.io.Serializable;

/**
 * A ReLU transfer function usually used in deep learning
 */
public class ReluTransferFunction implements ITransferFunction, Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	public float transfer(float x)
	{
		return (x > 0) ? x : 0.0f;
	}

	public float transferDerivative(float x, float fx)
	{
		return (x > 0) ? 1.0f : 0.0f;
	}
}
