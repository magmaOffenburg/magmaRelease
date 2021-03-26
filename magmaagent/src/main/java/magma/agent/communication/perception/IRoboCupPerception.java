/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception;

import hso.autonomy.agent.communication.perception.IPerception;
import java.util.List;

/**
 * The perception represents all input an agent can get from the outside.
 *
 * @author Simon Raffeiner
 *
 */
public interface IRoboCupPerception extends IPerception {
	/** object name constants */
	String BALL = "B";

	String GOAL_LEFT_LEFTPOST = "G1L";

	String GOAL_LEFT_RIGHTPOST = "G2L";

	String GOAL_RIGHT_LEFTPOST = "G1R";

	String GOAL_RIGHT_RIGHTPOST = "G2R";

	String LINE_POINT = "LP";

	String REFERENCE_POINT = "RP";

	String CAMERA_IMU_DATA = "IMU";

	/**
	 * Get Agent State
	 *
	 * @return AgentState perceptor
	 */
	IAgentStatePerceptor getAgentState();

	/**
	 * Get the Game State perceptor
	 *
	 * @return perceptor
	 */
	IGameStatePerceptor getGameState();

	/**
	 * Get the Hear perceptor
	 *
	 * @return perceptor
	 */
	List<IHearPerceptor> getHearPerceptors();

	List<IPlayerPos> getVisiblePlayers();
}
