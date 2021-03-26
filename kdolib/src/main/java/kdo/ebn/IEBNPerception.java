/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Interface to access the state of the world as propositions
 */
public interface IEBNPerception {
	/**
	 * Returns the truth value associated with this belief as a value between 0.0
	 * (false) and 1.0 (true)
	 *
	 * @return the truth value associated with this belief as a value between 0.0
	 *         (false) and 1.0 (true)
	 */
	float getTruthValue();

	/**
	 * @return the name of the belief
	 */
	String getName();
}
