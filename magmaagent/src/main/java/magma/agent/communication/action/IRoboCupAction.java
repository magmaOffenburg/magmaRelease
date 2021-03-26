/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action;

import hso.autonomy.agent.communication.action.IAction;

/**
 * Interface to all actions an agent can perform on the server
 */
public interface IRoboCupAction extends IAction {
	/**
	 * Send a single empty sync message
	 */
	void sendSync();
}