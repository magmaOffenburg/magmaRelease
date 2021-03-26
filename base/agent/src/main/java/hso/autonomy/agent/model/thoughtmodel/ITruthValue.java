/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.thoughtmodel;

/**
 * Basic interface for all classes representing a non trivial truth value that requires some kind of memory.
 * @author kdorer
 */
public interface ITruthValue {
	/**
	 * Called each update cycle
	 * @param thoughtModel the thought model that can be used to determine the truth value
	 */
	void update(IThoughtModel thoughtModel);

	/**
	 * @return the truth value of this object
	 */
	boolean isValid();
}