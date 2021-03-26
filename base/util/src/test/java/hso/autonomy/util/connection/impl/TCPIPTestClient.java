/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.commandline.BooleanArgument;
import hso.autonomy.util.commandline.HelpArgument;
import hso.autonomy.util.commandline.IntegerArgument;
import hso.autonomy.util.commandline.StringArgument;
import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.observer.IObserver;

/**
 * @author kdorer
 *
 */
public class TCPIPTestClient implements IObserver<byte[]>
{
	private TCPConnection connection;

	private boolean replyMessageServer;

	private int messageCount;

	public TCPIPTestClient(String host, int port, boolean clientResponse)
	{
		this.replyMessageServer = clientResponse;
		connection = new TCPConnection(host, port);
		messageCount = 0;
	}

	public void start() throws ConnectionException
	{
		System.out.println("starting remote connection");
		connection.attach(this);
		connection.establishConnection();
		connection.startReceiveLoop();
	}

	@Override
	public void update(byte[] content)
	{
		byte[] message;
		if (replyMessageServer) {
			message = TCPIPTestMessageCreator.createServerMessage(messageCount);
		} else {
			message = TCPIPTestMessageCreator.createClientMessage(messageCount);
		}
		try {
			connection.sendMessage(message);
		} catch (ConnectionException e) {
			System.err.println(e.getMessage());
		}
		messageCount++;
	}

	/**
	 * A test client can be started with
	 * "java -cp magmaTestTCPIP.jar magma.util.connection.tcpip.TCPIPTestClient"
	 * Use --help for help on the available arguments.
	 */
	public static void main(String[] args)
	{
		StringArgument serverArgument = new StringArgument("server", "localhost", "the ip address of the server");
		IntegerArgument portArgument =
				new IntegerArgument("port", 5790, 0, "the port to which the client tries to connect");
		BooleanArgument replyServerArgument = new BooleanArgument("replyMessageServer",
				"the client should reply with the server message (otherwise it does with client message)");

		new HelpArgument(serverArgument, portArgument, replyServerArgument).parse(args);

		String server = serverArgument.parse(args);
		int port = portArgument.parse(args);
		boolean replyServer = replyServerArgument.parse(args);

		TCPIPTestClient client = new TCPIPTestClient(server, port, replyServer);
		try {
			client.start();
		} catch (ConnectionException e) {
			System.err.println(e.getMessage());
		}
	}
}
