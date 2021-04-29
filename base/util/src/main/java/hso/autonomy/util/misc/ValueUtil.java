/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.misc;

/**
 *
 * @author kdorer
 */
public class ValueUtil
{
	/**
	 * Limits the passed value to the range passed. So the returned value will be
	 * in the interval [min, x, max]. If min > max then value is unchanged.
	 * @param value the value to limit
	 * @param min the smallest value value can have
	 * @param max the highest value value can have
	 * @return the passed value's value limited by the passed limits
	 */
	public static int limitValue(int value, int min, int max)
	{
		if (max < min) {
			return value;
		}

		if (value > max) {
			return max;
		}
		if (value < min) {
			return min;
		}
		return value;
	}

	/**
	 * Limits the passed value to the range passed. So the returned value will be
	 * in the interval [min, x, max]. If min > max then value is unchanged.
	 * @param value the value to limit
	 * @param min the smallest value value can have
	 * @param max the highest value value can have
	 * @return the passed value's value limited by the passed limits
	 */
	public static float limitValue(float value, float min, float max)
	{
		if (max < min) {
			return value;
		}

		if (value > max) {
			return max;
		}
		if (value < min) {
			return min;
		}
		return value;
	}

	/**
	 * Limits the passed value to the range passed. So the returned value will be
	 * in the interval [min, x, max]. If min > max then value is unchanged.
	 * @param value the value to limit
	 * @param min the smallest value value can have
	 * @param max the highest value value can have
	 * @return the passed value's value limited by the passed limits
	 */
	public static double limitValue(double value, double min, double max)
	{
		if (max < min) {
			return value;
		}

		if (value > max) {
			return max;
		}
		if (value < min) {
			return min;
		}

		return value;
	}

	/**
	 * Limits the passed value's absolute value by the limit passed. So if limit
	 * is 20 the returned value will be in the interval [-20, x, 20].
	 * @param value the value to limit
	 * @param limit positive upper limit of the interval
	 * @return the passed value's absolute value limited by the passed limit
	 */
	public static double limitAbs(double value, double limit)
	{
		if (value > limit) {
			return limit;
		}
		if (value < -limit) {
			return -limit;
		}
		return value;
	}

	/**
	 * Limits the passed value's absolute value by the limit passed. So if limit
	 * is 20 the returned value will be in the interval [-20, x, 20].
	 * @param value the value to limit
	 * @param limit positive upper limit of the interval
	 * @return the passed value's absolute value limited by the passed limit
	 */
	public static float limitAbs(float value, float limit)
	{
		if (value > limit) {
			return limit;
		}
		if (value < -limit) {
			return -limit;
		}
		return value;
	}

	/**
	 * Adjusts the passed current value by valueChange and makes sure that
	 * desired value is not exceeded.
	 * @param currentValue the value to adjust
	 * @param desiredValue the value we want to reach
	 * @param valueChange the delta to be added to current value
	 * @return the new value of currentValue
	 */
	public static double adjustValue(double currentValue, double desiredValue, double valueChange)
	{
		return adjustValue(currentValue, desiredValue, valueChange, valueChange);
	}

	/**
	 * Adjusts the passed current value by valueChange and makes sure that
	 * desired value is not exceeded.
	 * @param currentValue the value to adjust
	 * @param desiredValue the value we want to reach
	 * @param valueChangeAdd the delta to be added to current value
	 * @param valueChangeSubtract the delta to be subtracted from current value
	 * @return the new value of currentValue
	 */
	public static double adjustValue(
			double currentValue, double desiredValue, double valueChangeAdd, double valueChangeSubtract)
	{
		return currentValue + getValueAdjustment(currentValue, desiredValue, valueChangeAdd, valueChangeSubtract);
	}

