/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.List;

/**
 * Interface to access desired positions
 * @author Klaus Dorer
 */
public interface IPositionManager {
	List<PoseSpeed2D> getDesiredPositions();

	/**
	 * @return the last position orientation in the list
	 */
	PoseSpeed2D getFinalPositionSpeed();

	/**
	 * @param posOrientation the position and orientation of desired position
	 * @param enforceFromOutside true if the position should not be changed
	 */
	void setDesiredPosition(PoseSpeed2D posOrientation, boolean enforceFromOutside);

	/**
	 * adds an intermediate position (with orientation) to the list of positions
	 * @param index the index where to add the new position
	 * @param pos the position orientation to add
	 */
	void addDesiredPosition(int index, PoseSpeed2D pos);

	/**
	 * Removes all desired positions from the list
	 */
	void clear();
}