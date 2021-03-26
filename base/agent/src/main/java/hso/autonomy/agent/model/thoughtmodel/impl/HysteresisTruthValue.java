/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel.impl;

/**
 * Truth value that switches its truth at different bounds depending on its current truth value
 * @author kdorer
 */
public abstract class HysteresisTruthValue extends TruthValue
{
	/** the bound at which lower values will switch the truth value (including) */
	protected double lowerBound;
	/** the bound at which higher values will switch the truth value (excluding) */
	protected double upperBound;
	/** if true lower values than lowerBound will switch to true, otherwise false */
	private boolean lowIsTrue;
	/** last measurement for the truth value*/
	private double lastMeasurement;

	public HysteresisTruthValue(double lowerBound, double upperBound, boolean lowIsTrue)
	{
		this.upperBound = upperBound;
		this.lowerBound = lowerBound;
		this.lowIsTrue = lowIsTrue;
	}

	protected void setValidity(double value, float now)
	{
		lastMeasurement = value;
		if (lowIsTrue) {
			if (valid) {
				if (value > upperBound) {
					setValidity(false, now);
				}
			} else {
				if (value < lowerBound) {
					setValidity(true, now);
				}
			}
		} else {
			if (!valid) {
				if (value > upperBound) {
					setValidity(true, now);
				}
			} else {
				if (value < lowerBound) {
					setValidity(false, now);
				}
			}
		}
	}

	/**
	 * @return the lastMeasurement
	 */
	public double getLastMeasurement()
	{
		return lastMeasurement;
	}
}