	/**
	 * Returns the effective value change to adjust currentValue do desiredValue.
	 * @param currentValue the value to adjust
	 * @param desiredValue the value we want to reach
	 * @param valueChangeAdd the delta to be added to current value
	 * @param valueChangeSubtract the delta to be subtracted from current value
	 * @return the new value to add to currentValue
	 */
	public static double getValueAdjustment(
			double currentValue, double desiredValue, double valueChangeAdd, double valueChangeSubtract)
	{
		if (currentValue <= desiredValue) {
			if (currentValue + valueChangeAdd > desiredValue) {
				return desiredValue - currentValue;
			}
			return valueChangeAdd;
		}

		if (currentValue - valueChangeSubtract < desiredValue) {
			return desiredValue - currentValue;
		}
		return -valueChangeSubtract;
	}

	/**
	 * @param values the values to add
	 * @param maxValue the value to which to scale if sum is bigger
	 * @return a factor that ensures that the sum of the abs values of values is
	 *         at most maxValue
	 */
	public static double getScale(double[] values, double maxValue)
	{
		double sum = 0;
		for (double current : values) {
			sum += Math.abs(current);
		}
		if (sum < maxValue) {
			return 1.0;
		}
		return maxValue / sum;
	}

	/**
	 * Checks if the given value is inside the range defined by (min - epsilon)
	 * and (max + epsilon).
	 *
	 * @param value The value to check
	 * @param min The lower bound of the valid range
	 * @param max The upper bound of the valid range
	 * @param epsilon The allowed deviation from the defined range
	 *
	 * @return true if the value is inside the range, false otherwise
	 */
	public static boolean isValueInRange(double value, double min, double max, double epsilon)
	{
		return value >= min - epsilon && value <= max + epsilon;
	}

	/**
	 * Checks if the given value is inside the range defined by (min - epsilon)
	 * and (max + epsilon).
	 *
	 * @param value The value to check
	 * @param min The lower bound of the valid range
	 * @param max The upper bound of the valid range
	 * @param epsilon The allowed deviation from the defined range
	 *
	 * @return true if the value is inside the range, false otherwise
	 */
	public static boolean isValueInRange(float value, float min, float max, float epsilon)
	{
		return value >= min - epsilon && value <= max + epsilon;
	}

	/**
	 * Checks if the given value is inside the range defined by min and max.
	 *
	 * @param value The value to check
	 * @param min The lower bound of the valid range
	 * @param max The upper bound of the valid range
	 *
	 * @return true if the value is inside the range, false otherwise
	 */
	public static boolean isValueInRange(double value, double min, double max)
	{
		return value >= min && value <= max;
	}

	/**
	 * Checks if the given value is inside the range defined by min and max.
	 *
	 * @param value The value to check
	 * @param min The lower bound of the valid range
	 * @param max The upper bound of the valid range
	 *
	 * @return true if the value is inside the range, false otherwise
	 */
	public static boolean isValueInRange(float value, float min, float max)
	{
		return value >= min && value <= max;
	}

	public static double addToAverage(float oldAverage, float newValue, int count)
	{
		return oldAverage + ((newValue - oldAverage) / count);
	}

	public static double addToAverage(double oldAverage, double newValue, int count)
	{
		return oldAverage + ((newValue - oldAverage) / count);
	}

	/**
	 * Maps the passed value from the first value range to the second
	 * @param value the value to remap
	 * @param low1 lower bound of the values value space
	 * @param high1 upper bound of the values value space
	 * @param low2 lower bound of the new value space
	 * @param high2 upper bound of the new value space
	 * @return the value mapped to its new interval
	 */
	public static double remap(double value, double low1, double high1, double low2, double high2)
	{
		if (high1 - low1 == 0) {
			return 0; // avoid NaN
		}
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}

	/**
	 * Clips all elements of the passed array to the passed min and max value area and converts them to double.
	 * @param values the values clip in value range
	 * @param min lowest value the values may have
	 * @param max highest value the values may have
	 * @return a new double array that contains a clipped version of the passed float values
	 */
	public static double[] clip(float[] values, float min, float max)
	{
		double[] result = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = limitValue(values[i], min, max);
		}
		return result;
	}
}
