/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends ConnectionBase
{
	private final boolean littleEndian;

	private DataOutputStream out;

	private BufferedInputStream in;

	private ServerSocket serverSocket;

	private Socket socket;

	private int countConnections;

	/** the time when receive started (in ns) */
	protected long startTime;

	protected long endTime;

	boolean hasClient;

	private int port;

	public TCPServer()
	{
		this(true, 9000);
	}

	public TCPServer(boolean littleEndian, int port)
	{
		this.littleEndian = littleEndian;
		this.port = port;
		hasClient = false;
	}

	@Override
	public void establishConnection() throws ConnectionException
	{
		connected = true;

		Thread ourThread = new Thread(() -> {
			try {
				serverSocket = new ServerSocket(port);
				// System.out.println("Waiting for connections");
				while (!shutDown) {
					Socket newSocket = serverSocket.accept();
					if (socket != null) {
						closeConnection();
					}
					socket = newSocket;
					socket.setTcpNoDelay(true);
					hasClient = true;
					in = new BufferedInputStream(socket.getInputStream());
					out = new DataOutputStream(socket.getOutputStream());
					countConnections++;
					System.out.println("Have new connection (" + countConnections + "): " + socket.getInetAddress());
				}
			} catch (IOException | RuntimeException e) {
				e.printStackTrace();
			}
			connected = false;
		});
		ourThread.start();
	}

	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		if (!hasClient) {
			// System.out.println("No connection, can not send message!");
			return;
		}

		byte[] msg2 = new byte[msg.length + 4];

		// System.out.println(new String(msg));

		// creation of the messages header (4 bytes)
		int len = msg.length;
		int byte0 = (len >> 24) & 0xFF;
		int byte1 = (len >> 16) & 0xFF;
		int byte2 = (len >> 8) & 0xFF;
		int byte3 = len & 0xFF;

		try {
			if (littleEndian) {
				msg2[0] = (byte) byte0;
				msg2[1] = (byte) byte1;
				msg2[2] = (byte) byte2;
				msg2[3] = (byte) byte3;

				// out.writeByte((byte) byte0);
				// out.writeByte((byte) byte1);
				// out.writeByte((byte) byte2);
				// out.writeByte((byte) byte3);
			} else {
				msg2[0] = (byte) byte3;
				msg2[1] = (byte) byte2;
				msg2[2] = (byte) byte1;
				msg2[3] = (byte) byte0;
				// out.writeByte((byte) byte3);
				// out.writeByte((byte) byte2);
				// out.writeByte((byte) byte1);
				// out.writeByte((byte) byte0);
			}

			System.arraycopy(msg, 0, msg2, 4, msg.length);

			out.write(msg2);
			out.flush();
		} catch (IOException e) {
			closeConnection();
		}

		// System.out.println("Sent: " + msg);
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		while (!shutDown) {
			try {
				if (!hasClient) {
					Thread.sleep(100);
					continue;
				}

				byte[] currentMessage = receiveMessage();

				// trigger main update cycle
				observer.onStateChange(currentMessage);

				// debug information
				endTime = System.nanoTime();
			} catch (InterruptedException e) {
				// e.printStackTrace();
			} catch (ConnectionException e) {
				e.printStackTrace();
				closeConnection();
			}
		}
		closeConnection();
	}

	protected byte[] receiveMessage() throws ConnectionException
	{
		byte[] result;
		int length;

		try {
			int byte0 = in.read();

			startTime = System.nanoTime();

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
				// connection was shutdown
				throw new ConnectionException("Connection shut down");
			}

			result = new byte[length];
			while (total < length) {
				total += in.read(result, total, length - total);
			}

			// System.out.println(new String(result));

		} catch (IOException e) {
			closeConnection();
			throw new ConnectionException("Error when reading from socket, closing down...", e);
		}

		return result;
	}

	/*
	 * Close server connection
	 */
	public void closeConnection()
	{
		hasClient = false;
		if (socket != null) {
			System.out.println("Closing Connection: " + socket.getInetAddress());
		}

		try {
			if (out != null) {
				out.close();
				out = null;
			}
			if (in != null) {
				in.close();
				in = null;
			}
			if (socket != null) {
				socket.close();
				socket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out = null;
			in = null;
			socket = null;
		}
	}

	@Override
	public void stopReceiveLoop()
	{
		shutDown = true;
		closeConnection();
		try {
			if (!serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
