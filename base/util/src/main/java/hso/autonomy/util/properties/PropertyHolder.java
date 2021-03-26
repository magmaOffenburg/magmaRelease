/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyHolder
{
	private PropertyLoader loader;

	/** store the properties */
	private Properties defaultProperties;
	private Properties specificProperperties;

	/** store parsed properties */
	private Map<String, Double> doubleProperties;
	private Map<String, Integer> intProperties;

	/**
	 * Creates a new PropertyHolder and loads the files from the specified paths.
	 *
	 * @param defaultPropertiesPath the path of the property file that contains the default properties, if there is no
	 * default property file specify null
	 * @param specificPropertiesPath the path of the property file that contains the specific properties
	 */
	public PropertyHolder(String defaultPropertiesPath, String specificPropertiesPath)
	{
		loader = new PropertyLoader();
		defaultProperties = loader.load(defaultPropertiesPath, true);
		specificProperperties = loader.load(specificPropertiesPath, false);
		doubleProperties = new HashMap<>();
		intProperties = new HashMap<>();
	}

	/**
	 * Returns the property value of the specified key.
	 *
	 * @param key the key of the property
	 * @return the value of the specified key
	 */
	public String getProperty(String key)
	{
		return specificProperperties.getProperty(key);
	}

	/**
	 * Returns the property value of the specified key as a double and stores the parsed value.
	 *
	 * @param key the key of the property
	 * @return the value of the key
	 */
	public double getDoubleProperty(String key)
	{
		Double result = doubleProperties.get(key);

		if (result != null) {
			return result;
		} else {
			try {
				result = Double.parseDouble(specificProperperties.getProperty(key));
				doubleProperties.put(key, result);
				return result;
			} catch (NumberFormatException e) {
				System.err.println("Invalid double property: " + key + " = " + specificProperperties.getProperty(key));
				e.printStackTrace();
				return 1;
			}
		}
	}

	/**
	 * Returns the property value of the specified key as a integer and stores the parsed value.
	 *
	 * @param key the key of the property
	 * @return the value of the key
	 */
	public int getIntegerProperty(String key)
	{
		Integer result = intProperties.get(key);

		if (result != null) {
			return result;
		} else {
			try {
				result = Integer.parseInt(specificProperperties.getProperty(key));
				intProperties.put(key, result);
				return result;
			} catch (NumberFormatException e) {
				System.err.println("Invalid integer property: " + key + " = " + specificProperperties.getProperty(key));
				e.printStackTrace();
				return 1;
			}
		}
	}

	/**
	 * Returns the property value of the specified key as a float and stores the parsed value.
	 *
	 * @param key the key of the property
	 * @return the value of the key
	 */
	public float getFloatProperty(String key)
	{
		return (float) getDoubleProperty(key);
	}

	/**
	 * Returns the property value of the specified key as a short and stores the parsed value.
	 *
	 * @param key the key of the property
	 * @return the value of the key
	 */
	public int getShortProperty(String key)
	{
		return (short) getIntegerProperty(key);
	}

	/**
	 * Returns the property value of the specified key as a byte and stores the parsed value.
	 *
	 * @param key the key of the property
	 * @return the value of the key
	 */
	public int getByteProperty(String key)
	{
		return (byte) getIntegerProperty(key);
	}

	private class PropertyLoader
	{
		/**
		 * Loads a .properties file from the specified path.
		 *
		 * @param path the path of the property file
		 * @param isDefault true if the file to be loaded contains default properties otherwise false
		 * @return a java.util.Properties object containing the properties
		 */
		private Properties load(String path, boolean isDefault)
		{
			if (path == null) {
				return null;
			}

			Properties result = isDefault ? new Properties() : new Properties(defaultProperties);

			try (InputStream in = PropertyLoader.class.getResourceAsStream(path)) {
				result.load(in);
			} catch (IOException e) {
				System.err.println("Unable to load property file from " + path);
				e.printStackTrace();
			}

			return result;
		}
	}
}
