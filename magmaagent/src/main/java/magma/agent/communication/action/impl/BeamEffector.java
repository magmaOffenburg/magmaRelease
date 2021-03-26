/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.communication.action.impl;

import hso.autonomy.agent.communication.action.impl.Effector;
import hso.autonomy.util.geometry.Angle;
import hso.autonomy.util.geometry.IPose2D;
import hso.autonomy.util.geometry.Pose2D;
import magma.agent.communication.action.IBeamEffector;

/**
 * Implementation of the SimSpark "beam" effector.
 *
 * @author Stefan Glaser
 */
public class BeamEffector extends Effector implements IBeamEffector
{
	/** The target pose to beam to. */
	private IPose2D beamPose;

	public BeamEffector()
	{
		this(new Pose2D());
	}

	public BeamEffector(IPose2D pose)
	{
		super("beam");

		this.beamPose = pose;
	}

	public BeamEffector(double x, double y, Angle theta)
	{
		this(new Pose2D(x, y, theta));
	}

	/**
	 * Set beam coordinates
	 */
	public void setBeamPose(IPose2D beamPose)
	{
		this.beamPose = beamPose;
	}

	/**
	 * Set beam coordinates
	 */
	public void setBeamPose(double x, double y, Angle theta)
	{
		setBeamPose(new Pose2D(x, y, theta));
	}

	@Override
	public IPose2D getBeam()
	{
		return beamPose;
	}
}
