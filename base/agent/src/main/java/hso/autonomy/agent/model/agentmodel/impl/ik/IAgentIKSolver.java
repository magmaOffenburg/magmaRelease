/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl.ik;

import hso.autonomy.agent.model.agentmodel.IBodyPart;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IAgentIKSolver {
	/**
	 * Solve the IK for a specific body part and the given target pose.<br>
	 * The resulting angles to the joints of the limb represented by the
	 * targetBody are expected to be set along the given body part chain. The
	 * solver is also responsible to not overdrive the joints and limit the
	 * change angles according to the max speeds and angles of the corresponding
	 * joints.
	 *
	 * @param targetBody the target body part (representing the limb to
	 *        manipulate)
	 * @param targetPosition the target position
	 * @param targetAngles the target angles (may be null if not of interest)
	 * @return success
	 */
	boolean solve(IBodyPart targetBody, Vector3D targetPosition, Vector3D targetAngles);

	/**
	 * Just calculates but does not change the body model.
	 *
	 * @param targetBody the target body part (representing the limb to
	 *        manipulate)
	 * @param targetPosition the target position
	 * @param targetAngles the target angles (may be null if not of interest)
	 * @return the angles of each joint from the passed body part back to the
	 *         root, null if calculation is not possible without changing body
	 *         parts.
	 */
	double[] calculateDeltaAngles(IBodyPart targetBody, Vector3D targetPosition, Vector3D targetAngles);
}
