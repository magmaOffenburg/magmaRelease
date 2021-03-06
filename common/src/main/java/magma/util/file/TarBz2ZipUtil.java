/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.util.file;

import hso.autonomy.util.file.FileUtil;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;

/**
 *
 * @author kdorer
 */
public class TarBz2ZipUtil
{
	/**
	 * Creates the reader used for sequential reading
	 *
	 * @return the reader used for sequential reading
	 * @throws FileNotFoundException if the logsrc is not found
	 */
	public static BufferedReader createBufferedReader(File file) throws FileNotFoundException
	{
		Reader reader = null;
		if (isTarBZ2Ending(file)) {
			reader = getTarBZ2InputStream(file);

		} else if (isBZ2Ending(file)) {
			reader = getCompressedInputStream(file, CompressorStreamFactory.BZIP2);

		} else if (isGZipEnding(file)) {
			reader = getCompressedInputStream(file, CompressorStreamFactory.GZIP);

		} else if (isZIPEnding(file)) {
			reader = getZipStream(file);
		} else {
			reader = new FileReader(file);
		}
		return new BufferedReader(reader);
	}

	public static Reader getZipStream(File file)
	{
		try {
			ZipFile zipFile = new ZipFile(file);
			if (zipFile.size() != 1) {
				System.err.println("Only support single entry zip files");
				zipFile.close();
				return null;
			} else {
				ZipEntry zipEntry = zipFile.entries().nextElement();
				return new InputStreamReader(zipFile.getInputStream(zipEntry));
			}

		} catch (IOException e) {
			// not a zip file
			System.err.println("File has zip ending, but seems to be not zip");
			return null;
		}
	}

	public static Reader getTarBZ2InputStream(File file)
	{
		try {
			// only works for the current layout of tar.bz2 files
			InputStream zStream = new BufferedInputStream(new FileInputStream(file));
			CompressorInputStream bz2InputStream =
					new CompressorStreamFactory().createCompressorInputStream(CompressorStreamFactory.BZIP2, zStream);
			TarArchiveInputStream tarStream = new TarArchiveInputStream(bz2InputStream);
			TarArchiveEntry entry = tarStream.getNextTarEntry();

			// step into deepest directory
			while (entry != null && entry.isDirectory()) {
				TarArchiveEntry[] entries = entry.getDirectoryEntries();
				if (entries.length > 0) {
					entry = entries[0];
				} else {
					// empty directory
					entry = tarStream.getNextTarEntry();
				}
			}
			if (entry == null) {
				System.err.println("tar file does not contain logfile");
				return null;
			}

			// search for proper file
			while (entry != null && !entry.getName().endsWith("sparkmonitor.log")) {
				entry = tarStream.getNextTarEntry();
			}

			if (entry == null) {
				System.err.println("tar file does not contain logfile");
				return null;
			}

			// we have reached the proper position
			return new InputStreamReader(tarStream);

		} catch (IOException e) {
			// not a bz2 file
			System.err.println("File has bz2 ending, but seems to be not bz2");
			e.printStackTrace();
		} catch (CompressorException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Reader getCompressedInputStream(File file, String which)
	{
		try {
			InputStream zStream = new BufferedInputStream(new FileInputStream(file));
			CompressorInputStream bz2InputStream =
					new CompressorStreamFactory().createCompressorInputStream(which, zStream);
			return new InputStreamReader(bz2InputStream);

		} catch (IOException | CompressorException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Creates the writer as zip, bz2 or unpacked stream
	 *
	 * @return the writer used for sequential reading
	 * @throws IOException
	 */
	public static PrintWriter createPrintWriter(File file) throws IOException
	{
		Writer writer = null;
		if (isTarBZ2Ending(file)) {
			// TODO: add support for tar writing
			writer = getCompressingWriter(file, CompressorStreamFactory.BZIP2);

		} else if (isBZ2Ending(file)) {
			writer = getCompressingWriter(file, CompressorStreamFactory.BZIP2);

		} else if (isGZipEnding(file)) {
			writer = getCompressingWriter(file, CompressorStreamFactory.GZIP);

		} else {
			writer = new FileWriter(file);
		}

		return new PrintWriter(new BufferedWriter(writer));
	}

	public static Writer getCompressingWriter(File file, String which)
	{
		try {
			// only works for the current layout of tar.bz2 files
			OutputStream zStream = new BufferedOutputStream(new FileOutputStream(file));

			CompressorOutputStream bz2Stream =
					new CompressorStreamFactory().createCompressorOutputStream(which, zStream);
			// TarArchiveOutputStream tarStream = new TarArchiveOutputStream(
			// bz2Stream);
			// TarArchiveEntry entry = new TarArchiveEntry("spark.log");
			// tarStream.putArchiveEntry(entry);
			return new OutputStreamWriter(bz2Stream);

		} catch (IOException | CompressorException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isTarBZ2Ending(File file)
	{
		return FileUtil.hasEnding(file, "tar.bz2");
	}

	public static boolean isBZ2Ending(File file)
	{
		return FileUtil.hasEnding(file, ".bz2");
	}

	public static boolean isGZipEnding(File file)
	{
		return FileUtil.hasEnding(file, ".gz");
	}

	public static boolean isZIPEnding(File file)
	{
		return FileUtil.hasEnding(file, "zip");
	}
}
