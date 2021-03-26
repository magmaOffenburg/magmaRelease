/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.perception;

import hso.autonomy.agent.communication.perception.IPerceptor;

/**
 * The Hear Perceptor represents virtual ears. It receives what other agents
 * shout via the "say" method.
 *
 * @author Simon Raffeiner
 */
public interface IHearPerceptor extends IPerceptor {
	/**
	 * Get team name of the speaker (new in protocol 0.6.7), "" if not available
	 *
	 * @return team name of the speaker
	 */
	String getTeam();

	/**
	 * Get timestamp
	 *
	 * @return the global time when heard
	 */
	float getTime();

	/**
	 * Get message target
	 *
	 * @return ?? direction from where hear message came?
	 */
	String getTarget();

	/**
	 * Get message
	 *
	 * @return content of the message received from another player
	 */
	String getMessage();
}