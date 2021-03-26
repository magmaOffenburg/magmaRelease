/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel.impl;

import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.agent.model.thoughtmodel.ITruthValue;

public abstract class TruthValue implements ITruthValue
{
	/** true if this detection is valid */
	protected boolean valid;

	/** time since this detection is valid. Use only if valid is true */
	protected float validSince;

	/** time since this detection is invalid. Use only if valid is false */
	protected float invalidSince;

	public TruthValue()
	{
		valid = false;
		validSince = 0;
		invalidSince = 0;
	}

	@Override
	public abstract void update(IThoughtModel thoughtModel);

	@Override
	public boolean isValid()
	{
		return valid;
	}

	public float getValidSince()
	{
		return validSince;
	}

	public float getValidityTime(float now)
	{
		return now - validSince;
	}

	public float getInvalidSince()
	{
		return invalidSince;
	}

	public float getInValidityTime(float now)
	{
		return now - invalidSince;
	}

	protected void setValidity(boolean newValidity, float now)
	{
		if (newValidity == true) {
			if (!valid) {
				validSince = now;
				valid = true;
			}
		} else {
			if (valid) {
				invalidSince = now;
				valid = false;
			}
		}
	}
}
