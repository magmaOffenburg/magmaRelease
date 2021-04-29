/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.reinforcement;

/**
 * Interface for static and dynamic learning rate adaptation
 * @author kdorer
 */
public interface ILearnrateStrategy {
	/**
	 * @param explorationCount how often a state/action has been explored
	 * @return the current learning rate
	 */
	float getAlpha(int explorationCount);
}
