/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.communication.perception.impl;

import hso.autonomy.agent.communication.perception.ITransformPerceptor;
import hso.autonomy.util.geometry.IPose3D;

/**
 * Default implementation for {@link ITransformPerceptor}.
 *
 * @author Stefan Glaser
 */
public class TransformPerceptor extends Perceptor implements ITransformPerceptor
{
	/** The pose representing the transformation. */
	private final IPose3D pose;

	public TransformPerceptor(String name, IPose3D pose)
	{
		super(name);

		this.pose = pose;
	}

	@Override
	public IPose3D getPose()
	{
		return pose;
	}
}
