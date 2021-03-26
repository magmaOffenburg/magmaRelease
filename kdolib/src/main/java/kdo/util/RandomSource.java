/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.util;

import java.util.Random;

/**
 * This class is just needed to have an instance of IRandomSource, because class
 * Random is not abstracted by an interface
 * @author klaus
 */
public class RandomSource extends Random implements IRandomSource
{
	private static final long serialVersionUID = 1L;

	public RandomSource()
	{
		super();
	}

	public RandomSource(long seed)
	{
		super(seed);
	}
}
