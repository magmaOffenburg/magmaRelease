/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;
import java.io.IOException;

/**
 * Server connection implementation
 *
 * @author Klaus Dorer
 */
public class ServerConnection extends TCPConnection
{
	private final boolean littleEndian;

	public ServerConnection(String host, int port)
	{
		this(host, port, true);
	}

	public ServerConnection(String host, int port, boolean littleEndian)
	{
		super(host, port);
		this.littleEndian = littleEndian;
	}

	/**
	 * Send a message to the server
	 *
	 * @param msg Message in ASCII form
	 */
	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		// creation of the messages header (4 bytes)
		int len = msg.length;
		int byte0 = (len >> 24) & 0xFF;
		int byte1 = (len >> 16) & 0xFF;
		int byte2 = (len >> 8) & 0xFF;
		int byte3 = len & 0xFF;

		try {
			if (littleEndian) {
				out.write((byte) byte0);
				out.write((byte) byte1);
				out.write((byte) byte2);
				out.write((byte) byte3);
			} else {
				out.write((byte) byte3);
				out.write((byte) byte2);
				out.write((byte) byte1);
				out.write((byte) byte0);
			}
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			shutDown = true;
			throw new ConnectionException("Error writing to socket, shutting down...", e);
		}
	}

	@Override
	protected byte[] receiveMessage() throws ConnectionException
	{
		byte[] result;
		int length;

		try {
			int byte0 = in.read();
			int byte1 = in.read();
			int byte2 = in.read();
			int byte3 = in.read();
			if (littleEndian) {
				length = byte0 << 24 | byte1 << 16 | byte2 << 8 | byte3;
			} else {
				length = byte3 << 24 | byte2 << 16 | byte1 << 8 | byte0;
			}
			// the header
			int total = 0;

			if (length < 0) {
				// server was shutdown
				shutDown = true;
				throw new ConnectionException("Server shut down");
			}

			result = new byte[length];
			while (total < length) {
				total += in.read(result, total, length - total);
			}
		} catch (IOException e) {
			shutDown = true;
			throw new ConnectionException("Error when reading from socket, closing down...", e);
		}
		return result;
	}
}
