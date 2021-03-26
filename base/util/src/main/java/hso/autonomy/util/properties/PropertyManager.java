/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.properties;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Pose2D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class PropertyManager
{
	private static PropertyHolder properties;

	/**
	 * Loads .properties files from the specified paths.
	 *
	 * @param defaultpropertiesPath The path of the file that contains the default properties. If there is no default
	 * property file, specify null.
	 * @param specificpropertiesPath The path of the file that contains the specific properties.
	 */
	public static void load(String defaultpropertiesPath, String specificpropertiesPath)
	{
		properties = new PropertyHolder(defaultpropertiesPath, specificpropertiesPath);
	}

	/**
	 * Searches for the property with the specified key.
	 *
	 * @param key the key of the property
	 * @return The value of the specified key or null if the key doesn't exist.
	 */
	public static String get(String key)
	{
		return properties.getProperty(key);
	}

	/**
	 * Searches for the value with the specified key and returns it as a float.
	 *
	 * @param key the key of the property
	 * @return The value of the specified key or null if either the name or the key doesn't exist.
	 */
	public static float getFloat(String key)
	{
		return properties.getFloatProperty(key);
	}

	/**
	 * Searches for the value with the specified key and returns it as a double.
	 *
	 * @param key the key of the property
	 * @return The value of the specified key or null if either the name or the key doesn't exist.
	 */
	public static double getDouble(String key)
	{
		return properties.getDoubleProperty(key);
	}

	/**
	 * Searches for the value with the specified key and returns it as an integer.
	 *
	 * @param key the key of the property
	 * @return The value of the specified key or null if either the name or the key doesn't exist.
	 */
	public static int getInt(String key)
	{
		return properties.getIntegerProperty(key);
	}

	/**
	 * Searches for the value with the specified key and returns it as an short.
	 *
	 * @param key the key of the property
	 * @return The value of the specified key or null if either the name or the key doesn't exist.
	 */
	public static int getShort(String key)
	{
		return properties.getIntegerProperty(key);
	}

	/**
	 * Searches for the value with the specified key and returns it as an byte.
	 *
	 * @param key the key of the property
	 * @return The value of the specified key or null if either the name or the key doesn't exist.
	 */
	public static int getByte(String key)
	{
		return properties.getIntegerProperty(key);
	}

	/**
	 * Returns the property value of the specified key-prefix as a 2D pose.
	 *
	 * @param key the key of the property
	 * @return the pose of the key-prefix
	 */
	public static Pose2D getPose2D(String keyPrefix)
	{
		double x = properties.getDoubleProperty(keyPrefix + ".x");
		double y = properties.getDoubleProperty(keyPrefix + ".y");
		double theta = properties.getDoubleProperty(keyPrefix + ".theta");

		return new Pose2D(x, y, Angle.deg(theta));
	}

	/**
	 * Returns the property value of the specified key-prefix as a 2D area.
	 *
	 * @param key the key of the property
	 * @return the area of the key-prefix
	 */
	public static Area2D.Float getArea2D(String keyPrefix)
	{
		float minX = properties.getFloatProperty(keyPrefix + ".minX");
		float maxX = properties.getFloatProperty(keyPrefix + ".maxX");
		float minY = properties.getFloatProperty(keyPrefix + ".minY");
		float maxY = properties.getFloatProperty(keyPrefix + ".maxY");

		return new Area2D.Float(minX, maxX, minY, maxY);
	}

	/**
	 * Returns the property value of the specified key-prefix as a 2D vector.
	 *
	 * @param key the key of the property
	 * @return the vector of the key-prefix
	 */
	public static Vector2D getVector2D(String keyPrefix)
	{
		double x = properties.getDoubleProperty(keyPrefix + ".x");
		double y = properties.getDoubleProperty(keyPrefix + ".y");

		return new Vector2D(x, y);
	}

	/**
	 * Returns the property value of the specified key-prefix as a 3D vector.
	 *
	 * @param key the key of the property
	 * @return the vector of the key-prefix
	 */
	public static Vector3D getVector3D(String keyPrefix)
	{
		double x = properties.getDoubleProperty(keyPrefix + ".x");
		double y = properties.getDoubleProperty(keyPrefix + ".y");
		double z = properties.getDoubleProperty(keyPrefix + ".z");

		return new Vector3D(x, y, z);
	}
}
