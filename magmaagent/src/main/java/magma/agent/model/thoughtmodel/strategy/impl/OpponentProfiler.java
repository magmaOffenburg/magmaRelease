/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl;

import java.util.List;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.StrategyConfigurationHelper;
import magma.agent.model.worldmodel.IPlayer;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class OpponentProfiler
{
	private Team opponent;

	public void determineOpponentTeam(List<IPlayer> opponentPlayers)
	{
		opponent = !opponentPlayers.isEmpty() ? Team.fromName(opponentPlayers.get(0).getTeamname()) : Team.UNKNOWN;
	}

	public Vector3D getKickOffTargetPosition()
	{
		switch (opponent) {
		case MAGMA_OFFENBURG:
		case MAGMA_OPPONENT:
		case FC_PORTUGAL:
		case UT_AUSTIN_VILLA:
		case BAHIA_RT:
		case FUT_K:
		case AIUT_3D:
		case IT_ANDROIDS:
		case MIRACLE_3D:
		case KGP_KUBS:
		case UNKNOWN:
		default:
			return new Vector3D(-5, -2, 0);
		}
	}

	public String getTeamStrategyName()
	{
		switch (opponent) {
		case MAGMA_OFFENBURG:
		case MAGMA_OPPONENT:
		case FC_PORTUGAL:
		case UT_AUSTIN_VILLA:
		case BAHIA_RT:
		case FUT_K:
		case AIUT_3D:
		case IT_ANDROIDS:
		case MIRACLE_3D:
		case KGP_KUBS:
		case UNKNOWN:
		default:
			return StrategyConfigurationHelper.DEFAULT_STRATEGY;
		}
	}

	private enum Team {
		MAGMA_OFFENBURG("magmaOffenburg"),
		MAGMA_OPPONENT("magmaOpponent"),
		FC_PORTUGAL("FCPortugal"),
		UT_AUSTIN_VILLA("UTAustinVilla"),
		BAHIA_RT("BahiaRT"),
		FUT_K("FUT-K"),
		AIUT_3D("AIUT3D"),
		IT_ANDROIDS("ITAndroids"),
		MIRACLE_3D("Miracle3D"),
		KGP_KUBS("KgpKubs"),
		HFUT_ENGINE_3D("HFUTEngine3D"),
		MIRG("MIRG"),
		WRIGHT_OCEAN("WrightOcean"),
		UNKNOWN("UNKNOWN");

		private String name;

		Team(String name)
		{
			this.name = name;
		}

		private static Team fromName(String name)
		{
			for (Team team : Team.values()) {
				if (StringUtils.containsIgnoreCase(name, team.name)) {
					return team;
				}
			}
			return Team.UNKNOWN;
		}

		@Override
		public String toString()
		{
			return name;
		}
	}
}
