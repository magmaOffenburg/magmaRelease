/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action;

import hso.autonomy.util.geometry.IPose2D;

/**
 * Effector interface for a beam action in SimSpark.
 *
 * @author Stefan Glaser
 */
public interface IBeamEffector {
	/**
	 * Retrieve the 2D beam pose.
	 *
	 * @return the 2D beam pose
	 */
	IPose2D getBeam();
}
