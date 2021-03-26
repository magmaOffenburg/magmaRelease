/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

/**
 * UDP socket connection implementation
 *
 * @author Klaus Dorer
 */
public class UDPConnection extends SocketConnection
{
	/** the socket we listen to */
	private DatagramSocket datagramSocket;

	private int bufferSize;

	private IByteBufferReceiver receiver;

	private InetAddress address;

	/**
	 * Default
	 */
	public UDPConnection(String host, int port, int bufferSize, IByteBufferReceiver receiver)
	{
		super(host, port);
		this.bufferSize = bufferSize;
		this.receiver = receiver;
	}

	public void establishSending() throws ConnectionException
	{
		try {
			datagramSocket = new DatagramSocket(null);
			address = InetAddress.getByName(host);
			connected = true;
		} catch (IOException e) {
		}
	}

	@Override
	public void establishConnection() throws ConnectionException
	{
		try {
			datagramSocket = new DatagramSocket(null);
			datagramSocket.setReuseAddress(true);
			datagramSocket.setBroadcast(true);
			datagramSocket.setSoTimeout(500);
			datagramSocket.bind(new InetSocketAddress(port));
			connected = true;
		} catch (SocketException e) {
		}
	}

	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		try {
			DatagramPacket packet = new DatagramPacket(msg, msg.length, address, port);
			datagramSocket.send(packet);
		} catch (IOException e) {
			System.err.println("Error while sending");
			e.printStackTrace();
		}
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		try {
			while (!shutDown) {
				final ByteBuffer buffer = ByteBuffer.wrap(new byte[bufferSize]);

				final DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.array().length);

				try {
					datagramSocket.receive(packet);
					buffer.rewind();
					receiver.update(buffer);
				} catch (IOException e) {
				}
			}
		} finally {
			closeConnection();
			shutDown = false;
		}
	}

	public void setReceiver(IByteBufferReceiver receiver)
	{
		this.receiver = receiver;
	}

	/*
	 * Close server connection
	 */
	public void closeConnection()
	{
		connected = false;
		datagramSocket.close();
	}
}
