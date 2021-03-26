/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Stefan Glaser, Klaus Dorer
 */
public class LogfileReader
{
	private File logsrc;

	private BufferedReader br;

	public LogfileReader(File file)
	{
		this.logsrc = file;
	}

	public String next()
	{
		String currentMessage;
		try {
			currentMessage = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return currentMessage;
	}

	public void open() throws FileNotFoundException
	{
		try {
			br = TarBz2ZipUtil.createBufferedReader(logsrc);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + logsrc + " " + e.getMessage());
			throw e;
		}
	}

	public void close()
	{
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
