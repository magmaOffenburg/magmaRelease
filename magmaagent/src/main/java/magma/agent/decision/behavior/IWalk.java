/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.Pose2D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IWalk extends IBehavior {
	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100 .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left
	 */
	void walk(double forwardsBackwards, double stepLeftRight, Angle turnLeftRight);

	/**
	 * Set parameters for Walk. It is possible to combine all different
	 * parameters, e.g. forwards and sidesteps.
	 *
	 * @param forwardsBackwards positive = forwards; negative = backwards (-100 .. 100)
	 * @param stepLeftRight positive = right; negative = left (-100 .. 100)
	 * @param turnLeftRight positive = right; negative = left
	 * @param paramSetName the name of the walk parameter set to use
	 */
	void walk(double forwardsBackwards, double stepLeftRight, Angle turnLeftRight, String paramSetName);

	/**
	 *
	 * @param walkTo global position to walk to
	 * @param speedThere the speed vector we want to have at the destination
	 * @param distanceToFinal distance to the final position we want to walk to (not to the temporary position to avoid
	 *        an obstacle)
	 * @param slowDownDistance distance to this position at which we start to get slower
	 * @param maxSpeedLimit the maximal speed we want to keep
	 * @param paramSetName name of the walk parameter set to use
	 */
	void globalWalk(Pose2D walkTo, Vector3D speedThere, double distanceToFinal, double slowDownDistance,
			double maxSpeedLimit, String paramSetName);
}
