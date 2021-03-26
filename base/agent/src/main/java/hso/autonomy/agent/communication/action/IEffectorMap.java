/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.action;

import java.util.Map;

/**
 * Interface for simplifying the handling of effectors in a map.
 *
 * @author Stefan Glaser
 */
public interface IEffectorMap extends Map<String, IEffector> {
	/**
	 * Put the given effector into this map.
	 *
	 * @param effector the effector instance to add
	 */
	public void put(IEffector effector);

	/**
	 * Put all entries of the given map into this map.
	 *
	 * @param map the effector map to put into this map
	 */
	public void put(IEffectorMap map);

	/**
	 * Generic method for fetching typed effectors.
	 *
	 * @param name the name of the requested effector
	 * @param clazz the class of the effector
	 * @return an effector of the specified class with the given name is such an effector exists, or null otherwise
	 */
	public <T extends IEffector> T get(String name, Class<T> clazz);
}
