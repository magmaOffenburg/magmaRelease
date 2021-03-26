/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.connection.impl;

import hso.autonomy.util.connection.ConnectionException;
import hso.autonomy.util.connection.IServerConnection;
import hso.autonomy.util.connection.impl.ConnectionBase;
import java.io.File;
import java.io.FileNotFoundException;
import magma.util.file.LogfileReader;

/**
 * @author Stefan Glaser
 */
public class RCSSLogFileConnection extends ConnectionBase implements IServerConnection
{
	LogfileReader logfile;

	public RCSSLogFileConnection(File file)
	{
		logfile = new LogfileReader(file);
	}

	public String next()
	{
		String currentMessage;
		if (connected) {
			currentMessage = logfile.next();

			if (currentMessage != null) {
				observer.onStateChange(currentMessage.getBytes());
				return currentMessage;
			} else {
				stopReceiveLoop();
				return null;
			}
		}

		return null;
	}

	@Override
	public void establishConnection() throws ConnectionException
	{
		try {
			logfile.open();
			connected = true;
		} catch (FileNotFoundException e) {
			throw new ConnectionException(e.getMessage(), e);
		}
	}

	@Override
	public void sendMessage(byte[] msg) throws ConnectionException
	{
	}

	@Override
	public void startReceiveLoop() throws ConnectionException
	{
		// nothing to do
	}

	@Override
	public void stopReceiveLoop()
	{
		logfile.close();
		super.stopReceiveLoop();
	}
}
