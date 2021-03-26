/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import hso.autonomy.agent.model.worldmodel.IMoveableObject;
import hso.autonomy.util.geometry.Angle;
import java.util.Map;
import magma.common.humanoid.HLPenalty;
import magma.common.humanoid.HLRole;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface to access visible player information
 * @author Klaus Dorer
 */
public interface IPlayer extends IMoveableObject {
	/**
	 * @return the player number
	 */
	int getID();

	/**
	 * @return the team name of this player
	 */
	String getTeamname();

	/**
	 * @return true if this is a player of our team
	 */
	boolean isOwnTeam();

	/**
	 * @return estimated maximum speed of this player in m/s
	 */
	double getMaxSpeed();

	/**
	 * @return true if this is the goal keeper
	 */
	boolean isGoalie();

	/**
	 * Retrieves the horizontal angle of the player with respect to the global
	 * coordinate system. The horizontal angle is basically a 2D projection of
	 * the view axis of this player.
	 *
	 * @return the horizontal angle relative to the global coordinate system
	 */
	Angle getHorizontalAngle();

	/**
	 * @return the player's global orientation relative to the global field
	 *         system
	 */
	Rotation getGlobalOrientation();

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	float getTimeToTurnAndRun(Vector3D position, Angle directionAtTarget);

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction by side
	 * stepping
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @param left true if we want to step left
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	float getTimeForSideStep(Vector3D position, Angle directionAtTarget, boolean left);

	/**
	 * Calculates the time a player is expected to take to get from its current
	 * position to the passed position heading in the passed direction by back
	 * stepping
	 * @param position the destination position (global)
	 * @param directionAtTarget desired body direction (global, rad) at the
	 *        destination position
	 * @return time in seconds we estimate is needed to get to the specified
	 *         position with the specified direction
	 */
	float getTimeForBackStep(Vector3D position, Angle directionAtTarget);

	/**
	 * @return the map of bodyParts
	 */
	Map<String, Vector3D> getBodyParts();

	/**
	 * @return whether the player lies on the ground
	 */
	boolean isLying();

	void resetTTL();

	boolean decreaseTTL();

	/**
	 * Retrieve the penalty currently assigned to the player.
	 *
	 * @return the player penalty
	 */
	HLPenalty getPenalty();

	/**
	 * Check if the player is penalized.
	 *
	 * @return true, if the player has a penalty assigned to it, false otherwise
	 */
	boolean isPenalized();

	/**
	 * Check if the player is a substitute.
	 *
	 * @return true, if the player is a substitute player, false otherwise
	 */
	boolean isSubstitute();

	/**
	 * Retrieve the time the player remains penalized.
	 *
	 * @return the time the player remains penalized
	 */
	float getTimeRemainingPenalized();

	/**
	 * Retrieve the current role of the player.
	 *
	 * @return the current player role
	 */
	HLRole getRole();
}