/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.timeseries;

import java.io.Serializable;
import java.util.List;

/**
 * Interface for a generic timeseries of values.
 *
 * @author Stefan Glaser
 *
 * @param <D> Data type of the timeseries values.
 */
public interface ITimeSeries<D> extends Serializable {
	/**
	 * Set the interpolator instance used to interpolate data points for unknown timestamps.
	 *
	 * @param interpolator the data interpolator instance
	 */
	void setDataInterpolator(IDataInterpolator<D> interpolator);

	/**
	 * Retrieve the interpolator instance used to interpolate data points for unknown timestamps.
	 *
	 * @return the current data interpolator instance
	 */
	IDataInterpolator<D> getDataInterpolator();

	/**
	 * Interpolate a data point for the given time.<br>
	 * If no valid data interpolator instance is available, this method returns the closest data point available to the
	 * requested time.
	 *
	 * @param time the time for which a data point should be interpolated
	 *
	 * @return an interpolated data point for the given time
	 */
	D interpolateData(double time);

	/**
	 * Interpolate a data point for the given time.<br>
	 * If no valid data interpolator instance is available, this method returns the closest data point available to the
	 * requested time. In case the timeseries has no entries, <code>null</code> is returned.
	 *
	 * @param time the time for which a data point should be interpolated
	 * @param defaultValue the value to return in case the timeseries is empty
	 *
	 * @return an interpolated data point for the given time, or the given <code>defaultValue</code> if the timeseries
	 * is empty
	 */
	D interpolateData(double time, D defaultValue);

	/**
	 * Retrieve the sorted list of timeseries entries.
	 *
	 * @return the list of timeseries entries
	 */
	List<ITSEntry<D>> getEntries();

	/**
	 * Retrieve the most recent data point of this timeseries.
	 *
	 * @return the most recent data point
	 */
	D getMostRecentData();

	/**
	 * Retrieve the most recent data point of this timeseries.
	 *
	 * @param defaultValue the value to return in case the timeseries is empty
	 * @return the most recent data point
	 */
	D getMostRecentData(D defaultValue);

	/**
	 * Check if this timeseries is empty (does not contain any entries).
	 *
	 * @return true if this timeseries has no entries, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Retrieve the size of the timeseries data (the number of entries).
	 *
	 * @return the number of entries in this timeseries
	 */
	int size();

	/**
	 * Add a data point to this timeseries.
	 *
	 * @param time the time associated to the given data
	 * @param value the value to store
	 *
	 * @return the timeseries entry generated for the given time and data
	 */
	ITSEntry<D> add(double time, D value);

	/**
	 * Remove a data point from this timeseries.
	 *
	 * @param time the time of the entry to remove
	 *
	 * @return the timeseries entry that got removed or null, if no such entry exists
	 */
	ITSEntry<D> remove(double time);

	/**
	 * Remove an entry from this timeseries.
	 *
	 * @param entry the entry to remove
	 *
	 * @return success
	 */
	boolean remove(ITSEntry<D> entry);

	/** Clear timeseries (remove all entries). */
	void clear();

	/**
	 * Interface for a timeseries entry, combining a timestamp and a value.
	 *
	 * @author Stefan Glaser
	 *
	 * @param <D> Data type of the timeseries values.
	 */
	interface ITSEntry<D> {
		/**
		 * Retrieve the time of to this entry.
		 *
		 * @return the time
		 */
		double getTime();

		/**
		 * Retrieve the data associated with the timestamp.
		 *
		 * @return the data
		 */
		D getData();
	}
}
