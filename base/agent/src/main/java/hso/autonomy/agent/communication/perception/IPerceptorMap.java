/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import java.util.Map;

/**
 * Interface for simplifying the handling of perceptors in a map.
 *
 * @author Stefan Glaser
 */
public interface IPerceptorMap extends Map<String, IPerceptor> {
	/**
	 * Put the given perceptor into this map.
	 *
	 * @param perceptor the perceptor instance to add
	 */
	public void put(IPerceptor perceptor);

	/**
	 * Put all entries of the given map into this map.
	 *
	 * @param map the perceptor map to put into this map
	 */
	public void put(IPerceptorMap map);

	/**
	 * Generic method for fetching typed perceptors.
	 *
	 * @param name the name of the requested perceptor
	 * @param clazz the class of the perceptor
	 * @return a perceptor of the specified class with the given name if such a perceptor exists, or null otherwise
	 */
	public <T extends IPerceptor> T get(String name, Class<T> clazz);
}
