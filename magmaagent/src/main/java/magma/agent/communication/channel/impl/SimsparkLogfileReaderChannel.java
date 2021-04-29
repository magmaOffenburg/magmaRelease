/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IChannelState.ConnectionState;
import hso.autonomy.agent.communication.channel.impl.InputOutputChannel;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.impl.TimedConnection;
import java.io.File;
import java.io.FileNotFoundException;
import magma.agent.communication.action.impl.ServerMessageEncoder;
import magma.agent.communication.perception.impl.ServerMessageParser;
import magma.util.file.LogfileReader;
import magma.util.file.LogfileWriter;

/**
 * Behaves like a simspark connection, but reads the sensor information from a
 * logfile.
 * @author kdorer
 */
public class SimsparkLogfileReaderChannel extends InputOutputChannel
{
	/** file to read perceptions from */
	private LogfileReader logPercept;

	/** file to write actions to */
	private LogfileWriter logAct;

	public SimsparkLogfileReaderChannel(
			IChannelManager manager, ChannelParameters info, File perceptionfile, File actionFile)
	{
		super(manager, new TimedConnection(20), new ServerMessageParser(), new ServerMessageEncoder());

		this.logPercept = new LogfileReader(perceptionfile);

		if (actionFile != null) {
			logAct = new LogfileWriter(actionFile);
			logAct.open();
		}
	}

	@Override
	protected boolean startReceiveLoop()
	{
		Thread ourThread = new Thread(() -> {
			try {
				logPercept.open();
				connection.startReceiveLoop();
				state.setConnectionState(ConnectionState.NOT_CONNECTED);
			} catch (ConnectionException e) {
				state.setConnectionState(ConnectionState.DISCONNECTED);
				System.err.println(e);
			} catch (RuntimeException | FileNotFoundException e) {
				state.setConnectionState(ConnectionState.DISCONNECTED);
				e.printStackTrace();
			}
			getManager().lostConnection(SimsparkLogfileReaderChannel.this);
		});
		ourThread.start();
		return true;
	}

	@Override
	protected void sendMessage(byte[] message)
	{
		if (logAct != null) {
			logAct.write(new String(message));
		}
		// this connection does not send back messages
	}

	/**
	 * Notification for each message was received on this channel
	 */
	@Override
	protected void onEachMessage(byte[] message) throws ConnectionException
	{
		String msg = logPercept.next();
		if (msg == null) {
			stopChannel();
		}
		super.onEachMessage(msg.getBytes());
	}

	@Override
	public void stopChannel()
	{
		logPercept.close();
		if (logAct != null) {
			logAct.close();
		}
		super.stopChannel();
	}
}
