/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.neuralNetwork.transfer;

import java.io.Serializable;

/**
 * Does not change the output
 * @author kdorer
 */
public class IdentityTransferFunction implements ITransferFunction, Serializable
{
	@Override
	public float transfer(float value)
	{
		return value;
	}

	@Override
	public float transferDerivative(float x, float fx)
	{
		return fx;
	}
}
