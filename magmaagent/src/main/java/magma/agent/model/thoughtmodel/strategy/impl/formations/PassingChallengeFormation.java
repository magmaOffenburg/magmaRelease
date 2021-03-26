/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.formations;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;

public class PassingChallengeFormation extends Formation
{
	public PassingChallengeFormation()
	{
		poses.put(7, new Pose2D(4, 1));
		poses.put(8, new Pose2D(-0.2, -0.1, Angle.deg(35)));
		poses.put(9, new Pose2D(8, 0));
		poses.put(10, new Pose2D(13, 1));
	}
}