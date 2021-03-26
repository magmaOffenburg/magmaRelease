/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import magma.agent.decision.behavior.base.KickDistribution;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickWalkBackwardParameters extends KickWalkParameters
{
	public static KickWalkBackwardParameters instance()
	{
		return new KickWalkBackwardParameters(new Vector3D(-0.19, 0.05, 0.00), getParamArray());
	}

	private KickWalkBackwardParameters(Vector3D ballPos, double[] p)
	{
		super(ballPos, p);
	}

	private static double[] getParamArray()
	{
		//		Average utility: 6.902 averaged: 10, at Mon Jul 03 01:35:46 CEST 2017, properties: {
		//		  "ballX": 6.979,
		//		  "ballY": 0.006,
		//		  "angle": 0.01,
		//		  "distance": 6.98,
		//		  "absBallY": 0.077,
		//		  "maxBallHeight": 0.317,
		//		  "supportFoot": 0.0,
		//		  "standing": 1.0,
		//		  "stabilized": 0.0,
		//		  "hitBall": 1.0,
		//		  "localBallX": -0.156,
		//		  "localBallY": -0.048,
		//		  "supportFootX": -0.17,
		//		  "supportFootY": -0.11,
		//		  "supportFootOrientation": 88.169
		//		}
		return new double[] {4, 0, -0.1500, -0.0500, 0, -0.2000, -0.1300, 0.1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5800, -21.4000, -3.2700, 27.2000, -45.2800, 25.8800, 3.7500, 20.4800,
				-42.1300, -11.0500, -23.6200, -17.7600, 62.5300, 5.7100, 45.8300, 6.3500, 3.2200, 2.8300, 0.9500, 5,
				1.4400, 6.4600, 1.0500, 1.0700, 1.4700, 2.9400, 3.2400, 5.7100, 1.6700, 0.2900, -38.3600, 7.1100,
				35.0900, -24.5500, 20.9700, 11.7800, 29.5600, -41.0600, 5.0100, -21.5900, -90.8200, 76.1600, 4.7500,
				39.6100, 5.3100, 0.9300, 2.7100, 6.4900, 2.8900, 5.8900, 3.0600, 0.2900, 5.1200, 6.4800, 2.4900, 1.6000,
				2.0900, 2.9400, 1.9700, -36.3700, -1.9500, 37.2200, -29.6400, 15.7900, 3.1700, 37.7100, -17.4200,
				-7.5000, -19.5200, -104.2700, 11.4400, -9.9100, 38.8700, 6.5600, 0.7100, 5.4600, 5.3100, 1.6900, 1.3700,
				4.3400, 0.4600, 4.6500, 6.1000, 6.3100, 6.1400, 5.7400, 4.3400, 2.4600, -12.3500, -4.0800, -3.4700,
				-62.2600, -7.2200, 7.5600, 56.5100, -18.6800, -2.8100, 19.4200, -103.8700, 24.4700, -15.4200, 29.9800,
				3.3800, 6.5000, 2.8300, 1.4400, 2.2700, 1.8500, 3.0900, 0.8600, 2.6700, 5.8700, 1.5900, 3.1500, 1.7800,
				6.0300};
	}

	public KickDistribution getDistribution()
	{
		return new KickDistribution(
				new double[] {0.0, 0.1, 0.0, 0.1, 0.0, 0.1, 0.1, 0.0, 0.0, 0.0, 0.1, 0.2, 0.0, 0.1, 0.2},
				new double[] {0.1, 0.1, 0.3, 0.2, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
						0.0, 0.1, 0.1});
	}
}
