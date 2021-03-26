/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

/**
 *
 */
package magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk;

import magma.agent.decision.behavior.base.KickDistribution;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class KickWalkStandingParameters extends KickWalkParameters
{
	public static KickWalkStandingParameters instance()
	{
		return new KickWalkStandingParameters(new Vector3D(-0.19, 0.06, 0.00), getParamArray());
	}

	public KickWalkStandingParameters(Vector3D ballPos, double[] p)
	{
		super(ballPos, p);
	}

	private static double[] getParamArray()
	{
		double[] p = {4, 0, -0.1900, 0.0600, 0, 0.1300, 0.2100, 0.0900, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 1, 1, 1, 1, 1.5873, -28.8969, 2.6265, -21.0972, -77.7251, 38.5024, -1.3635, -1, -19.8626,
				12.1375, 40.4457, -92.5121, -40.0514, -5.9614, 16.8257, 0.1943, 6.1139, 7.0300, 2.8591, 3.3199, 0.8434,
				5.1583, 0.0813, 2.2892, 1.4829, 4.7372, 6.6832, 6.8885, 4.6228, 4.6910, -3.5851, -13.9183, -5.5201,
				-78.6849, -0.8148, 15.0512, 38.4359, -19.7894, 10.7189, 71.8978, -44.0076, 43.8712, -14.5476, 59.3210,
				0.2602, 5.6670, 5.0274, 2.2410, 2.2322, 6.5526, 5.7022, 6.7328, 6.0477, 5.3126, 6.9814, 6.0203, 4.2697,
				1.3781, 2.2558, -47.3713, -2.4693, 18.5374, -71.4051, -18.6904, 4.8178, 58.9793, -32.5865, -0.3376,
				-7.6771, -87.1319, 41.1597, -2.3065, 14.1043, 6.0849, 0, 2.7412, 6.8269, 4.4791, 1.4699, 5.4328, 2.5473,
				4.6980, 2.2342, 2.4715, 6.3411, 1.9155, 2.2787, 1.9670, -26.1503, 4.8879, -14.7475, -2.5638, 22.3479,
				-2.0245, 51.7976, -30.5855, 1.1249, -10.3784, -87.5179, 75, 14.1189, 47.3629, 4.4352, 4.8131, 5.0643,
				0.3465, 4.3527, 4.6852, 6.8099, 2.3017, 5.1733, 6.6248, 4.5613, 4.3624, 1.4785, 5.5892};
		return p;
	}

	@Override
	public KickDistribution getDistribution()
	{
		// TODO: store in an instance to save runtime
		return new KickDistribution(
				new double[] {0.003, 0.003, 0.002, 0.003, 0.004, 0.001, 0.003, 0.001, 0.013, 0.013, 0.954},
				new double[] {0.002, 0.002, 0.002, 0.013, 0.71, 0.244, 0.009, 0.006, 0.003, 0.0, 0.001, 0.0, 0.001, 0.0,
						0.0, 0.0, 0.001, 0.0, 0.001, 0.001, 0.0, 0.0, 0.001, 0.0, 0.0, 0.0, 0.0, 0.0, 0.001, 0.001, 0.0,
						0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
						0.0, 0.0, 0.0, 0.0, 0.0, 0.001});
	}
}
