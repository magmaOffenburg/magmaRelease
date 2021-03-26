/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn;

/**
 * Interface to access the state of the world as propositions
 */
public interface IResourceBelief extends IEBNPerception {
	/**
	 * @return the amount of resource units currently available
	 */
	int getAmountAvailable();
}
