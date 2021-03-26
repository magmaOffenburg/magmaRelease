/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebn.observation;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that provides observation functionality for th ebn
 * @author Thomas Rinklin
 *
 */
public class EbnSubject implements ISubscribeUnsubscribe
{
	/** the list of observers that are notified */
	private final List<IEbnObserver> observers;

	/**
	 * Default constructor creating the observer list
	 */
	public EbnSubject()
	{
		super();
		this.observers = new ArrayList<IEbnObserver>();
	}

	@Override
	public void attach(IEbnObserver observer)
	{
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public boolean detach(IEbnObserver observer)
	{
		return observers.remove(observer);
	}

	/**
	 * Removes all observers from the list of observers
	 */
	public void detachAll()
	{
		observers.clear();
	}

	/**
	 * informs all observers that values has changed
	 */
	public void onValueChange()
	{
		for (IEbnObserver observer : observers) {
			observer.valuesChanged();
		}
	}

	/**
	 * informs all observers that structure has changed
	 */
	public void onStructureChange()
	{
		for (IEbnObserver observer : observers) {
			observer.structureChanged();
		}
	}
}
