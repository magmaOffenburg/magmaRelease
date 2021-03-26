/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.util.observer;

/**
 * Interface to guarantee independence of Observer and Subject
 */
public interface IObserver<T> {
	/**
	 * Called to notify an observer about a state change
	 * @param content reference to the object containing changed state
	 */
	void update(T content);
}
