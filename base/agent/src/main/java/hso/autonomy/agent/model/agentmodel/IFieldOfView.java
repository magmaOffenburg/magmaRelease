/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.util.geometry.Angle;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Interface for the field of view of a camera, lidar or radar sensor.
 *
 * @author Stefan Glaser
 */
public interface IFieldOfView {
	/**
	 * Retrieve the horizontal field of view range (+/- angle).<br>
	 * <b>Note:</b> If the total horizontal field of view angle is 120 degrees, this angle is 60 degrees.
	 *
	 * @return the horizontal field of view angle
	 */
	Angle horizontal();

	/**
	 * Retrieve the vertical field of view range (+/- angle).<br>
	 * <b>Note:</b> If the total vertical field of view angle is 120 degrees, this angle is 60 degrees.
	 *
	 * @return the vertical field of view angle
	 */
	Angle vertical();

	/**
	 * Check if the given point is inside this field of view.
	 *
	 * @param point the point to test
	 * @return true, if the point is within the field of view, false if not
	 */
	boolean isInside(Vector3D point);
}