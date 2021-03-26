/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy;

import java.util.List;

/**
 * @author Stephan Kammerer
 */
public interface ITeamStrategy {
	String getName();
	/**
	 * @return List of all available team roles
	 */
	List<IRole> getAvailableRoles();

	/**
	 * @return the specific role for player x
	 */
	IRole getRoleForPlayerID(int playerID);

	IFormation getFormation();
}
