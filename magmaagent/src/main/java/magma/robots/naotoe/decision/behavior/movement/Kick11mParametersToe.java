/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.decision.behavior.movement;

import magma.agent.decision.behavior.base.KickDistribution;
import magma.robots.nao.decision.behavior.movement.kick.Kick11mParameters;

public class Kick11mParametersToe extends Kick11mParameters
{
	@Override
	protected void setValues()
	{
		put(Param.TIME0, 5.980542f);
		put(Param.TIME1, 47.67634f);
		put(Param.TIME2, 5.2337775f);
		put(Param.TIME3, 2.0126028f);
		put(Param.LHP1, -21.672497f);
		put(Param.RHP1, -25.013222f);
		put(Param.RKP1, -43.00942f);
		put(Param.RFP1, -0.60380554f);
		put(Param.RTP1, 24.458244f);
		put(Param.LHP2, -16.346897f);
		put(Param.LHYP1, -25.416431f);
		put(Param.LHYPS1, 5.880075f);
		put(Param.LHYPS2, 5.0096493f);
		put(Param.RHYP2, -4.299588f);
		put(Param.RHP2, 91.211365f);
		put(Param.RKP2, -31.679153f);
		put(Param.RFP2, -31.228767f);
		put(Param.RTP2, 44.641968f);
		put(Param.RKPS1, 4.759756f);
		put(Param.RHPS2, 6.428073f);
		put(Param.RHYPS2, 4.850277f);
		put(Param.RFPS2, 5.503548f);
		put(Param.RHR2, -0.097299576f);
		put(Param.LHR1, -3.3772464f);
		put(Param.LKP2, -25.888914f);
		put(Param.LFP2, -4.456583f);
		put(Param.POS_X, 0.11011081f);
		put(Param.POS_Y, -0.13241145f);
		put(Param.KICK_ANGLE, -37.096344f);
		put(Param.MIN_X_OFFSET, 0.1260245f);
		put(Param.RUN_TO_X, -0.17275508f);
		put(Param.RUN_TO_Y, 0.021898419f);
		put(Param.CANCEL_DISTANCE, 0.23550412f);
		put(Param.STABILIZE_TIME, 12.8303795f);
		// Average utility: 8.863 averaged: 50, properties: {
		// "ballX": 9.466,
		// "ballY": -0.028,
		// "targetDistanceX": 1.82,
		// "targetDistanceY": 0.623,
		// "distanceXY": 2.137
		// }

		distribution =
				new KickDistribution(new double[] {0.085, 0.008, 0.005, 0.008, 0.005, 0.008, 0.028, 0.013, 0.02, 0.005,
											 0.005, 0.01, 0.012, 0.005, 0.005, 0.004, 0.012, 0.018, 0.04, 0.084, 0.181,
											 0.167, 0.128, 0.078, 0.03, 0.018, 0.008, 0.008, 0.002},
						new double[] {0.186, 0.207, 0.174, 0.135, 0.072, 0.05, 0.037, 0.017, 0.018, 0.011, 0.008, 0.007,
								0.009, 0.009, 0.011, 0.005, 0.002, 0.0, 0.002, 0.004, 0.003, 0.007, 0.008, 0.0, 0.002,
								0.002, 0.0, 0.002, 0.002, 0.0, 0.001, 0.0, 0.0, 0.0, 0.0, 0.002, 0.001, 0.0, 0.001, 0.0,
								0.0, 0.0, 0.0, 0.001, 0.001, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
								0.0, 0.0, 0.001, 0.0, 0.0, 0.0, 0.0, 0.001, 0.0, 0.0, 0.0, 0.0, 0.0, 0.001});
	}
}
