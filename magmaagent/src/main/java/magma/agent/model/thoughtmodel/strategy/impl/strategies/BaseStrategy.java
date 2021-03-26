/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import java.util.ArrayList;
import java.util.List;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.IFormation;
import magma.agent.model.thoughtmodel.strategy.IRole;
import magma.agent.model.thoughtmodel.strategy.ITeamStrategy;
import magma.agent.model.thoughtmodel.strategy.impl.formations.OpponentKickOffFormation;
import magma.agent.model.thoughtmodel.strategy.impl.formations.OwnKickOffFormation;
import magma.agent.model.worldmodel.GameState;
import magma.agent.model.worldmodel.IRoboCupWorldModel;
import magma.common.spark.PlaySide;

/**
 * @author Stephan Kammerer
 */
public abstract class BaseStrategy implements ITeamStrategy
{
	/** All available roles which can be applied */
	protected final ArrayList<IRole> availableRoles = new ArrayList<>();

	private String name;

	protected final IRoboCupThoughtModel thoughtModel;

	protected final IRoboCupWorldModel worldModel;

	protected IFormation ownKickOffFormation = new OwnKickOffFormation();

	protected IFormation opponentKickOffFormation;

	public BaseStrategy(String name, IRoboCupThoughtModel thoughtModel)
	{
		this.name = name;
		this.thoughtModel = thoughtModel;
		this.worldModel = this.thoughtModel.getWorldModel();
		opponentKickOffFormation = new OpponentKickOffFormation(thoughtModel);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public List<IRole> getAvailableRoles()
	{
		return availableRoles;
	}

	@Override
	public IRole getRoleForPlayerID(int playerID)
	{
		if (playerID < 1 || availableRoles.size() < playerID) {
			return null;
		}
		return availableRoles.get(playerID - 1);
	}

	@Override
	public IFormation getFormation()
	{
		// we assume that the left team always has kickoff because
		// the server is usually restarted for each half in competitions
		GameState gameState = worldModel.getGameState();
		if ((gameState == GameState.BEFORE_KICK_OFF && worldModel.getPlaySide() == PlaySide.LEFT) ||
				gameState == GameState.OPPONENT_GOAL || gameState == GameState.OWN_KICK_OFF) {
			return ownKickOffFormation;
		}
		return opponentKickOffFormation;
	}
}
