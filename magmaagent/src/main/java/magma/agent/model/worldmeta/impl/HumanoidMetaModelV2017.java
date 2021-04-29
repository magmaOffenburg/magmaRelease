/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class HumanoidMetaModelV2017 extends HumanoidMetaModelV2014
{
	public static final HumanoidMetaModelV2017 INSTANCE = new HumanoidMetaModelV2017();

	@Override
	public int getVersion()
	{
		return 2017;
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.6, 2.6, 1.8);
	}
}
