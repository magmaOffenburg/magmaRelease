/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry.interpolation.value;

import hso.autonomy.util.geometry.interpolation.progress.LinearProgress;
import hso.autonomy.util.misc.ValueUtil;

/**
 * @author Stefan Glaser
 */
public class AbsMaxSineLinearValueInterpolator extends SineLinearValueInterpolator
{
	public double absMax;

	public AbsMaxSineLinearValueInterpolator(double absMax)
	{
		super(new LinearProgress(), 0);
		this.absMax = absMax;
	}

	@Override
	protected double calculateInterpolationValue(double initial, double target, float t)
	{
		double lin = (target - initial) * t;
		double sin = Math.sin(t * Math.PI) * amplitude;
		sin = ValueUtil.limitAbs(sin, absMax);

		return initial + lin + sin;
	}
}
