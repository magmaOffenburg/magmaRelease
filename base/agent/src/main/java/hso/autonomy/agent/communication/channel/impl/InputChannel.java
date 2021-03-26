/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.channel.impl;

import hso.autonomy.agent.communication.channel.IChannelManager;
import hso.autonomy.agent.communication.perception.IMessageParser;
import hso.autonomy.agent.communication.perception.IPerceptorMap;
import hso.autonomy.agent.communication.perception.PerceptorConversionException;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.IServerConnection;

/**
 *
 * @author kdorer
 */
public abstract class InputChannel extends ConnectionChannel
{
	/** message decoder */
	private final IMessageParser parser;

	public InputChannel(IChannelManager manager, IServerConnection connection, IMessageParser parser)
	{
		super(manager, connection);
		this.parser = parser;
	}

	/**
	 * Notification for each message was received on this channel
	 */
	@Override
	protected void onEachMessage(byte[] message) throws ConnectionException
	{
		try {
			publishPerceptors(parser.parseMessage(message));
		} catch (PerceptorConversionException | RuntimeException e) {
			// TODO: we should be able to recognize. At least we should explicitly
			// specify which exceptions we ignore on purpose
			onInvalidData(parser.getErrorString(message) + "\nDetails: " + e.getMessage());
		}
	}

	protected void publishPerceptors(IPerceptorMap rawPerceptors)
	{
		getManager().addPerceptors(rawPerceptors);
	}

	/**
	 * Notification when the first message was received on this channel, before
	 * processing it
	 */
	@Override
	protected void onFirstMessage(byte[] message) throws ConnectionException
	{
	}
}
