/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class HumanoidMetaModelV2014 extends RCServerMetaModelV66
{
	public static final HumanoidMetaModelV2014 INSTANCE = new HumanoidMetaModelV2014();

	@Override
	public int getVersion()
	{
		return 2014;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(9, 6);
	}

	@Override
	protected Vector3D initGoalDimensions()
	{
		return new Vector3D(0.6, 2.6, 1.8);
	}

	@Override
	protected Vector2D initPenaltyAreaDimensions()
	{
		return new Vector2D(1, 5);
	}

	@Override
	public float getMiddleCircleRadius()
	{
		return 0.75f;
	}

	@Override
	public float getBallRadius()
	{
		return 0.11f;
	}

	@Override
	protected double getPenaltyHalfLength()
	{
		return getPenaltyAreaDimensions().getY() / 2;
	}

	@Override
	protected void initLandMarks()
	{
		double fieldHalfLength = getFieldDimensions().getX() / 2;
		double fieldHalfWidth = getFieldDimensions().getY() / 2;
		double penaltyAreaHalfLength = getPenaltyHalfLength();
		double penaltyAreaWidth = getPenaltyAreaDimensions().getX();
		double goalHalfWidth = getGoalDimensions().getY() / 2;

		// all possible t-corners at the field
		String type = "T_junction";
		addLandmark("Tsla", type, new Vector3D(-fieldHalfLength, penaltyAreaHalfLength, 0.0));
		addLandmark("Tsra", type, new Vector3D(-fieldHalfLength, -penaltyAreaHalfLength, 0.0));
		addLandmark("Tclf", type, new Vector3D(0.0f, fieldHalfWidth, 0.0));
		addLandmark("Tcrf", type, new Vector3D(0.0f, -fieldHalfWidth, 0.0));
		addLandmark("Tola", type, new Vector3D(fieldHalfLength, penaltyAreaHalfLength, 0.0));
		addLandmark("Tora", type, new Vector3D(fieldHalfLength, -penaltyAreaHalfLength, 0.0));

		// all possible l-corners at the field
		type = "L_junction";
		addLandmark("Lslf", type, new Vector3D(-fieldHalfLength, fieldHalfWidth, 0.0));
		addLandmark("Lsrf", type, new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0.0));
		addLandmark("Lsla", type, new Vector3D(-(fieldHalfLength - penaltyAreaWidth), penaltyAreaHalfLength, 0.0));
		addLandmark("Lsra", type, new Vector3D(-(fieldHalfLength - penaltyAreaWidth), -penaltyAreaHalfLength, 0.0));
		addLandmark("Lola", type, new Vector3D((fieldHalfLength - penaltyAreaWidth), penaltyAreaHalfLength, 0.0));
		addLandmark("Lora", type, new Vector3D((fieldHalfLength - penaltyAreaWidth), -penaltyAreaHalfLength, 0.0));
		addLandmark("Lolf", type, new Vector3D(fieldHalfLength, fieldHalfWidth, 0.0));
		addLandmark("Lorf", type, new Vector3D(fieldHalfLength, -fieldHalfWidth, 0.0));

		// X-crosses at the field
		type = "X_junction";
		addLandmark("Xclc", type, new Vector3D(0.0f, (getMiddleCircleRadius()), 0.0));
		addLandmark("Xcrc", type, new Vector3D(0.0f, -(getMiddleCircleRadius()), 0.0));

		// penalty marks at the field
		type = "P_junction";
		addLandmark("Psmx", type, new Vector3D(-2.4f, 0.0, 0.0));
		addLandmark("Pomx", type, new Vector3D(2.4f, 0.0, 0.0));

		// goal posts at the field
		type = "Posts";
		addLandmark("Gsrg", type, new Vector3D(-fieldHalfLength, -goalHalfWidth, 0.0));
		addLandmark("Gslg", type, new Vector3D(-fieldHalfLength, goalHalfWidth, 0.0));
		addLandmark("Gorg", type, new Vector3D(fieldHalfLength, -goalHalfWidth, 0.0));
		addLandmark("Golg", type, new Vector3D(fieldHalfLength, goalHalfWidth, 0.0));
	}
}
