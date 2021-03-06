/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.decision.behavior.movement.kickwalk;

import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.KickWalkStandingParameters;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.ParameterListComposite;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Ball is to the player's side and moves into player's direction
 *
 */
public class KickWalkStraightSideGridToe extends ParameterListComposite
{
	public KickWalkStraightSideGridToe()
	{
		params();
	}

	private void params()
	{
		// kick ball side (0.10,-0.24)
		double[] p0 = {4, 0, 0.1000, -0.2400, 0, -0.0400, 0.1000, 0.1500, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 3.5000, -7.7300, 11.4900, 27.2600, -48.2800, 28.7500, -0.4200, 49.1300,
				-3.5400, -10.2900, -29.5400, -97.9600, 6.4200, 15.0200, 24.0500, 5.2700, 1.9600, 0.3400, 2.6200, 1.2100,
				5.4100, 1.2000, 0.0900, 3.7900, 1.7900, 2.1500, 5.1200, 4.6000, 3.2600, 1.2200, -35.7400, 15.2800,
				-20.4000, -68.1000, 9.5700, 9.2700, 48.9000, -3.9000, -18.3700, -34.4500, -89.0100, 15.5000, 15.6600,
				25, 0.4600, 6.1800, 4.4400, 5.7800, 6.0100, 5.6600, 2.0400, 1.1700, 6.4400, 1.4800, 6.0700, 6, 3,
				1.6200, 4.8800, -3.0900, 3.6800, -40.7500, -73.7200, -5.3500, -2.8400, 18.4900, -45.0500, -12.1400,
				54.4200, -17.6300, 63.2700, -9.1200, 58.7400, 5.7600, 1.4500, 5.9400, 4.4900, 1.0100, 5.6600, 3.0700,
				1.5000, 1.5100, 6.4800, 6.6700, 6.5000, 6.0800, 3.3800, 4.0800, -37.2800, -0.5700, -44.8300, -29.6200,
				20.6300, 10.3500, 22.7800, -10.1100, -12.7400, 19.7100, -44.7500, 13.2800, -3.3100, 19.9400, 5.8200,
				4.9300, 2.5200, 5.4600, 6.4800, 2.5300, 2.8800, 5.7700, 0.7400, 3.6800, 6.6100, 4.7800, 4.5700, 1.1000};
		//		Average utility: 5.239 averaged: 10, at Sun May 06 10:12:00 CEST 2018, properties: {
		//		  "ballX": 5.606,
		//		  "ballY": -0.219,
		//		  "angle": -2.136,
		//		  "distance": 5.635,
		//		  "absBallY": 0.396,
		//		  "maxBallHeight": 0.113,
		//		  "supportFoot": 0.0,
		//		  "standing": 1.0,
		//		  "stabilized": 0.0,
		//		  "hitBall": 1.0,
		//		  "localBallX": 0.064,
		//		  "localBallY": -0.167,
		//		  "supportFootX": -0.09,
		//		  "supportFootY": 0.121,
		//		  "supportFootOrientation": -92.829
		//		}
		add(new KickWalkStandingParameters(new Vector3D(0.064, -0.13, 0), p0));

		// kick ball side (0.05,-0.24)
		double[] p1 = {4, 0, 0.0500, -0.2400, 0, 0, 0.1500, 0.2000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 4.3100, -6.5500, 11.3600, 30.4900, -39.8500, 32.3400, -2, 47.7200, -3.5500,
				-14.7100, -42.8600, -85.3200, -20.3900, 13.8700, 12.5100, 6.1000, 1.3400, 1.4300, 3.1200, 1.9600,
				5.9800, 1.3300, 0.3700, 2.2300, 2.5800, 2.8600, 3.7900, 3.5400, 4.7200, 0.4300, -28.9000, 14.9300,
				-18.4300, -61.2200, 12.1200, 3.4900, 43.2600, -0.5800, -18.0600, -13.4100, -93.0700, 28.2000, 13.7200,
				11, 0.4800, 6.3800, 4.7300, 4.9400, 5.6100, 6.1300, 1.8700, 3.2400, 6.5700, 2.5600, 5.4500, 5.5600,
				3.2700, 1.4500, 3.3500, -1.2000, 4.0600, -42.2000, -75.2800, -7.2500, -5.4400, 16.5100, -45.7600,
				-13.9900, 62.3300, -18.9800, 56.3400, -4.3800, 58.1700, 4.5800, 2.3600, 6.3500, 5.5300, 1.1400, 5.4100,
				3.2700, 2.4700, 2.5300, 6, 6.2400, 6.4300, 6.4900, 2.8400, 2.9200, -35.4700, 1.3700, -35.6400, -37.8500,
				27.6200, 9.6000, 18.0800, -10.0400, -8.6600, 22.7900, -24.2600, 20.4400, -0.8000, 21.8700, 6.1800,
				6.1300, 3.3600, 6.5600, 6.4200, 2.5700, 2.2400, 5.8700, 0.4600, 2.5400, 6.1200, 3.5000, 5.1200, 1.8000};
		//		Average utility: 4.440 averaged: 10, at Sun May 06 20:36:17 CEST 2018, properties: {
		//		  "ballX": 4.911,
		//		  "ballY": -0.298,
		//		  "angle": -3.515,
		//		  "distance": 4.961,
		//		  "absBallY": 0.521,
		//		  "maxBallHeight": 0.048,
		//		  "supportFoot": 0.0,
		//		  "standing": 1.0,
		//		  "stabilized": 0.0,
		//		  "hitBall": 1.0,
		//		  "localBallX": 0.027,
		//		  "localBallY": -0.186,
		//		  "supportFootX": -0.093,
		//		  "supportFootY": 0.115,
		//		  "supportFootOrientation": -93.65
		//		}
		add(new KickWalkStandingParameters(new Vector3D(0.023, -0.13, 0), p1));

		// kick ball side (0.05,-0.28)
		double[] p2 = {4, 0, 0.0500, -0.2800, 0, 0, 0.1500, 0.2000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 4.5700, -2.8500, 13.6600, 29.5100, -41.3900, 31.6600, -2.8200, 40.4400, -5.5900,
				-15, -33.9700, -77.8500, -26.5400, 10.7200, 17.9400, 5.6800, 1.4000, 2.5500, 2.7800, 1.9000, 5.7400,
				1.2300, 0.7500, 2.0100, 2.4100, 3.1800, 4.1400, 4.1800, 5.3500, 0.8900, -32.1200, 16.0300, -12.7800,
				-61.4500, 3.7200, 7, 37.6600, -2.3600, -18.5700, -17.8800, -89.5800, -4.3800, 15.7700, 6.0100, 0.7600,
				6.4100, 5.4200, 4.9900, 6.2800, 5.9000, 1.5300, 4.4000, 6.8800, 2.9500, 5.8100, 5.6100, 4.0500, 2.1900,
				3.2800, -1.6500, 2.7000, -57.4000, -72.3300, 9.1700, -3.7200, 17, -36.3400, -18.8000, 59.3900, -24.9400,
				50.6700, -3.3900, 53.1200, 5.1300, 1.9200, 6.8600, 6.1300, 1.5600, 6.5300, 4.0500, 3.6900, 3.8600,
				6.0100, 6.0900, 6.8300, 5.6800, 2.8700, 3.7600, -30.9600, 1.1600, -35, -40.2100, 35.5000, 6.4900,
				15.3800, -7.0300, -12.1100, 26, -19.6800, 32.3000, -3.2800, 30.5900, 6.1200, 6.5000, 3.8600, 6.3500,
				4.6600, 1.6500, 2.1100, 4.8800, 1.2200, 2.4600, 5.4300, 4.5500, 4.4900, 1.3100};
		//		Average utility: 4.105 averaged: 10, at Mon May 07 10:25:55 CEST 2018, properties: {
		//		  "ballX": 4.792,
		//		  "ballY": -0.871,
		//		  "angle": -12.185,
		//		  "distance": 4.976,
		//		  "absBallY": 0.871,
		//		  "maxBallHeight": 0.135,
		//		  "supportFoot": 0.0,
		//		  "standing": 1.0,
		//		  "stabilized": 0.0,
		//		  "hitBall": 1.0,
		//		  "localBallX": 0.03,
		//		  "localBallY": -0.18,
		//		  "supportFootX": -0.099,
		//		  "supportFootY": 0.111,
		//		  "supportFootOrientation": -94.02
		//		}
		add(new KickWalkStandingParameters(new Vector3D(0.01, -0.165, 0), p2));

		double[] p3 = {4, 0, 0.1000, -0.2800, 0, 0, 0.1500, 0.2000, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 3.2500, -3.5400, 12.6600, 24.2200, -43.0200, 30.6100, -2.7300, 47.3400, -5.3500,
				-15.5700, -27.6100, -72.6800, -42.6600, 15.3100, 36.7300, 5.5400, 1.9700, 1.4900, 2.3300, 1.2700,
				5.4500, 0.4800, 1.5700, 2.8200, 2.8100, 2.1800, 4.5200, 5.0200, 4.4400, 1.0900, -25.8000, 15.6700,
				-1.6100, -61.5100, 5.4900, 3.1700, 31.3400, -5.1600, -18.9800, -27.0300, -100.6000, -8.2100, 11.2000,
				11.7300, 0.2500, 5.6600, 5.1800, 4.5900, 6.2000, 5.9600, 0.3900, 3.6000, 6.4600, 2.8200, 5.9700, 5.9200,
				4.2500, 0.9900, 3.4400, 0.0100, 2.0800, -52.9600, -73.7100, 9.7600, -6.5600, 19.6200, -39, -19.0600,
				57.3800, -37.7100, 62.7500, -5.9000, 56.2400, 5.0300, 1.2900, 6.4100, 5.3700, 1.0700, 6.0200, 3.5800,
				2.9400, 4.5400, 6.6400, 5.9900, 6.4200, 5.7600, 3.0100, 3.4200, -27.9600, 4.0600, -48.9300, -47.4000,
				31.7500, 4.5400, 13.3900, -4.3400, -13.4400, 72.7700, -28.9300, 48.7000, 1.6900, 23.7000, 4.6100,
				6.0100, 2.5500, 5.7200, 5.6000, 1.2300, 2.3600, 5.1200, 1.1100, 2.3100, 6.3300, 3.8700, 3.5400, 0.4700};
		//		Average utility: 3.838 averaged: 10, at Tue May 08 22:52:28 CEST 2018, properties: {
		//		  "ballX": 4.582,
		//		  "ballY": -0.76,
		//		  "angle": -11.775,
		//		  "distance": 4.72,
		//		  "absBallY": 0.883,
		//		  "maxBallHeight": 0.07,
		//		  "supportFoot": 0.0,
		//		  "standing": 1.0,
		//		  "stabilized": 0.0,
		//		  "hitBall": 1.0,
		//		  "localBallX": 0.058,
		//		  "localBallY": -0.165,
		//		  "supportFootX": -0.088,
		//		  "supportFootY": 0.117,
		//		  "supportFootOrientation": -91.648
		//		}
		add(new KickWalkStandingParameters(new Vector3D(0.06, -0.165, 0), p3));
	}
}
