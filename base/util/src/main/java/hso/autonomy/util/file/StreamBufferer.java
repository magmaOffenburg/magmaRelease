/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author kdorer
 */
public class StreamBufferer
{
	private InputStream in;

	private StringBuffer buffer;

	private int maxSize;

	public StreamBufferer(InputStream in, int maxSize)
	{
		this.in = in;
		this.maxSize = maxSize;
		ConsumerThread t = new ConsumerThread();
		t.start();
	}

	class ConsumerThread extends Thread
	{
		@Override
		public void run()
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			try {
				buffer = new StringBuffer(maxSize);
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
					if (buffer.length() > maxSize) {
						buffer.delete(0, buffer.length() - maxSize + 100);
					}
				}
				reader.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
	}

	public String getBuffer()
	{
		return buffer.toString();
	}
}
