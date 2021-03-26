/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.formations;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import java.util.Optional;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.worldmodel.IPlayer;

public class OpponentKickOffFormation extends Formation
{
	private IRoboCupThoughtModel thoughtModel;

	public OpponentKickOffFormation(IRoboCupThoughtModel thoughtModel)
	{
		this.thoughtModel = thoughtModel;

		poses.put(1, new Pose2D(-14.5, 0));
		poses.put(2, new Pose2D(-11.4, 0));
		poses.put(3, new Pose2D(-11, -7));
		poses.put(4, new Pose2D(-0.5, -6));
		poses.put(5, new Pose2D(-0.5, 6));
		poses.put(6, new Pose2D(-4, -1.3));
		poses.put(7, new Pose2D(-2.25, 0));
		poses.put(8, new Pose2D(-0.3, 3));
		poses.put(9, new Pose2D(-0.3, -3));
		poses.put(10, new Pose2D(-4, 1.5));
		poses.put(11, new Pose2D(-11, 7));
	}

	@Override
	public Pose2D getPlayerPose(int id)
	{
		if (id == 4 || id == 5) {
			Optional<IPlayer> opponent = thoughtModel.getOpponentsAtMeList()
												 .stream()
												 .filter(id == 4 ? player
														 -> player.getPosition().getY() < -5
																 : player -> player.getPosition().getY() > 5)
												 .findFirst();
			if (opponent.isPresent()) {
				return new Pose2D(poses.get(4).getX(), opponent.get().getPosition().getY(), Angle.ZERO);
			}
		}
		return super.getPlayerPose(id);
	}
}
