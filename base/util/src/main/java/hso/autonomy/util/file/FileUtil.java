/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtil
{
	public static boolean areEqual(File file1, File file2)
	{
		try {
			return file1.getCanonicalPath().equals(file2.getCanonicalPath());
		} catch (IOException e) {
			return false;
		}
	}

	public static boolean hasEnding(File file, String ending)
	{
		return file.getName().toLowerCase().endsWith(ending);
	}

	/**
	 * Returns an array of filenames from the resource path specified. Works for
	 * jar files.
	 * @param path the path to return the files, ending with "/", no "/" at start
	 * @return the names of each file in the specified URI not including path
	 *         information.
	 * @throws URISyntaxException if the path is not a valid URI
	 * @throws IOException if reading the folder causes problems
	 */
	public static String[] getResourceListing(String path) throws URISyntaxException, IOException
	{
		ClassLoader cl = FileUtil.class.getClassLoader();
		URL pathURL = cl.getResource(path);
		if (pathURL != null && pathURL.getProtocol().equals("file")) {
			// reading from a file
			return new File(pathURL.toURI()).list();
		}

		if (pathURL == null) {
			// when reading from a jar file we use the class loader's path to get
			// hold of the jar
			String className = FileUtil.class.getName().replace(".", "/") + ".class";
			pathURL = cl.getResource(className);
		}

		if (pathURL.getProtocol().equals("jar")) {
			String jarPath = pathURL.getPath().substring(5, pathURL.getPath().indexOf("!"));
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));

			Enumeration<JarEntry> entries = jar.entries();
			Set<String> result = new HashSet<>();
			while (entries.hasMoreElements()) {
				// read through all files of the jar file
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) {
					// but only take the ones in the specified path
					String entry = name.substring(path.length());
					int slashPosition = entry.indexOf("/");
					if (slashPosition >= 0) {
						// only the name in case of a subdirectory
						entry = entry.substring(0, slashPosition);
					}
					result.add(entry);
				}
			}
			jar.close();
			return result.toArray(new String[result.size()]);
		}

		throw new UnsupportedOperationException("Cannot list files for URL " + pathURL);
	}

	public static String readFile(String file)
	{
		try {
			return new String(Files.readAllBytes(Paths.get(file)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeFile(String file, String content, OpenOption... option)
	{
		try {
			Files.write(Paths.get(file), content.getBytes(), option);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFile(String file, String content)
	{
		writeFile(file, content);
	}

	public static void appendFile(String file, String content)
	{
		writeFile(file, content, StandardOpenOption.APPEND);
	}
}
