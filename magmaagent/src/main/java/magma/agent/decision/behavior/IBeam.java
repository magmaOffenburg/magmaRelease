/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.util.geometry.IPose2D;

/**
 * Interface for a beam behavior that can move the agent in 2D space.
 *
 * @author Stefan Glaser
 */
public interface IBeam extends IBehavior {
	/**
	 * Set target beaming position
	 */
	void setPose(IPose2D target);

	/**
	 * Retrieve the beam target pose.
	 *
	 * @return the target pose
	 */
	IPose2D getPose();
}
