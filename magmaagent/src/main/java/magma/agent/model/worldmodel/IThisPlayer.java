/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel;

import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import hso.autonomy.util.geometry.Pose3D;
import java.util.Map;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface to access visible player information
 * @author Klaus Dorer, Stefan Glaser
 */
public interface IThisPlayer extends IPlayer {
	/**
	 * Returns the relative angle this player's torso has to the specified
	 * position
	 * @param position the position to which to calculate the body angle
	 * @return the relative angle this player's torso has to the specified
	 *         position
	 */
	Angle getBodyDirectionTo(Vector3D position);

	/**
	 * Returns the player's local body orientation. <br>
	 * <br>
	 * Since the coordinate systems of the field and the robot are initially
	 * rotated by -90 degrees, the own player has its own slightly different
	 * system representation. By the local body orientation we describe the
	 * orientation of the robot relative to its initial pose and <b>not</b> to
	 * the field system!<br>
	 * <br>
	 *
	 * Although the resulting rotation is different than the one obtained from
	 * the getGlobalOrientation() method of the IPlayer interface, one can be
	 * transformed into the other.<br>
	 * Examples:<br>
	 * local->global: globalRotation = rotZ(-90) * localRotation * rotZ(90);<br>
	 * global->local: localRotation = rotZ(90) * localRotation * rotZ(-90);
	 *
	 * @return the player's local body orientation
	 */
	Rotation getOrientation();

	boolean isLyingOnBack();

	boolean isLyingOnFront();

	boolean isLeaningToSide();

	boolean isInHandStand();

	/**
	 * @return the z coordinate of the up vector (matrix[2][2])
	 */
	double getUpVectorZ();

	/**
	 * @return true if this is the goal keeper of our team
	 */
	@Override
	boolean isGoalie();

	/**
	 * Calculate global position based on own position and orientation.
	 *
	 * @param localPosition - the position to transform, given in the local root
	 *        body system
	 * @return the position, relative to the global coordinate system
	 */
	Vector3D calculateGlobalPosition(Vector3D localPosition);

	/**
	 * Calculate global position in 2D based on own position and body direction.
	 * <br>
	 * This calculation is independent of rotations along the view axis of the
	 * agent. Is is useful for top view calculations.
	 *
	 * @param localPos - the position to transform, given in the local root body
	 *        system (just xy matters)
	 * @return the position in 2D (top view) relative to the global coordinate
	 *         system
	 */
	Vector3D calculateGlobal2DPosition(Vector3D localPos);

	/**
	 * Calculates the global 2D pose of a local 3D pose, e.g. of a body part
	 * @param poseToTranslate the 3D pose in local coordinate system
	 * @return the global 2D pose
	 */
	Pose2D calculateGlobalBodyPose2D(Pose3D poseToTranslate);

	/**
	 * Translates the local pose to the global coordinate system
	 * @param poseToTranslate the 3D pose to translate in global coordinate
	 *        system
	 * @return the global 3D pose
	 */
	Pose3D calculateGlobalBodyPose(Pose3D poseToTranslate);

	/**
	 * Calculate local position based on own position and orientation.
	 *
	 * @param globalPosition - the position to transform, given in the global
	 *        coordinate system
	 * @return the position, relative to the local root body system
	 */
	Vector3D calculateLocalPosition(Vector3D globalPosition);

	/**
	 * Checks if the passed 2D xy-point (absolute coordinate system) is inside
	 * the passed area (xy, relative coordinate system)
	 * @param absolutePosition 2D vector (x,y) of the point to check in the
	 *        global coordinate system
	 * @param area the area in this player's coordinate system. The coordinates
	 *        should NOT take neck angles into account, i.e. assume head is
	 *        straight.
	 * @return true if the point is inside the polygon
	 */
	boolean isInsideArea(Vector3D absolutePosition, Area2D.Float area);

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is left of this player with respect
	 *         to the body direction
	 */
	boolean positionIsLeft(Vector3D absolutePosition);

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is right of this player with
	 *         respect to the body direction
	 */
	boolean positionIsRight(Vector3D absolutePosition);

	/**
	 * @param absolutePosition the position to check
	 * @return true if the specified position is behind this player with respect
	 *         to the body direction
	 */
	boolean positionIsBehind(Vector3D absolutePosition);

	IPositionManager getPositionManager();

	/**
	 * Allows to set the global Position
	 * @param position in global Coordinates System
	 */
	void setGlobalPosition(Vector3D position, float time);

	/**
	 * Returns the speed the agents wants to run with
	 * @return the speed (in m/cycle) in the global coordinate system
	 */
	Vector3D getIntendedGlobalSpeed();

	/**
	 * @param speed the speed (in m/cycle) in the local coordinate system
	 */
	void setIntendedGlobalSpeed(Vector3D speed);

	/**
	 * @return the 2d position and horizontal rotation of this player
	 */
	IPose2D getPose2D();

	Map<String, Vector3D> getBodyPartsVision();
}