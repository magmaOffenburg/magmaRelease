/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;

/**
 * Just implements a time trigger that can trigger perception. The
 * real communication is handled a layer above.
 * @author kdorer
 */
public class TimedConnection extends ConnectionBase
{
	/** the number of milliseconds to sleep */
	private int millis;

	/**
	 * @param millis the number of milli seconds to trigger
	 */
	public TimedConnection(int millis)
	{
		this.millis = millis;
	}

	@Override
	public void establishConnection() throws ConnectionException
	{
		connected = true;
	}

	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
		// not needed, done by layer above
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		// main loop with 20ms sleep
		while (!shutDown) {
			observer.onStateChange(new byte[0]);
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		connected = false;
	}
}
