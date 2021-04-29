/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.util.connection.ConnectionException;
import java.io.File;
import magma.util.file.LogfileWriter;

/**
 * Adds that each message received is logged to a file and/or each message sent
 * as well. I would have preferred to do this as a decorator, but there is no
 * interface to decorate the onEachMessage(). And since I only needed it now for
 * Simspark...
 * @author kdorer
 */
public class SimsparkLogfileWriterChannel extends SimsparkChannel
{
	private LogfileWriter logPercept;

	private LogfileWriter logAct;

	/**
	 *
	 * @param manager the channel manager to report to
	 * @param info configuration information for the channel
	 * @param perceptionFile the file to write perceptions to, null if no
	 *        perceptions should be logged
	 * @param actionFile the file to write actions to, null if no actions should
	 *        be logged
	 */
	public SimsparkLogfileWriterChannel(
			IChannelManager manager, ChannelParameters info, File perceptionFile, File actionFile)
	{
		super(manager, info);

		if (perceptionFile != null) {
			logPercept = new LogfileWriter(perceptionFile);
			logPercept.open();
		}

		if (actionFile != null) {
			logAct = new LogfileWriter(actionFile);
			logAct.open();
		}
	}

	@Override
	protected void onEachMessage(byte[] message) throws ConnectionException
	{
		if (logPercept != null) {
			logPercept.write(new String(message));
		}
		super.onEachMessage(message);
	}

	@Override
	protected void sendMessage(byte[] message)
	{
		if (logAct != null) {
			logAct.write(new String(message));
		}
		super.sendMessage(message);
	}

	@Override
	public void stopChannel()
	{
		if (logPercept != null) {
			logPercept.close();
		}

		if (logAct != null) {
			logAct.close();
		}

		super.stopChannel();
	}
}
