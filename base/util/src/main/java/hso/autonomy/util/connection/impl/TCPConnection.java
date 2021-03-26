/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Base for socket connection implementations
 *
 * @author Klaus Dorer
 */
public class TCPConnection extends SocketConnection
{
	/** Network socket */
	private Socket socket;

	/** Outgoing data stream */
	protected OutputStream out;

	/** Incoming data stream */
	protected InputStream in;

	public TCPConnection(String host, int port)
	{
		super(host, port);
	}

	/**
	 * Actually connect to the server
	 */
	@Override
	public void establishConnection() throws ConnectionException
	{
		try {
			socket = new Socket(host, port);
			socket.setTcpNoDelay(true);

			in = new BufferedInputStream(socket.getInputStream());
			out = new BufferedOutputStream(new DataOutputStream(socket.getOutputStream()));

			connected = true;

		} catch (UnknownHostException e) {
			throw new ConnectionException(e.getMessage(), e);
		} catch (ConnectException e) {
			String message = host + ":" + port + " refused the connection. Is the server running?";
			throw new ConnectionException(message, e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ConnectionException("IO problems", e);
		}
	}

	/**
	 * Send a message to the server
	 *
	 * @param msg Message in ASCII form
	 */
	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		byte[] body = msg;

		// System.out.println(new String(msg));

		try {
			out.write(body);
			out.flush();
		} catch (IOException e) {
			shutDown = true;
			throw new ConnectionException("Error writing to socket, shutting down...", e);
		}
	}

	/**
	 * Starts the endless loop of receiving messages and notifying observers
	 */
	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		try {
			while (!shutDown) {
				byte[] currentMessage = receiveMessage();

				// trigger main update cycle
				observer.onStateChange(currentMessage);
			}
		} finally {
			closeConnection();
			shutDown = false;
		}
	}

	/**
	 * Receive server message
	 */
	protected byte[] receiveMessage() throws ConnectionException
	{
		try {
			int length = 16384;
			byte[] buffer = new byte[length];
			int total = in.read(buffer, 0, length);

			if (total < 0) {
				// server was shutdown
				shutDown = true;
				throw new ConnectionException("Server shut down");
			}

			// System.out.println(new String(result));

			byte[] result = new byte[total];
			System.arraycopy(buffer, 0, result, 0, total);
			return result;

		} catch (IOException e) {
			shutDown = true;
			throw new ConnectionException("Error when reading from socket, closing down...", e);
		}
	}

	/**
	 * Close server connection
	 */
	public void closeConnection()
	{
		try {
			connected = false;
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
