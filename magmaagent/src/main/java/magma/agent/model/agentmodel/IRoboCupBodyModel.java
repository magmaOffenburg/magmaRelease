/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.agentmodel;

import hso.autonomy.agent.model.agentmodel.IBodyModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author dorer
 */
public interface IRoboCupBodyModel extends IBodyModel {
	/**
	 * Center of Mass relative to the ground
	 *
	 * TODO: Should be renamed to "Center of Stability" or something, since
	 * "center of Gravity" would result in the same as "Center of Mass" as all
	 * Body Parts have the same density
	 */
	Vector3D getCenterOfGravity();

	/**
	 * 4 Vectors that define the stability hull<br>
	 * <br>
	 * 0: Left foot, left tiptoe <br>
	 * 1: Left foot, left heel <br>
	 * 2: Right foot, right tiptoe <br>
	 * 3: Right foot, right heel
	 *
	 * @return Stability Hull
	 */
	Vector3D[] getStabilityHull();
}