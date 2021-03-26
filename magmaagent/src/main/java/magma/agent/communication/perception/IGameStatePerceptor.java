/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception;

import hso.autonomy.agent.communication.perception.IPerceptor;
import magma.common.spark.TeamColor;

/**
 * The Game State perceptor is a virtual perceptor representing the actual game
 * state, consisting of the global server time and the play mode (BeforeKickOff
 * etc.)
 *
 * @author Simon Raffeiner
 */
public interface IGameStatePerceptor extends IPerceptor {
	String NAME = "GS";

	String getPlaymode();

	float getTime();

	String getTeamSide();

	TeamColor getTeamColor();

	int getAgentNumber();

	int getScoreLeft();

	int getScoreRight();
}