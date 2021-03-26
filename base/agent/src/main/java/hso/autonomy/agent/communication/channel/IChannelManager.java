/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.channel;

import hso.autonomy.agent.communication.action.IActionPerformer;
import hso.autonomy.agent.communication.perception.IPerceptionLogger;
import hso.autonomy.agent.communication.perception.IPerceptorMap;

/**
 *
 * @author kdorer
 */
public interface IChannelManager extends IActionPerformer {
	enum ChannelManagerStatus
	{
		CREATED,
		STARTED,
		STOPPED,
		LOST_MAIN_CONNECTION
	}

	void addPerceptors(IPerceptorMap newPerceptors);

	void addInputChannel(IInputChannel channel, boolean isMainChannel);

	void addOutputChannel(IOutputChannel channel);

	boolean start();

	void stop();

	boolean isConnected();

	/**
	 * @return the next map of perceptors to process, null if there is none
	 */
	IPerceptorMap getNextPerceptorMap();

	/**
	 * Called if a connection of an input channel is lost
	 */
	void lostConnection(IInputChannel channel);

	ChannelManagerStatus getStatus();

	/**
	 * Whether there was a connection at any point
	 */
	boolean hadConnection();

	IInputChannel getMainChannel();

	IPerceptionLogger getPerceptionLogger();
}
