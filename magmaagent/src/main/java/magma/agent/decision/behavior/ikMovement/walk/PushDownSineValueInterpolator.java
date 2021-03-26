/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.ikMovement.walk;

import hso.autonomy.util.geometry.interpolation.progress.LinearProgress;
import hso.autonomy.util.geometry.interpolation.progress.ProgressFunction;
import hso.autonomy.util.geometry.interpolation.value.ValueInterpolatorBase;

/**
 * @author Stefan Glaser
 */
public class PushDownSineValueInterpolator extends ValueInterpolatorBase
{
	public double amplitude;

	public PushDownSineValueInterpolator()
	{
		this(new LinearProgress(), 0);
	}

	public PushDownSineValueInterpolator(ProgressFunction progress, double amplitude)
	{
		super(progress);

		this.amplitude = amplitude;
	}

	@Override
	protected double calculateInterpolationValue(double initial, double target, float t)
	{
		if (t > 0.98) {
			return target;
		}

		double sin = Math.sin(t * Math.PI / 2) * amplitude;

		return initial + sin;
	}
}
