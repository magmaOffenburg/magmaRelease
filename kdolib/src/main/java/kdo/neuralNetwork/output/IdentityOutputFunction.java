/*
 * Copyright (c) 2012 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.neuralNetwork.output;

import java.io.Serializable;

/**
 * Does not change the output
 * @author kdorer
 */
public class IdentityOutputFunction implements IOutputFunction, Serializable
{
	@Override
	public float[] output(float[] output)
	{
		return output;
	}
}
