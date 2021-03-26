/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.channel.IChannelState.ConnectionState;
import hso.autonomy.agent.communication.channel.IChannelState.DataState;
import hso.autonomy.agent.communication.channel.IInputChannel;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.IServerConnection;
import hso.autonomy.util.observer.IObserver;
import java.util.Arrays;

/**
 *
 * @author kdorer
 */
public abstract class ConnectionChannel extends ChannelBase implements IInputChannel, IObserver<byte[]>
{
	/** the connection to the server */
	protected final IServerConnection connection;

	/** the number of received messages */
	private int numberOfReceivedMessages;

	public ConnectionChannel(IChannelManager manager, IServerConnection connection)
	{
		super(manager);
		this.connection = connection;
		numberOfReceivedMessages = 0;
	}

	/**
	 * Starts the connection to the server, will only return after disconnection
	 * Uses default IP and port
	 *
	 * @return true, if connection was successful and the agent stopped receiving
	 *         messages, false if connection was refused or the server shut down
	 */
	@Override
	public boolean startChannel()
	{
		if (connection == null) {
			return false;
		}
		if (connection.isConnected()) {
			return true;
		}
		connection.attach(this);
		state.setConnectionState(ConnectionState.TRYING_TO_CONNECT);
		try {
			connection.establishConnection();
			state.setConnectionState(ConnectionState.CONNECTED);
		} catch (ConnectionException e) {
			state.setConnectionState(ConnectionState.CONNECTION_FAILED);
			state.setLastErrorMessage(e.getMessage());
			System.err.println("ConnectionChannel::startChannel(): " + e);
			return false;
		}

		return startReceiveLoop();
	}

	/**
	 * Starts to receive data from the connection. The default implementation
	 * calls the connections startReceiveLoop method and may block depending on
	 * the connection.
	 */
	protected boolean startReceiveLoop()
	{
		try {
			connection.startReceiveLoop();
			return true;
		} catch (ConnectionException e) {
			state.setLastErrorMessage(e.getMessage());
			state.setConnectionState(ConnectionState.CONNECTION_FAILED);
			return false;
		}
	}

	@Override
	public void stopChannel()
	{
		if (connection == null || !connection.isConnected()) {
			return;
		}
		connection.stopReceiveLoop();
		state.setConnectionState(ConnectionState.NOT_CONNECTED);
	}

	@Override
	public void update(byte[] message)
	{
		try {
			if (numberOfReceivedMessages == 0) {
				onFirstMessage(message);
			}
			onEachMessage(message);
			numberOfReceivedMessages++;

		} catch (ConnectionException e) {
			System.err.println("ConnectionChannel::update(): " + e);
			state.setConnectionState(ConnectionState.DISCONNECTED);

		} catch (RuntimeException e) {
			onInvalidData(Arrays.toString(message));
		}
	}

	protected void onInvalidData(String message)
	{
		System.err.println("Error parsing message:\n" + message);
		state.setDataState(DataState.INVALID_DATA);
	}

	/**
	 * Notification when the first message was received on this channel, before
	 * processing it
	 */
	protected void onFirstMessage(byte[] message) throws ConnectionException
	{
	}

	/**
	 * Notification for each message was received on this channel
	 */
	protected void onEachMessage(byte[] message) throws ConnectionException
	{
	}

	public int getNumberOfReceivedMessages()
	{
		return numberOfReceivedMessages;
	}
}
