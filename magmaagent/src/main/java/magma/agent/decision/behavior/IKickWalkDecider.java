/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.util.geometry.Area2D;

/**
 * @author kdorer
 *
 */
public interface IKickWalkDecider extends IKickDecider {
	Area2D.Float getKickableArea();

	/** @return true if the base class checks should be performed */
	boolean checkKickEstimator();

	/** @return the minimal number of cycles the force sensor had to be zero in last off ground situation */
	int getOffGroundBefore();

	/** @return the maximal number of cycles the foot sensor has force since last ground contact */
	int getMaxOnGround();

	/** @return the minimal z value the torso's z axis must have */
	double getMinUpVectorZ();
}
