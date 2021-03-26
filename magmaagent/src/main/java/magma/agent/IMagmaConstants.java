/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent;

/**
 * Interface for all constants that should be globally accessible and are
 * specific to our team
 * @author Klaus Dorer
 */
public interface IMagmaConstants {
	/** the default name of the own team */
	String DEFAULT_TEAMNAME = "magmaOffenburg";

	/** the default decision maker name */
	String DEFAULT_DECISION_MAKER = "Default";

	/** the default component factory */
	String DEFAULT_FACTORY = "Default";

	/** the default id of the own team */
	byte DEFAULT_TEAMID = 43;

	int DEFAULT_SERVER_VERSION = 66;

	/** the number of players that play at most in one team */
	int NUMBER_OF_PLAYERS_PER_TEAM = 11;

	/** constant used to identify the id of a player that is not known */
	int UNKNOWN_PLAYER_NUMBER = -1;

	/** constant used to identify the team name of a player that is not known */
	String UNKNOWN_PLAYER_TEAMNAME = "";

	double DISTANCE_PENALTY_LYING = 3;

	float TIME_DELAY_LYING = 0.5f;
}
