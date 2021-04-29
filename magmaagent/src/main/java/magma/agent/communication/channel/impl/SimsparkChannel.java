/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IChannelState.ConnectionState;
import hso.autonomy.agent.communication.channel.impl.InputOutputChannel;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.impl.ServerConnection;
import java.util.List;
import magma.agent.communication.action.impl.ServerMessageEncoder;
import magma.agent.communication.channel.IRoboCupChannel;
import magma.agent.communication.perception.impl.ServerMessageParser;

/**
 *
 * @author kdorer
 */
public class SimsparkChannel extends InputOutputChannel implements IRoboCupChannel
{
	/** the scene string to send as first command */
	private String scene;

	/** the init message to be sent as second message */
	private String initMessage;

	public SimsparkChannel(IChannelManager manager, ChannelParameters info)
	{
		super(manager, new ServerConnection(info.getHost(), info.getPort()), new ServerMessageParser(),
				new ServerMessageEncoder());

		initMessage = "(init (unum " + info.getPlayerNumber() + ")(teamname " + info.getTeamname() + "))";
	}

	@Override
	public void init(List<String> initParams)
	{
		this.scene = "(scene " + initParams.get(0) + ")";
	}

	@Override
	protected boolean startReceiveLoop()
	{
		Thread ourThread = new Thread(() -> {
			try {
				connection.sendMessage(scene.getBytes());
				connection.startReceiveLoop();
				state.setConnectionState(ConnectionState.NOT_CONNECTED);
			} catch (ConnectionException e) {
				state.setConnectionState(ConnectionState.DISCONNECTED);
			} catch (RuntimeException e) {
				state.setConnectionState(ConnectionState.DISCONNECTED);
				e.printStackTrace();
			}
			getManager().lostConnection(SimsparkChannel.this);
		});
		ourThread.start();
		return true;
	}

	/**
	 * Notification when the first message was received on this channel, before
	 * processing it
	 * @throws ConnectionException
	 */
	@Override
	protected void onFirstMessage(byte[] message) throws ConnectionException
	{
		// After initially sending the scene string (for choosing the
		// model), we are forced to send a further (soccer specific)
		// initialization string to the server as the reply of the very first
		// received perception-message.
		connection.sendMessage(initMessage.getBytes());
	}
}
