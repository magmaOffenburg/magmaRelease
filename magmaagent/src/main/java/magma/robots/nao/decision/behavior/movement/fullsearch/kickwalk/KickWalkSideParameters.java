/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickWalkSideParameters extends KickWalkParameters
{
	public static KickWalkSideParameters instance()
	{
		return new KickWalkSideParameters(new Vector3D(-0.18, 0.00, 0.00), getParamArray());
	}

	public KickWalkSideParameters(Vector3D ballPos, double[] p)
	{
		super(ballPos, p);
	}

	private static double[] getParamArray()
	{
		// excellent sidekick
		// Average utility: 5.144 averaged: 10 [ 5.144 -0.453 5.144 5.144 0.129
		// 0.000 -0.084 0.117 -82.741 ]
		return new double[] {4, 0, -0.1800, 0, 0, 0.1000, 0.2000, 0.0800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 2.4800, -28.9900, 16.4300, -44.2600, -57.3400, 6.1700, 13.3700, 0, -18.5800,
				-17.6700, 71.9600, -69.9000, 42.3500, 9.9100, 0, 1.6000, 6.7300, 4.0800, 2.8600, 3.7800, 5.5400, 7,
				3.4900, 4.5100, 3.6300, 0.2400, 4.1700, 6.2100, 7, 0.6800, -32.4900, 3.1500, 7.5100, -29.5500, 21.0800,
				-4.6700, 0, -19.3200, -16.4700, 57.6100, -96.4900, -35.8000, 2.3700, 0, 0.8700, 1.0800, 4.0900, 2.1900,
				2.0100, 0.0700, 7, 6.6800, 5.2300, 5.5000, 0.0700, 6.5000, 3.2900, 7, 0.2000, -4.0600, -2.1700,
				-25.5600, -18.8800, 28.1900, 8.3100, 0, -22.7100, -13.0600, 59.8300, -53.3400, -31.8700, -2.1300, 0,
				3.4000, 3.1400, 5.2700, 1.0200, 2.7400, 2.8500, 7, 3.4300, 2.4800, 1.9700, 2.6000, 6.7300, 6.1600, 7,
				4.2800, -16.9700, -10.6000, 33, -56.7200, -2.0800, -12.9700, 0, -7.2000, 17.5300, -4.6500, -110.4200,
				-42.8800, 15.3400, 0, 5.7100, 5.0600, 3.4000, 2.5600, 5.9800, 5, 7, 5.9500, 6.9700, 5.5200, 1.9200,
				7.0900, 5.4600, 7};
	}
}
