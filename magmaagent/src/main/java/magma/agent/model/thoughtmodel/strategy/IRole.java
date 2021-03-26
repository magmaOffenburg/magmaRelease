/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy;

import hso.autonomy.util.geometry.IPose2D;

/**
 * Interface for all roles our team can take. The position specifies where the
 * player is situated on the field (horizontal viewpoint).
 *
 * @author Stephan Kammerer
 */
public interface IRole {
	/**
	 * @return the name of the role
	 */
	String getName();

	/**
	 * Update the internal state of the role.
	 */
	void update();

	/**
	 * @return the target pose
	 */
	IPose2D getTargetPose();

	IPose2D getIndependentTargetPose();

	/**
	 * Retrieve the priority of the role.
	 *
	 * @return the priority of the role
	 */
	float getPriority();
}
