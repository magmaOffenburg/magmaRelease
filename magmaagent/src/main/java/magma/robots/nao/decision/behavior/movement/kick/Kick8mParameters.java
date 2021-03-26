/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.kick;

import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.ikMovement.KickMovementParameters;

public class Kick8mParameters extends KickMovementParameters
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
		put(Param.POS_X, 0.12243838f);
		put(Param.POS_Y, -0.120891005f);
		put(Param.KICK_ANGLE, -19.820599f);
		put(Param.MIN_X_OFFSET, 0.13773388f);
		put(Param.RUN_TO_X, -0.1697112f);
		put(Param.RUN_TO_Y, 0.027524972f);
		put(Param.CANCEL_DISTANCE, 0.1786513f);
		put(Param.STABILIZE_TIME, 14.117299f);
		// Average utility: 5.926 averaged: 1000, properties: [
		// ballX: 6.484,
		// ballY: -0.004,
		// absBallY: 0.558,
		// maxBallHeight: 0.130,
		// supportFoot: 0.000,
		// hitBall: 0.977,
		// supportFootX: -0.086,
		// supportFootY: 0.047,
		// supportFootOrientation: -73.387
		// ]

		distribution =
				new KickDistribution(new double[] {0.028, 0.001, 0.003, 0.005, 0.007, 0.007, 0.018, 0.008, 0.002, 0.004,
											 0.004, 0.012, 0.047, 0.161, 0.425, 0.217, 0.036, 0.012, 0.003},
						new double[] {0.149, 0.259, 0.222, 0.16, 0.077, 0.036, 0.027, 0.017, 0.01, 0.012, 0.007, 0.006,
								0.005, 0.002, 0.001, 0.002, 0.001, 0.002, 0.002, 0.0, 0.0, 0.001, 0.0, 0.0, 0.0, 0.0,
								0.001, 0.0, 0.0, 0.0, 0.0, 0.0, 0.001});
	}
}
