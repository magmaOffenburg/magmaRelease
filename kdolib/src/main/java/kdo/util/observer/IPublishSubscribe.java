/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.util.observer;

public interface IPublishSubscribe<T> {
	/**
	 * Adds an observer to the list of observers if not already in the list
	 * @param observer the observer that wants to be informed
	 */
	void attach(IObserver<T> observer);

	/**
	 * Removes an observer from the list of observers
	 * @param observer the observer that wants to be removed
	 * @return true if the observer has been in the list and was removed
	 */
	boolean detach(IObserver<T> observer);

	/**
	 * Removes all observers from the list of observers
	 */
	void detachAll();

	/**
	 * Called to inform observer about a change in state
	 * @param content the object that contains the changed information
	 */
	void onStateChange(T content);
}