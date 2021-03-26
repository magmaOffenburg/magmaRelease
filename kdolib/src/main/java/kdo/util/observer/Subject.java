/*
 * Copyright (c) 2007 Klaus Dorer
 * Hochschule Offenburg
 */
package kdo.util.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Observer notification classes. This implementation uses a
 * simplified design. Model classes would not inherit from this class but use a
 * mediator. The name was chosen to comply with Gamma et all's book, but is a
 * rather general name
 */
public class Subject<T> implements IPublishSubscribe<T>
{
	/** the list of observers that are notified */
	private final List<IObserver<T>> observers;

	/**
	 * Default constructor creating the observer list
	 */
	public Subject()
	{
		super();
		this.observers = new ArrayList<IObserver<T>>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * magma.util.observer.IPublishSubscribe#attach(magma.util.observer.IObserver
	 * )
	 */
	public void attach(IObserver<T> observer)
	{
		synchronized (observers)
		{
			if (!observers.contains(observer)) {
				observers.add(observer);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * magma.util.observer.IPublishSubscribe#detach(magma.util.observer.IObserver
	 * )
	 */
	public boolean detach(IObserver<T> observer)
	{
		synchronized (observers)
		{
			return observers.remove(observer);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see magma.util.observer.IPublishSubscribe#detachAll()
	 */
	public void detachAll()
	{
		synchronized (observers)
		{
			observers.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see magma.util.observer.IPublishSubscribe#onStateChange()
	 */
	public void onStateChange(T content)
	{
		synchronized (observers)
		{
			for (IObserver<T> observer : observers) {
				observer.update(content);
			}
		}
	}
}
