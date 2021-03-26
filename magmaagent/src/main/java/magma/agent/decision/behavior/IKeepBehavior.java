/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;

/*
 * marker interface for keep behaviors
 */
public interface IKeepBehavior {
	default boolean shouldAbortKeeping(IRoboCupWorldModel worldModel)
	{
		return !worldModel.getBall().isMoving() || worldModel.getGameState() != GameState.PLAY_ON;
	}
}
