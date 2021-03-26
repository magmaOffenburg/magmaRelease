/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.util;

import java.util.Random;

/**
 * @author klaus
 *
 */
public interface IRandomSource {
	/**
	 * @see Random#nextInt(int)
	 */
	int nextInt(int n);

	/**
	 * @see Random#nextFloat()
	 */
	float nextFloat();

	/**
	 * @see Random#nextDouble()
	 */
	double nextDouble();

	/**
	 * @see Random#nextBoolean()
	 */
	boolean nextBoolean();

	/**
	 * @see Random#nextGaussian()
	 */
	double nextGaussian();
}
