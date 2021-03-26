/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy;

import magma.agent.model.worldmodel.IPlayer;

/**
 * @author Stephan Kammerer
 */
public interface IRoleManager {
	/**
	 * Dynamic role assignment to agents.
	 *
	 * @return role the specific agent has
	 */
	IRole determineRole(IPlayer closestPlayerAtBall, int playerId);

	ITeamStrategy getStrategy();

	void setStrategy(ITeamStrategy strategy);
}