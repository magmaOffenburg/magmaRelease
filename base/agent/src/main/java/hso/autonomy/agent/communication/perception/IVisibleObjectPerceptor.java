/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * The Visible Object Perceptor represents a single object as reported by the visual sensor sub-system.
 *
 * @author Simon Raffeiner
 */
public interface IVisibleObjectPerceptor extends IPerceptor {
	/**
	 * Retrieve the type information associated with this detected visual object.
	 *
	 * @return the type information
	 */
	String getType();

	/**
	 * Retrieve the position depth indicator of this perceptor.
	 *
	 * @return true if this perceptor has depth information in the position, false otherwise
	 */
	boolean hasDepth();

	/**
	 * @return the object position in camera coordinates
	 */
	Vector3D getPosition();

	void setPosition(Vector3D position);

	double getDistance();

	double getHorizontalAngle();

	double getLatitudeAngle();

	/**
	 * Get horizontal angle (in degrees)
	 */
	double getHorizontalAngleDeg();

	/**
	 * Get vertical angle (in degrees)
	 */
	double getLatitudeAngleDeg();
}