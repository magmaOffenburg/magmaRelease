/*
 * Copyright (c) 2008 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.transfer;

import java.io.Serializable;

/**
 * Step transfer returns 1.0 for all values bigger than a specified input
 */
public class StepTransferFunction implements ITransferFunction, Serializable
{
	/** */
	private static final long serialVersionUID = 1L;

	/** the value at which the function switches from 0 to 1 */
	private final float step;

	/**
	 * @param step the value at which the function switches from 0 to 1
	 */
	public StepTransferFunction(float step)
	{
		super();
		this.step = step;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kdo.neuralNetwork.transfer.ITransferFunction#transfer(float)
	 */
	@Override
	public float transfer(float value)
	{
		if (value >= step) {
			return 1.0f;
		} else {
			return 0.0f;
		}
	}

	@Override
	public float transferDerivative(float x, float fx)
	{
		// to seamlessly work with perceptron update rule
		return 1;
	}
}
