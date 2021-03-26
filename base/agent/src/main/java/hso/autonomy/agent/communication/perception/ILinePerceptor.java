/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Perceptor for line sequence
 *
 * @author Klaus Dorer, Fabian Korak
 */
public interface ILinePerceptor extends IVisibleObjectPerceptor {
	Vector3D getPosition2();

	double getLatitudeAngleDeg2();

	double getLatitudeAngle2();

	double getHorizontalAngleDeg2();

	double getHorizontalAngle2();

	double getDistance2();
}