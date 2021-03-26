/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.decision.behavior.movement.kickwalk;

import magma.agent.decision.behavior.base.KickDistribution;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkParameters;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickWalkBackwardParametersToe extends KickWalkParameters
{
	public static KickWalkBackwardParametersToe instance()
	{
		return new KickWalkBackwardParametersToe(new Vector3D(-0.19, 0.05, 0.00), getParamArray());
	}

	private KickWalkBackwardParametersToe(Vector3D ballPos, double[] p)
	{
		super(ballPos, p);
	}

	private static double[] getParamArray()
	{
		//		Average utility: 6.395 averaged: 10, at Sun Jul 02 15:27:26 CEST 2017, properties: {
		//		  "ballX": 6.772,
		//		  "ballY": -0.33,
		//		  "angle": -2.813,
		//		  "distance": 6.789,
		//		  "absBallY": 0.377,
		//		  "maxBallHeight": 0.351,
		//		  "supportFoot": 0.0,
		//		  "standing": 1.0,
		//		  "stabilized": 0.0,
		//		  "hitBall": 1.0,
		//		  "localBallX": -0.161,
		//		  "localBallY": -0.065,
		//		  "supportFootX": -0.245,
		//		  "supportFootY": -0.097,
		//		  "supportFootOrientation": 81.479
		//		}
		return new double[] {4, 0, -0.1500, -0.0500, 0, -0.2000, -0.1300, 0.1000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.6000, -16.4700, -4.4800, 29.6200, -75.8500, 27.7900, 8.2400, 20.9100,
				-43.7400, -5.9000, -33.1300, -28.5500, 62.1300, 16.3600, 48.4500, 6.5400, 0.9700, 3.4400, 3.3100,
				4.7300, 1.6600, 5.5900, 0.5000, 1.0100, 0.8000, 2.7300, 3.9200, 4.9400, 1.7900, 1.1100, -38.6700,
				8.8100, 28.6800, -20.2700, 6.9100, 6.0800, 37.8500, -44.6000, 7.6600, -15.0700, -93.9000, 47.3900,
				11.6200, 32.6600, 6.4900, 1.1800, 2.7900, 5.0700, 2.0800, 4.6300, 3.4200, 0.7700, 5.2300, 6.6700,
				1.0600, 1.7500, 2.8800, 1.7100, 1.7400, -32.7300, 1.0100, 36, -28.2800, 18.3400, 10.6900, 49.5500,
				-26.4300, -13.3500, -32.7200, -97.2500, 32.2100, -12.5700, 33.5100, 5.7900, 1.7100, 3.0700, 4.8000,
				2.5100, 2.7300, 3.9200, 0.3400, 6.6300, 6.6400, 6.2500, 6.1900, 6.6900, 4.8900, 2.1800, -19.7400,
				3.9600, 21.4200, -65.5100, 16.0600, 9.0100, 50.0600, -16.5000, -0.2800, 26.6300, -107.5000, 41.4200,
				-15.7700, 30.5900, 2.4000, 6.1200, 2.4100, 1.4800, 2.6400, 1.2700, 2.8400, 1.7500, 1.4800, 4.7000,
				0.5400, 2.0300, 2.5100, 6.3500};
	}

	public KickDistribution getDistribution()
	{
		return new KickDistribution(
				new double[] {0.0, 0.0, 0.1, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.1, 0.2, 0.3, 0.3},
				new double[] {0.3, 0.2, 0.3, 0.0, 0.1, 0.0, 0.1});
	}
}
