/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Base class for goal tracking, a mechanism to avoid activation spreading
 */
public interface IBaseGoalTrack {
	/**
	 * @return the sum of activation received directly or indirectly by goals
	 */
	double getSumActivation();

	/**
	 * If the passed activation of goal is higher than any previous activation,
	 * take over the new value
	 * @param i index of the goal for which the activation is set
	 * @param val new value for the activation
	 */
	void setActivation(int i, double val);

	/**
	 * @param i index of the goal of which the activation is returned
	 * @return the activation received directly or indirectly by goal i
	 */
	double getActivation(int i);

	/**
	 * @return the number of goals in the network
	 */
	int getNoOfGoals();

	/**
	 * Updates the activations to the newly calculated activations
	 */
	void setToNewActivation();
}
