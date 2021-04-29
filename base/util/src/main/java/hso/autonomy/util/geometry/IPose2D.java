/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.util.geometry;

import java.io.Serializable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Interface for a 2D pose.
 *
 * @author Stefan Glaser
 */
public interface IPose2D extends Serializable {
	/**
	 * Retrieve the x-position value.
	 *
	 * @return the x-position value
	 */
	double getX();

	/**
	 * Retrieve the y-position value.
	 *
	 * @return the y-position value
	 */
	double getY();

	/**
	 * Retrieve the position.
	 *
	 * @return the position
	 */
	Vector2D getPosition();

	/**
	 * Retrieve the position as 3D (z = 0).
	 *
	 * @return the position
	 */
	Vector3D getPosition3D();

	/**
	 * Retrieve the horizontal angle
	 *
	 * @return the horizontal angle
	 */
	Angle getAngle();

	/**
	 * Apply this pose as a rigid transformation to another pose. The resulting
	 * pose is stored in a new object.
	 *
	 * @param other the pose to transform
	 * @return a new pose object holding the resulting pose
	 */
	IPose2D applyTo(IPose2D other);

	/**
	 * Apply this pose inversely as a rigid transformation to another pose. The
	 * resulting pose is stored in a new object.
	 *
	 * @param other the pose to transform
	 * @return a new pose object holding the resulting pose
	 */
	IPose2D applyInverseTo(IPose2D other);

	/**
	 * Apply this pose as a rigid transformation to the given position vector.
	 *
	 * @param position the position vector to transform
	 * @return the transformed vector
	 */
	Vector3D applyTo(Vector3D position);

	/**
	 * Apply this pose as a rigid transformation to the given position vector.
	 *
	 * @param position the position vector to transform
	 * @return the transformed vector
	 */
	Vector2D applyTo(Vector2D position);

	/**
	 * Apply this pose as a rigid transformation to the given polygon.
	 *
	 * @param poly the polygon to transform
	 * @return the transformed polygon
	 */
	Polygon applyTo(Polygon poly);

	/**
	 * Apply this pose inversely as a rigid transformation to the given position
	 * vector.
	 *
	 * @param position the position vector to transform
	 * @return the inversely transformed vector
	 */
	Vector3D applyInverseTo(Vector3D position);

	/**
	 * Apply this pose inversely as a rigid transformation to the given 2D position
	 * vector.
	 *
	 * @param position the position vector to transform
	 * @return the inversely transformed vector
	 */
	Vector2D applyInverseTo(Vector2D position);

	/**
	 * Retrieve the reverted pose of this pose. The reverted pose corresponds to the inverted transformation.
	 *
	 * @return a new pose representing the reverse transformation
	 */
	IPose2D revert();

	/**
	 * Returns the Euclidean distance of this position to the other position
	 * @param other the pose to which to calculate the distance to
	 * @return the distance from this to the passed pose
	 */
	double getDistanceTo(IPose2D other);

	/**
	 * Returns the Euclidean distance of this position to the other position
	 * @param other the position to which to calculate the distance to
	 * @return the distance from this pose to the passed position
	 */
	double getDistanceTo(Vector2D other);

	/**
	 * Returns the difference of this angle to the passed angle
	 * @param other the pose from which to calculate the delta angle
	 * @return the the difference of this angle to the passed pose's angle
	 */
	Angle getDeltaAngle(IPose2D other);

	/**
	 * Returns the difference of this angle to connection of this and other
	 * position
	 * @param other the pose from which to calculate the angle to
	 * @return the the difference of this angle to the passed pose's position
	 */
	Angle getAngleTo(IPose2D other);

	/**
	 * Returns the difference of this angle to connection of this and other
	 * position
	 * @param other the position from which to calculate the angle to
	 * @return the the difference of this angle to the passed pose's position
	 */
	Angle getAngleTo(Vector2D other);

	/**
	 * @return a new Pose2D with it's x/y position flipped and it's angle rotated
	 *         by 180 degrees
	 */
	Pose2D flip();
}
