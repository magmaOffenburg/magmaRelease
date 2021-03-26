/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.worldmodel;

import hso.autonomy.util.geometry.Angle;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Base Interface for all visible objects in the world model
 * @author Klaus Dorer
 */
public interface IVisibleObject {
	/**
	 * @return the name of this object
	 */
	String getName();

	/**
	 * @return the current position in the global coordinate system
	 */
	Vector3D getPosition();

	/**
	 * @return the previous position in the global coordinate system
	 */
	Vector3D getPreviousPosition();

	/**
	 * Retrieves the local position of the visible object.<br>
	 * This position is not directly or necessarily the same "local" position as
	 * observed through the perception! We provide an unique local coordinate
	 * system for the agent, based on the root body system. All local positions
	 * of the model layer are relative to this local system.<br>
	 * <b>Note:</b> The unique local system is facing the x axis!
	 *
	 * @return the local position (relative to the global root body system)
	 */
	Vector3D getLocalPosition();

	/**
	 * Retrieves the position of the visible object as seen by camera.
	 *
	 * @return the position of the visible object in the camera coordinate system
	 */
	Vector3D getSeenPosition();

	/**
	 * @return time stamp when this object was last updated by perception
	 */
	float getLastSeenTime();

	/**
	 *  Refresh the local position of this object after a movement of our robot.
	 *
	 * @param localPosition the updated local position
	 */
	void refreshLocalPosition(Vector3D localPosition);

	/**
	 * Updates this object with the latest perception
	 *
	 * @param localPosition the local position as observed in the root body system
	 * @param globalPosition the calculated global position of this visible object
	 * @param time the current absolute time
	 */
	void updateFromVision(Vector3D seenPosition, Vector3D localPosition, Vector3D globalPosition, float time);

	/**
	 * Updates this object's position due to hearing
	 * @param globalPosition the position of the object in global coordinates
	 */
	void updateFromAudio(Vector3D localPosition, Vector3D globalPosition, float time);

	/**
	 * Returns the delta of current time passed and the last time this object was
	 * seen
	 * @param currentTime the current global time
	 * @return delta of current time passed and the last time this object was seen
	 */
	float getAge(float currentTime);

	// TODO Modify the getDistanceToXY-methods to return the nearest point, not
	// just the first
	/**
	 * @param other the point to which to calculate
	 * @return the distance projection to the x y plane between this object and
	 *         the passed coordinate
	 */
	double getDistanceToXY(Vector3D other);

	/**
	 * @param other the point to which to calculate
	 * @return the distance from this to other
	 */
	double getDistanceToXY(Vector2D other);

	/**
	 * @param other the point to which to calculate
	 * @return the distance projection to the x y plane between this object and
	 *         the passed objects position
	 */
	double getDistanceToXY(IVisibleObject other);

	/**
	 * Calculates the distance of this visible object to the passed one
	 * @param other the object to which to calculate distance
	 * @return the distance of this visible object to the passed one
	 */
	double getDistanceToXYZ(IVisibleObject other);

	/**
	 * Calculates the distance of this visible object to the passed position
	 * @param other the position to which to calculate distance
	 * @return the distance of this visible object to the passed position
	 */
	double getDistanceToXYZ(Vector3D other);

	/**
	 * Calculates the Direction of this visible object to the passed one
	 * @param other the object to which to calculate direction
	 * @return the direction (rad) of this visible object to the passed one
	 */
	Angle getDirectionTo(IVisibleObject other);

	/**
	 * Calculates the global absolute Direction of this visible object to the
	 * passed Vector3D
	 * @param other the Vector3D to which to calculate direction
	 * @return the global absolute direction (rad) of this visible object to the
	 *         passed Vector
	 */
	Angle getDirectionTo(Vector3D other);

	void setVisible(boolean state);

	/**
	 * @return true if this object was seen last perception
	 */
	boolean isVisible();

	/**
	 * @return The distance required to move around the object without collision
	 */
	double getCollisionDistance();

	/**
	 * @return the source where we got this information from
	 */
	InformationSource getInformationSource();
}