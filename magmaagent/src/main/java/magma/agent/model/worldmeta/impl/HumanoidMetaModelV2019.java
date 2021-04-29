/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class HumanoidMetaModelV2019 extends HumanoidMetaModelV2017
{
	public static final HumanoidMetaModelV2019 INSTANCE = new HumanoidMetaModelV2019();

	@Override
	public int getVersion()
	{
		return 2019;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(14, 9);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 1.5f;
	}

	@Override
	protected void initLandMarks()
	{
		super.initLandMarks();

		// replace penalty marks at the field
		String type = "P_junction";
		addLandmark("Psmx", type, new Vector3D(-5f, 0.0, 0.0));
		addLandmark("Pomx", type, new Vector3D(5f, 0.0, 0.0));
	}
}
