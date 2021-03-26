/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Observer notification classes. This implementation uses a
 * simplified design. Model classes would not inherit from this class but use a
 * mediator. The name was chosen to comply with Gamma et all's book, but is a
 * rather general name
 *
 * @param <T> Data type transported in updates
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
		this.observers = new ArrayList<>();
	}

	@Override
	public void attach(IObserver<T> observer)
	{
		if (observer == null) {
			throw new RuntimeException("Can not add null observer");
		}
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	@Override
	public boolean detach(IObserver<T> observer)
	{
		return observers.remove(observer);
	}

	@Override
	public void detachAll()
	{
		observers.clear();
	}

	@Override
	public void onStateChange(T content)
	{
		for (IObserver<T> observer : observers) {
			observer.update(content);
		}
	}
}
