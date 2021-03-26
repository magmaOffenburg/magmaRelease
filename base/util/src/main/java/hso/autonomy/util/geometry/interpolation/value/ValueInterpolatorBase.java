/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.interpolation.value;

import hso.autonomy.util.geometry.interpolation.IValueInterpolator;
import hso.autonomy.util.geometry.interpolation.progress.ProgressFunction;

/**
 * @author Stefan Glaser
 */
public abstract class ValueInterpolatorBase implements IValueInterpolator
{
	/** interpolation progress function */
	public ProgressFunction progress;

	public ValueInterpolatorBase(ProgressFunction progress)
	{
		this.progress = progress;
	}

	@Override
	public double interpolate(double initial, double target, float t)
	{
		float p = progress.getProgress(t);

		if (p >= 1) {
			return target;
		} else if (p <= 0) {
			return initial;
		}

		return calculateInterpolationValue(initial, target, p);
	}

	protected abstract double calculateInterpolationValue(double initial, double target, float t);
}
