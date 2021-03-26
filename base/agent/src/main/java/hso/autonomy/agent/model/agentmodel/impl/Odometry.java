/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmodel.impl;

import hso.autonomy.agent.communication.perception.IPerception;
import hso.autonomy.agent.model.agentmodel.IOdometryErrorModel;
import hso.autonomy.agent.model.agentmodel.ISensor;
import hso.autonomy.util.geometry.IPose3D;
import hso.autonomy.util.geometry.Pose3D;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Base class for all odometry classes that are able to keep track of our pose.
 */
public class Odometry extends VirtualSensor
{
	private final IOdometryErrorModel errorModel;

	/** the current pose estimation */
	protected IPose3D pose;

	private IPose3D lastReceivedDelta;

	public Odometry(IPose3D initialPoseEstimation, IOdometryErrorModel errorModel)
	{
		super("Odometry");
		this.errorModel = errorModel;
		if (initialPoseEstimation == null) {
			initialPoseEstimation = new Pose3D();
		}
		pose = initialPoseEstimation;
	}

	public IPose3D getPose()
	{
		return pose;
	}

	public void init(IPose3D initialPoseEstimation)
	{
		this.pose = initialPoseEstimation;
	}

	public void update(IPose3D delta)
	{
		this.lastReceivedDelta = delta;
		this.pose = pose.applyTo(delta);
	}

	public void setPose(IPose3D pose)
	{
		this.pose = pose;
	}

	public IPose3D getLastReceivedDelta()
	{
		return lastReceivedDelta;
	}

	public void setPosition(Vector3D position)
	{
		this.pose = new Pose3D(position, pose.getOrientation());
	}

	@Override
	public void updateFromPerception(IPerception perception)
	{
	}

	@Override
	public ISensor copy()
	{
		return null;
	}

	public IOdometryErrorModel getErrorModel()
	{
		return errorModel;
	}
}
