/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV62 extends RoboCupWorldMetaModel
{
	public static final RCServerMetaModelV62 INSTANCE = new RCServerMetaModelV62();

	@Override
	public int getVersion()
	{
		return 62;
	}

	@Override
	public float getBallDecay()
	{
		return 0.94f;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(12, 8);
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.4, 1.4, 0.8);
	}

	@Override
	protected Vector2D initPenaltyAreaDimensions()
	{
		return new Vector2D(1.2, 2.6);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 1;
	}

	@Override
	public float getBallRadius()
	{
		return 0.042f;
	}

	@Override
	protected void initLandMarks()
	{
		double fieldHalfLength = getFieldDimensions().getX() / 2;
		double fieldHalfWidth = getFieldDimensions().getY() / 2;
		double goalHalfWidth = getGoalDimensions().getY() / 2;
		double goalHeight = getGoalDimensions().getZ();

		// left goal posts
		String type = "Goalpost";
		addLandmark("G1L", type, new Vector3D(-fieldHalfLength, goalHalfWidth, goalHeight));
		addLandmark("G2L", type, new Vector3D(-fieldHalfLength, -goalHalfWidth, goalHeight));
		// right goal posts
		addLandmark("G1R", type, new Vector3D(fieldHalfLength, goalHalfWidth, goalHeight));
		addLandmark("G2R", type, new Vector3D(fieldHalfLength, -goalHalfWidth, goalHeight));

		// flags
		type = "Flag";
		addLandmark("F1L", type, new Vector3D(-fieldHalfLength, fieldHalfWidth, 0.0f));
		addLandmark("F2L", type, new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0.0f));
		addLandmark("F1R", type, new Vector3D(fieldHalfLength, fieldHalfWidth, 0.0f));
		addLandmark("F2R", type, new Vector3D(fieldHalfLength, -fieldHalfWidth, 0.0f));
	}

	@Override
	protected void initFieldLines()
	{
	}
}
