/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

/**
 * Base for socket connection implementations
 *
 * @author Klaus Dorer
 */
public abstract class SocketConnection extends ConnectionBase
{
	// the active host name of the server
	protected final String host;

	// Server TCP Port
	protected final int port;

	/**
	 * Default
	 */
	public SocketConnection(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	@Override
	public String toString()
	{
		return host + ":" + port;
	}
}
