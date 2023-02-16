/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.formations;

import hso.autonomy.util.geometry.Pose2D;

public class PenaltyKickerFormation extends Formation
{
	public PenaltyKickerFormation()
	{
		poses.put(1, new Pose2D(-14.5, 0));
		poses.put(8, new Pose2D(4.5f, 0.03f));
	}
}
