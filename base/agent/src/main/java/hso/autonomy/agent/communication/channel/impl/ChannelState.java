/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelState;

/**
 *
 * @author rschilli
 *
 */
public class ChannelState implements IChannelState
{
	private ConnectionState connectionState;

	private DataState dataState;

	/** the last problem we had with this channel */
	private String lastErrorMessage;

	public ChannelState()
	{
		connectionState = ConnectionState.NOT_CONNECTED;
		dataState = DataState.CORRECT_DATA;
		lastErrorMessage = null;
	}

	@Override
	public ConnectionState getConnectionState()
	{
		return connectionState;
	}

	@Override
	public DataState getDataState()
	{
		return dataState;
	}

	public String getLastErrorMessage()
	{
		return lastErrorMessage;
	}

	@Override
	public void setLastErrorMessage(String lastErrorMessage)
	{
		this.lastErrorMessage = lastErrorMessage;
	}

	@Override
	public void setConnectionState(ConnectionState connectionState)
	{
		this.connectionState = connectionState;
	}

	@Override
	public void setDataState(DataState dataState)
	{
		this.dataState = dataState;
	}

	@Override
	public boolean isConnected()
	{
		if (connectionState == ConnectionState.CONNECTED) {
			return true;
		}
		if (connectionState == ConnectionState.TRYING_TO_CONNECT) {
			// it may happen that we receive the first message before the state of
			// the connection is set to connected
			return true;
		}
		return false;
	}

	@Override
	public String toString()
	{
		return "Connection State: " + connectionState;
	}
}
