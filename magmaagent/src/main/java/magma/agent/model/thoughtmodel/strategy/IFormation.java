/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy;

import hso.autonomy.util.geometry.Pose2D;

public interface IFormation {
	Pose2D getPlayerPose(int id);
}