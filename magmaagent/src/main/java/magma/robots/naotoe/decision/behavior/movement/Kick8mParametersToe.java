/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.decision.behavior.movement;

import magma.agent.decision.behavior.base.KickDistribution;
import magma.robots.nao.decision.behavior.movement.kick.Kick8mParameters;

public class Kick8mParametersToe extends Kick8mParameters
{
	@Override
	protected void setValues()
	{
		put(Param.TIME0, 6.8379064f);
		put(Param.TIME1, 2.733952f);
		put(Param.TIME2, 1.2859101f);
		put(Param.TIME3, 5.821396f);
		put(Param.LHP1, 3.8708534f);
		put(Param.RHP1, -16.612797f);
		put(Param.RKP1, -61.28322f);
		put(Param.RFP1, 36.449028f);
		put(Param.RTP1, 0);
		put(Param.LHP2, -39.1873f);
		put(Param.LHYP1, -8.655966f);
		put(Param.LHYPS1, 6.015331f);
		put(Param.LHYPS2, 2.856319f);
		put(Param.RHYP2, -22.684937f);
		put(Param.RHP2, 50.049927f);
		put(Param.RKP2, -0.6450043f);
		put(Param.RFP2, -28.68277f);
		put(Param.RTP2, 0);
		put(Param.RKPS1, 6.0750794f);
		put(Param.RHPS2, 3.9570673f);
		put(Param.RHYPS2, 4.4162498f);
		put(Param.RFPS2, 4.5389266f);
		put(Param.RHR2, -14.342959f);
		put(Param.LHR1, 10.916556f);
		put(Param.LKP2, -1.5f);
		put(Param.LFP2, -1.6f);
		put(Param.POS_X, 0.13255188f);
		put(Param.POS_Y, -0.13252006f);
		put(Param.KICK_ANGLE, -14.710983f);
		put(Param.MIN_X_OFFSET, 0.13643005f);
		put(Param.RUN_TO_X, -0.1416185f);
		put(Param.RUN_TO_Y, 0.02483453f);
		put(Param.CANCEL_DISTANCE, 0.20033681f);
		put(Param.STABILIZE_TIME, 14.054988f);
		// Average utility: 5.585 averaged: 1000, properties: [
		// ballX: 6.424,
		// ballY: 0.005,
		// absBallY: 0.839,
		// maxBallHeight: 0.263,
		// supportFoot: 0.001,
		// hitBall: 0.967,
		// supportFootX: -0.060,
		// supportFootY: 0.049,
		// supportFootOrientation: -80.589
		// ]

		distribution = new KickDistribution(
				new double[] {0.05, 0.011, 0.02, 0.022, 0.021, 0.021, 0.023, 0.017, 0.012, 0.011, 0.02, 0.028, 0.025,
						0.057, 0.117, 0.206, 0.137, 0.113, 0.068, 0.02, 0.001},
				new double[] {0.116, 0.129, 0.138, 0.13, 0.12, 0.092, 0.065, 0.048, 0.036, 0.023, 0.01, 0.015, 0.015,
						0.017, 0.006, 0.002, 0.005, 0.004, 0.004, 0.004, 0.004, 0.003, 0.003, 0.0, 0.004, 0.0, 0.001,
						0.0, 0.0, 0.001, 0.0, 0.001, 0.001, 0.0, 0.0, 0.0, 0.001, 0.0, 0.0, 0.0, 0.0, 0.001, 0.0, 0.0,
						0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.001});
	}
}
