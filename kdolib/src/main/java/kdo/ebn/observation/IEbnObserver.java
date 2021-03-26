/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.observation;

/**
 * Interface which has to be implemented to observe the ebn
 * @author Thomas Rinklin
 *
 */
public interface IEbnObserver {
	/**
	 * is called if values has changed
	 */
	void valuesChanged();

	/**
	 * is called if the structure of the ebn has changed
	 */
	void structureChanged();
}
