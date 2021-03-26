/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV66 extends RCServerMetaModelV64
{
	public static final RCServerMetaModelV66 INSTANCE = new RCServerMetaModelV66();

	@Override
	public int getVersion()
	{
		return 66;
	}

	@Override
	public float getBallDecay()
	{
		return 0.987f;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(30, 20);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 2;
	}
}
