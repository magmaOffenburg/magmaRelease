/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception;

import hso.autonomy.util.geometry.IPose3D;

/**
 * Interface for perceptors representing a 3D transformation.
 *
 * @author Stefan Glaser
 */
public interface ITransformPerceptor extends IPerceptor {
	/**
	 * Retrieve the perceived pose.
	 *
	 * @return the perceived pose
	 */
	IPose3D getPose();
}
