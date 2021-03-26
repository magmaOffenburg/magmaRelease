/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * "Force" sensor interface. Provides read-only access only.
 *
 * @author Klaus Dorer
 */
public interface IForceResistance extends ISensor {
	/**
	 * Retrieve the force origin (the point in 3-dimensional space the force
	 * effects on)
	 *
	 * @return Force origin point
	 */
	Vector3D getForceOrigin();

	/**
	 * Retrieve the 3-dimensional force vector
	 *
	 * @return Force vector
	 */
	Vector3D getForce();

	Vector3D getPreviousForce();

	Vector3D getPreviousForceOrigin();

	/**
	 * @return the number of consecutive update cycles we had force on this
	 *         sensor
	 */
	int getCyclesWithForce();

	/**
	 * @return the number of consecutive update cycles we had no force on this
	 *         sensor (not including the current cycle)
	 */
	int getPreviousCyclesWithoutForce();
}