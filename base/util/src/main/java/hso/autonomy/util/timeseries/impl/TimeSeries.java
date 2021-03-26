/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timeseries.impl;

import hso.autonomy.util.timeseries.IDataInterpolator;
import hso.autonomy.util.timeseries.ITimeSeries;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for representing a generic timeseries.
 *
 * @author Stefan Glaser
 *
 * @param <D> The data type of the timeseries values.
 */
public class TimeSeries<D> implements ITimeSeries<D>
{
	/** The timeseries entires. */
	private LinkedList<ITSEntry<D>> entries;

	/** The maximum number of entries to store in this timeseries. */
	private int maxEntries;

	/** The data interpolator instance. */
	private IDataInterpolator<D> dataInterpolator;

	/**
	 * Default constructor for an infinite timeseries (no limitation of timeseries entries).
	 */
	public TimeSeries()
	{
		this(-1, null);
	}

	/**
	 * @param maxEntries the maximum number of entries allowed in this timeseries (<= 0 for infilite)
	 * @param interpolator the data interpolator instance
	 */
	public TimeSeries(int maxEntries, IDataInterpolator<D> interpolator)
	{
		this.entries = new LinkedList<ITimeSeries.ITSEntry<D>>();
		this.maxEntries = maxEntries;
		this.dataInterpolator = interpolator;
	}

	@Override
	public void setDataInterpolator(IDataInterpolator<D> interpolator)
	{
		this.dataInterpolator = interpolator;
	}

	@Override
	public IDataInterpolator<D> getDataInterpolator()
	{
		return dataInterpolator;
	}

	@Override
	public D interpolateData(double time)
	{
		return interpolateData(time, null);
	}

	@Override
	public synchronized D interpolateData(double time, D defaultValue)
	{
		if (entries.isEmpty()) {
			return defaultValue;
		}

		if (entries.size() == 1 || time < entries.getFirst().getTime()) {
			// only one entry, or requested time is before the first entry in our list, thus return oldest entry
			return entries.getFirst().getData();
		} else if (time > entries.getLast().getTime()) {
			// requested time is after the latest entry in our list, thus return most recent entry
			return entries.getLast().getData();
		}

		// find surrounding entries to the given time
		ITSEntry<D> entryBefore = entries.getLast();
		ITSEntry<D> entryAfter = entries.getLast();

		Iterator<ITSEntry<D>> iterator = entries.descendingIterator();
		while (iterator.hasNext()) {
			entryAfter = entryBefore;
			entryBefore = iterator.next();

			if (entryBefore.getTime() < time) {
				break;
			}
		}

		// try to interpolate the data value, or return closest entry with respect to time
		if (dataInterpolator != null) {
			double ratio = (time - entryBefore.getTime()) / (entryAfter.getTime() - entryBefore.getTime());
			return dataInterpolator.interpolateData(entryBefore.getData(), entryAfter.getData(), ratio);
		} else if (Math.abs(time - entryBefore.getTime()) < Math.abs(time - entryAfter.getTime())) {
			return entryBefore.getData();
		} else {
			return entryAfter.getData();
		}
	}

	@Override
	public List<ITSEntry<D>> getEntries()
	{
		return entries;
	}

	@Override
	public D getMostRecentData()
	{
		return getMostRecentData(null);
	}

	@Override
	public D getMostRecentData(D defaultValue)
	{
		if (entries.isEmpty()) {
			return defaultValue;
		}

		return entries.getLast().getData();
	}

	@Override
	public boolean isEmpty()
	{
		return entries.isEmpty();
	}

	@Override
	public int size()
	{
		return entries.size();
	}

	@Override
	public synchronized ITSEntry<D> add(double time, D value)
	{
		ITSEntry<D> newEntry = new TSEntry(time, value);

		if (entries.isEmpty() || entries.get(entries.size() - 1).getTime() < time) {
			// add entry to list end
			entries.add(newEntry);
		} else if (entries.get(0).getTime() > time) {
			// add entry to list start
			entries.add(0, newEntry);
		} else {
			// insert element into list
			int idx = entries.size() - 1;
			while (idx >= 0 && entries.get(idx).getTime() > time) {
				idx--;
			}
			idx++;

			entries.add(idx, newEntry);
		}

		// limit number of entries
		if (maxEntries > 0) {
			while (entries.size() > maxEntries) {
				entries.remove(0);
			}
		}

		return newEntry;
	}

	@Override
	public ITSEntry<D> remove(double time)
	{
		for (int idx = 0; idx < entries.size(); idx++) {
			if (entries.get(idx).getTime() == time) {
				return entries.remove(idx);
			}
		}

		return null;
	}

	@Override
	public boolean remove(ITSEntry<D> entry)
	{
		return entries.remove(entry);
	}

	@Override
	public void clear()
	{
		entries.clear();
	}

	/**
	 * Class representing a generic timeseries entry.
	 *
	 * @author Stefan Glaser
	 *
	 * @param <D> Data type of the entry value.
	 */
	class TSEntry implements ITSEntry<D>
	{
		/** The time of the timeseries entry. */
		public final double time;

		/** The value of the timeseries entry. */
		public final D value;

		public TSEntry(double time, D value)
		{
			this.time = time;
			this.value = value;
		}

		@Override
		public double getTime()
		{
			return time;
		}

		@Override
		public D getData()
		{
			return value;
		}
	}
}
