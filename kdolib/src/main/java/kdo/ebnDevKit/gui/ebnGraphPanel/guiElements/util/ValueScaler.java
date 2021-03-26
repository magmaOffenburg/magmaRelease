/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package kdo.ebnDevKit.gui.ebnGraphPanel.guiElements.util;

import java.util.LinkedList;

/**
 * this class scales values to a range between 0 and 1. to reach this, it
 * calculates an internal factor. to reach this, it stores information about the
 * highest value in the last few rounds. a round means a usage cycle of this
 * scaler. the end of round must be marked by invoking markRound(). note that
 * all negative values return 0 as scaled value and will be used to calculate
 * the internal factor.
 * @author Thomas Rinklin
 */
public class ValueScaler
{
	private final int scaleRounds;

	private final LinkedList<Double> maxPerRound;

	/** current factor */
	private double factor = Double.MAX_VALUE;

	/**
	 * constructor: scaleRounds = 100
	 */
	public ValueScaler()
	{
		this(100);
	}

	/**
	 * constructor
	 * @param scaleRounds amount of rounds, which the object uses to calculation
	 *        the factor
	 */
	public ValueScaler(int scaleRounds)
	{
		this.scaleRounds = scaleRounds;
		maxPerRound = new LinkedList<Double>();
		maxPerRound.add(Double.MIN_VALUE);
	}

	/**
	 * scales a value to a range between 0 and 1 and use the value to calculate
	 * the internal factor.
	 * @param value the value to scale
	 * @return the scaled value
	 */
	public double scaleValue(double value)
	{
		return scaleValue(value, true);
	}

	/**
	 * scales a value to a range between 0 and 1
	 * @param value value to scale
	 * @param useToCalcFactor use this value to calculate the internal factor
	 * @return scaled value
	 */
	public double scaleValue(double value, boolean useToCalcFactor)
	{
		if (useToCalcFactor)
			updateRoundList(value);
		if (value < 0)
			return 0;
		if (value * factor > 1)
			return 1;
		return value * factor;
	}

	private void updateRoundList(double value)
	{
		if (value > maxPerRound.getFirst()) {
			maxPerRound.set(0, value);
		}
	}

	/**
	 * marks a round of scale usage
	 */
	public void markRound()
	{
		// calculate maximal value of the last (scaleRounds) rounds
		double maxVal = 0;
		for (Double maxValRound : maxPerRound) {
			maxVal = Math.max(maxVal, maxValRound);
		}

		// rescale
		// System.out.print(".");
		double scaledMaxVal = factor * maxVal;
		if (scaledMaxVal < 0.8 || scaledMaxVal > 1) {
			// System.out.print(factor);
			factor = 0.98 / maxVal;
			// System.out.println(" -> " + factor);
		}

		if (maxPerRound.size() >= scaleRounds)
			maxPerRound.removeLast();
		maxPerRound.addFirst(Double.MIN_VALUE);
	}
}
