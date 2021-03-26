/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.PoseSpeed2D;
import java.util.List;

public interface IWalkEstimator {
	enum WalkMode
	{
		FORWARD,
		BACKWARD,
		LEFT_SIDE,
		RIGHT_SIDE,
		DIAGONAL_LEFT,
		DIAGONAL_RIGHT,
		DIAGONAL_BACK_LEFT,
		DIAGONAL_BACK_RIGHT
	}

	/**
	 * Calculates if it is best to walk forward, backward, sideward or diagonal
	 * @param thisPlayerPose the pose of this player in the world
	 * @param poses list of (at most 2) poses where to go to
	 * @return the best walk mode given the passed poses
	 */
	WalkMode getFastestWalkMode(IPose2D thisPlayerPose, List<PoseSpeed2D> poses);

	/**
	 * Calculates the estimated time it requires to walk
	 * @param thisPlayerPose the pose of this player in the world
	 * @param poses list of (at most 2) poses where to go to
	 * @return the time to walk the poses with the best walk mode
	 */
	float getFastestWalkTime(IPose2D thisPlayerPose, List<PoseSpeed2D> poses);

	/**
	 * @return the speed we are able to turn (in degrees per second)
	 */
	float getTurningSpeed();

	/**
	 * @param walkMode the walk mode for which to get the speed
	 * @return the speed of the walkMode in m/s
	 */
	float getWalkSpeed(WalkMode walkMode);

	/**
	 * @param walkMode the walk mode for which to set the speed
	 * @param speed the speed to set in m/s
	 */
	void setWalkSpeed(WalkMode walkMode, float speed);
}