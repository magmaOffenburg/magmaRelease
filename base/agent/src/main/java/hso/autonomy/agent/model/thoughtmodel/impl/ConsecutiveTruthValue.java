/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;

/**
 * Base class for truth values that require a consecutive number of true / false observations before they switch their
 * values
 * @author kdorer
 *
 */
public abstract class ConsecutiveTruthValue extends TruthValue
{
	/** the number of consecutive true detections before valid is switched to true */
	private int consecutiveTrueValues;

	/** the number of consecutive false detections before valid is switched to false */
	private int consecutiveFalseValues;

	/** the number of times the opposite detection is detected in a row */
	private int consecutiveCount;

	public ConsecutiveTruthValue()
	{
		this(1, 1);
	}

	public ConsecutiveTruthValue(int consecutiveTrueValues, int consecutiveFalseValues)
	{
		this.consecutiveTrueValues = consecutiveTrueValues;
		this.consecutiveFalseValues = consecutiveFalseValues;
		consecutiveCount = consecutiveTrueValues;
	}

	@Override
	public abstract void update(IThoughtModel thoughtModel);

	@Override
	protected void setValidity(boolean newValidity, float now)
	{
		if (newValidity == true) {
			if (!valid) {
				consecutiveCount++;
				if (consecutiveCount >= consecutiveTrueValues) {
					validSince = now;
					valid = true;
					consecutiveCount = 0;
				}
			} else {
				consecutiveCount = 0;
			}
		} else {
			if (valid) {
				consecutiveCount++;
				if (consecutiveCount >= consecutiveFalseValues) {
					invalidSince = now;
					valid = false;
					consecutiveCount = 0;
				}
			} else {
				consecutiveCount = 0;
			}
		}
	}
}