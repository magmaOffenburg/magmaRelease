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
public class StreamConsumer
{
	private InputStream in;

	private boolean print;

	private int id;

	public StreamConsumer(InputStream in, boolean print, int id)
	{
		this.in = in;
		this.print = print;
		this.id = id;
		ConsumerThread t = new ConsumerThread();
		t.start();
	}

	class ConsumerThread extends Thread
	{
		@Override
		public void run()
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			try {
				String line = reader.readLine();
				while (line != null) {
					if (print) {
						System.out.println(id + "-" + line);
					}
					line = reader.readLine();
				}
				reader.close();
			} catch (IOException e) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
