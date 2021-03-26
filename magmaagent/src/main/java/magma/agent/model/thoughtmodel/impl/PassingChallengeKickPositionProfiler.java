/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.impl;

import java.util.SortedSet;
import java.util.TreeSet;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.KickPositionEstimation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class PassingChallengeKickPositionProfiler extends KickPositionProfiler
{
	public PassingChallengeKickPositionProfiler(IRoboCupThoughtModel thoughtModel)
	{
		super(thoughtModel);
	}

	@Override
	protected SortedSet<KickPositionEstimation> doEstimate()
	{
		SortedSet<KickPositionEstimation> result = new TreeSet<>();

		Vector3D playerPos = worldModel.getThisPlayer().getPosition();
		Vector3D nearestPlayerPos = thoughtModel.getPlayersAtMeList().get(0).getPosition();
		Vector3D otherGoalPos = worldModel.getOtherGoalPosition();

		if (playerPos.distance(otherGoalPos) < nearestPlayerPos.distance(otherGoalPos)) {
			result.add(new KickPositionEstimation(otherGoalPos, 1));
		} else {
			result.add(new KickPositionEstimation(
					new Vector3D(nearestPlayerPos.getX(), (nearestPlayerPos.getY() + playerPos.getY()) / 2,
							nearestPlayerPos.getZ()),
					1));
		}

		return result;
	}
}
