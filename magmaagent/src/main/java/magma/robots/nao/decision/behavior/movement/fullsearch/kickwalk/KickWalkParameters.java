/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import magma.agent.decision.behavior.base.KickDistribution;
import magma.robots.nao.decision.behavior.movement.fullsearch.FullSearchMovementParameters;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickWalkParameters extends FullSearchMovementParameters
{
	public enum CustomParam { POS_X, POS_Y, KICK_ANGLE, MIN_X_KICK, MAX_X_KICK, MAX_Y_KICK }

	Vector3D ballPos;

	public static KickWalkParameters instance()
	{
		return new KickWalkParameters(new Vector3D(-0.18, 0.00, 0.00), getParamArray());
	}

	public KickWalkParameters(Vector3D ballPos, double[] p)
	{
		super(p, CustomParam.values());
		this.ballPos = ballPos;
	}

	public float get(CustomParam param)
	{
		return get(param.name());
	}

	private static double[] getParamArray()
	{
		// KickWalkDiagonalMaxParametersToe (-0.17/0.04)

		//        Average utility: 5.192 averaged: 10, at Sat Jun 16 23:21:34 CEST 2018, properties: {
		//        "ballX": 2.847,
		//                "ballY": -6.695,
		//                "angle": -66.955,
		//                "distance": 7.284,
		//                "absBallY": 6.695,
		//                "maxBallHeight": 0.344,
		//                "supportFoot": 0.0,
		//                "standing": 0.8,
		//                "stabilized": 0.0,
		//                "hitBall": 1.0,
		//                "localBallX": 0.137,
		//                "localBallY": -0.048,
		//                "trueLocalBallX": 0.152,
		//                "trueLocalBallY": -0.041,
		//                "supportFootX": -0.081,
		//                "supportFootY": 0.089,
		//                "supportFootOrientation": -97.527
		//    }
		//
		return new double[] {4, 0, -0.1700, 0.0400, 0, 0, 0.1900, 0.0800, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1, 5.2892, -25.9333, -11.0380, -42.9535, -50.6867, 24.6765, -7.0747, 31.9657,
				-42.1950, 13.6153, 46.1989, -100.5929, -11.9097, -12.0723, 18.5538, 4.0955, 3.6178, 5.5942, 2.1507,
				2.0073, 2.2501, 4.0745, 2.9656, 5.9864, 1.6942, 4.4770, 5.7728, 4.1994, 5.2769, 3.7521, -16.8920,
				3.2833, -13.0123, -62.0986, -20.6366, 10.4696, 49.0502, -44.7640, -12.6014, 59.9927, -44.9967, 48.4462,
				-6.4361, 51.9568, 5.4428, 4.1644, 4.7280, 1.7155, 0.8833, 5.9972, 5.2305, 6.4987, 6.2552, 5.9986,
				6.0840, 5.5249, 0.7314, 4.7459, 0.5412, -40.8781, -7.9850, 18.0298, -72.1598, -12.2652, -12.8794,
				59.3237, -42.7578, 14.5578, 42.4656, -85.8323, -10.9462, -5.5737, 15.1464, 2.9863, 4.4220, 4.6628,
				6.3676, 7.0519, 4.5756, 2.7434, 4.2936, 5.5440, 1.7115, 2.4400, 5.2333, 3.8517, 4.6403, 0.6211, -6.6407,
				6.7853, -33.0444, -20.2953, 14.8039, 6.6845, 51.5386, -32.5763, 12.2921, -38.6973, -103.2326, 55.8709,
				14.5646, 61.5654, 6.1434, 5.9819, 3.5421, 1.9516, 2.5049, 3.8121, 3.3126, 0.5998, 5.8506, 2.1873,
				6.1006, 4.8505, 0.9497, 5.8598};
	}

	public KickDistribution getDistribution()
	{
		return null;
	}
}
