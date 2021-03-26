/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Interface to access all behaviors of the agent
 */
public interface IEBNAction {
	/**
	 * Called to perform the behavior
	 */
	void perform();

	/**
	 * Retrieve the behavior name
	 *
	 * @return Behavior name
	 */
	String getName();

	/**
	 * Check if this behavior is finished performing
	 *
	 * @return True if finished, false if not
	 */
	boolean isFinished();

	/**
	 * @return the intensity with which to perform the behavior.
	 */
	float getIntensity();

	/**
	 * @param intensity the intensity with which to perform the behavior. Values
	 *        are free to use, but are typically in the range of [0..1]
	 */
	void setIntensity(float intensity);
}
