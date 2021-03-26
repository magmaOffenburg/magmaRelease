/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel;

import hso.autonomy.util.geometry.Angle;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public interface IOdometryErrorModel {
	/**
	 *  Apply some noise to the translation update of the odometry
	 * @param trans received odometry translation
	 * @return noisy odometry translation
	 */
	Vector2D applyNoiseToTrans(Vector2D trans);

	/**
	 *  Apply some noise to the z-Angle of the received orientation update of the odometry
	 * @param orientationEstimation received orientation estimation
	 * @return noisy rotation
	 */
	Angle applyNoiseToRotation(Angle deltaZAngle);
}
