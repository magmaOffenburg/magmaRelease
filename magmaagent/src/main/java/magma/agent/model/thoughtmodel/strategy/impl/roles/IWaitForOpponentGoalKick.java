/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.roles;

import java.util.Optional;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IWaitForOpponentGoalKick {
	default Optional<Vector3D> waitForOpponentGoalKick(IRoboCupWorldModel worldModel, boolean leftSide)
	{
		if (worldModel.getGameState() == GameState.OPPONENT_GOAL_KICK) {
			// we should wait for the ball at the corner
			final float cornerX = worldModel.fieldHalfLength() - 2.9f;
			final float cornerY = leftSide ? 5.4f : -5.4f;
			return Optional.of(new Vector3D(cornerX, cornerY, 0));
		}
		return Optional.empty();
	}
}
