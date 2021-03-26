/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV63 extends RCServerMetaModelV62
{
	public static final RCServerMetaModelV63 INSTANCE = new RCServerMetaModelV63();

	@Override
	public int getVersion()
	{
		return 63;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(18, 12);
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.6, 2.1, 0.8);
	}

	@Override
	protected Vector2D initPenaltyAreaDimensions()
	{
		return new Vector2D(1.8, 3.9);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 1.8f;
	}
}
