/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.formations;

import hso.autonomy.util.geometry.Pose2D;
import java.util.HashMap;
import java.util.Map;
import magma.agent.model.thoughtmodel.strategy.IFormation;

public class Formation implements IFormation
{
	protected Map<Integer, Pose2D> poses = new HashMap<>();

	@Override
	public Pose2D getPlayerPose(int id)
	{
		Pose2D playerPose = poses.get(id);
		return playerPose != null ? playerPose : new Pose2D();
	}
}
