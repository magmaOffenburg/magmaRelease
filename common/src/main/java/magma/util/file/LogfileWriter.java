/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Klaus Dorer
 */
public class LogfileWriter
{
	private File logDest;

	private PrintWriter br;

	public LogfileWriter(File file)
	{
		this.logDest = file;
	}

	public void write(String message)
	{
		br.println(message);
	}

	public void open()
	{
		try {
			br = TarBz2ZipUtil.createPrintWriter(logDest);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close()
	{
		br.close();
	}
}
