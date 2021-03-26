/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface IRoboCupAgentMetaModel extends IAgentMetaModel {
	/**
	 * Returns the corresponding action scene string, to this meta model.
	 *
	 * @return the scene-string
	 */
	String getSceneString();

	/**
	 * Returns the static pivot-point used as replacement of the CoM in the
	 * balancing engine related movements. This pivot-point is usually close to
	 * the initial CoM location or somewhere in the pelvis of the robot.
	 *
	 * @return the static pivot-point
	 */
	Vector3D getStaticPivotPoint();

	float getSoccerPositionKneeAngle();

	float getSoccerPositionHipAngle();

	int getGoalPredictionTime();

	float getCycleTime();

	float getVisionCycleTime();

	/**
	 * @return the z coordinate of the torso center when the robot is upright
	 */
	float getTorsoZUpright();

	boolean hasFootForceSensors();
}
