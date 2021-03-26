/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action;

/**
 * Interface to all actions an agent can perform.
 *
 * @author Stefan Glaser
 */
public interface IAction {
	/**
	 * Command an action via the given effector.<br>
	 * All actions added with this method will be accumulated internally and send at the end of this cycle.<br>
	 * <b>Note:</b> Effectors with the same name will override each other.
	 *
	 * @param effector the effector to send at the end of this agent cycle
	 */
	void put(IEffector effector);

	/**
	 * Directly send an action consisting only of the given effector.<br>
	 * <b>Note:</b> Only use this method if the effector you are about to send is required to be processed individually.
	 *
	 * @param effector the effector to send
	 */
	void send(IEffector effector);

	/**
	 * Sends a motor command to the server. Includes commands to all universals
	 * and hinge joints. The speed of each joint has to be set before by
	 * setHingeEffectorSpeed()
	 *
	 */
	void sendAction();
}