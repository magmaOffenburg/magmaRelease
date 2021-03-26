/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao.decision.behavior.movement.kick;

import magma.agent.decision.behavior.base.KickDistribution;
import magma.agent.decision.behavior.ikMovement.KickMovementParameters;

public class Kick11mParameters extends KickMovementParameters
{
	@Override
	protected void setValues()
	{
		put(Param.TIME0, 5.4665203f);
		put(Param.TIME1, 42.299393f);
		put(Param.TIME2, 6.94856f);
		put(Param.TIME3, 4.866303f);
		put(Param.LHP1, -16.210241f);
		put(Param.RHP1, -0.17883682f);
		put(Param.RKP1, -58.80802f);
		put(Param.RFP1, -0.5956955f);
		put(Param.RTP1, 32.27075f);
		put(Param.LHP2, -34.904472f);
		put(Param.LHYP1, -15.432957f);
		put(Param.LHYPS1, 3.2847774f);
		put(Param.LHYPS2, 2.4971957f);
		put(Param.RHYP2, -12.323225f);
		put(Param.RHP2, 26.798836f);
		put(Param.RKP2, -50.18795f);
		put(Param.RFP2, -43.306408f);
		put(Param.RTP2, 32.01544f);
		put(Param.RKPS1, 5.7106204f);
		put(Param.RHPS2, 4.751129f);
		put(Param.RHYPS2, 6.1104727f);
		put(Param.RFPS2, 7.1513143f);
		put(Param.RHR2, 8.171728f);
		put(Param.LHR1, -1.2518616f);
		put(Param.LKP2, -48.328873f);
		put(Param.LFP2, -25.35057f);
		put(Param.POS_X, 0.08943039f);
		put(Param.POS_Y, -0.13052785f);
		put(Param.KICK_ANGLE, -30.416935f);
		put(Param.MIN_X_OFFSET, 0.14064083f);
		put(Param.RUN_TO_X, -0.16300514f);
		put(Param.RUN_TO_Y, 0.02630686f);
		put(Param.CANCEL_DISTANCE, 0.19988737f);
		put(Param.STABILIZE_TIME, 10.889549f);
		// Average utility: 9.950 averaged: 50, properties: {
		// "ballX": 11.035,
		// "ballY": -0.088,
		// "targetDistanceX": 0.723,
		// "targetDistanceY": 0.549,
		// "distanceXY": 1.05
		// }

		distribution = new KickDistribution(
				new double[] {0.048, 0.0, 0.002, 0.016, 0.015, 0.002, 0.0, 0.002, 0.002, 0.002, 0.0, 0.0, 0.002, 0.0,
						0.011, 0.011, 0.001, 0.004, 0.003, 0.006, 0.051, 0.169, 0.289, 0.201, 0.065, 0.035, 0.026,
						0.009, 0.012, 0.01, 0.005, 0.001},
				new double[] {0.219, 0.298, 0.191, 0.13, 0.061, 0.016, 0.018, 0.008, 0.008, 0.005, 0.002, 0.001, 0.004,
						0.004, 0.005, 0.005, 0.002, 0.005, 0.002, 0.005, 0.001, 0.001, 0.0, 0.001, 0.002, 0.002, 0.0,
						0.001, 0.001, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.001, 0.0, 0.0, 0.001});
	}
}
