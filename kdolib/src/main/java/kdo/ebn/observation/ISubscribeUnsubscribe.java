/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.observation;

/**
 * Interface that provides methods to attach and detach observers from the
 * observer-list
 * @author Thomas Rinklin
 */
public interface ISubscribeUnsubscribe {
	/**
	 * Adds an observer to the list of observers if not already in the list
	 * @param observer the observer that wants to be informed
	 */
	void attach(IEbnObserver observer);

	/**
	 * Removes an observer from the list of observers if not already in the list
	 * @param observer the observer that wants to be informed no longer
	 * @return true if the observer was in the list, false if not
	 */
	boolean detach(IEbnObserver observer);
}
