/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.IGlobalPosePerceptor;
import hso.autonomy.util.geometry.Pose3D;

/**
 * @author Simon Raffeiner
 */
public class GlobalPosePerceptor extends Perceptor implements IGlobalPosePerceptor
{
	/** torso pose in global coordinate system */
	private Pose3D globalPose;

	public GlobalPosePerceptor(Pose3D globalPose)
	{
		super("globalPose");

		this.globalPose = globalPose;
	}

	@Override
	public Pose3D getGlobalPose()
	{
		return globalPose;
	}
}
