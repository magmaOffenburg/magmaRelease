/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Base interface for all movable objects
 * @author Klaus Dorer
 */
public interface IMoveableObject extends IVisibleObject {
	/**
	 * Returns a list of future positions as predicted by the speed.
	 *
	 * @param howMany the minimum number of future positions that should be
	 *        returned
	 * @return an array of future positions
	 */
	Vector3D[] getFuturePositions(int howMany);

	/**
	 * Estimate possible future positions.
	 *
	 * @param when The minimum number of estimates
	 * @return the expected positions in "when" cycles
	 */
	Vector3D getFuturePosition(int when);

	/**
	 * Get current speed
	 *
	 * @return Current Speed
	 */
	Vector3D getSpeed();

	/**
	 * @return the previous speed
	 */
	Vector3D getOldSpeed();

	double getPossibleSpeed();

	/**
	 * @return true if this object is faster than a threshold
	 */
	boolean isMoving();

	void updateNoVision(float globalTime);
}