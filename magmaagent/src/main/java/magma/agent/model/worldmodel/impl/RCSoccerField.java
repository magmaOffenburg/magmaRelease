/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmodel.impl;

import hso.autonomy.agent.model.worldmodel.localizer.ILineFeature;
import hso.autonomy.agent.model.worldmodel.localizer.IPointFeature;
import hso.autonomy.agent.model.worldmodel.localizer.impl.FeatureMap;
import hso.autonomy.util.geometry.Area2D;
import hso.autonomy.util.geometry.Area2D.Float;
import java.util.Map;
import magma.agent.model.worldmodel.IRCSoccerField;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * This class describes a soccer field environment for the RoboCup competitions.
 *
 * @author Stefan Glaser
 */
public class RCSoccerField extends FeatureMap implements IRCSoccerField
{
	/** The half length of the soccer pitch (global x) */
	public final float fieldHalfLength;

	/** The half width of the soccer pitch (global y) */
	public final float fieldHalfWidth;

	/** The half width of the goals (global y) */
	public final float goalHalfWidth;

	/** The height of the goals (global z) */
	public final float goalHeight;

	/** The depth of the goals (global x) */
	public final float goalDepth;

	/** The width of the penalty areas (global x) */
	public final float penaltyWidth;

	/** The half length of the penalty areas (global y) - does NOT include the goal width! */
	public final float penaltyHalfLength;

	/** The radius of the center circle */
	public final float centerCircleRadius;

	/** The position of the own goal (between the goal posts) */
	public final Vector3D ownGoalPosition;

	/** The position of the other goal (between the goal posts) */
	public final Vector3D otherGoalPosition;

	/** The field area. */
	public final Area2D.Float fieldArea;

	public RCSoccerField(Map<String, IPointFeature> pointFeatures, Map<String, ILineFeature> lineFeatures,
			float fieldHalfLength, float fieldHalfWidth, float goalDepth, float goalHalfWidth, float goalHeight,
			float penaltyWidth, float penaltyHalfLength, float centerCircleRadius)
	{
		super(pointFeatures, lineFeatures);

		this.fieldHalfLength = fieldHalfLength;
		this.fieldHalfWidth = fieldHalfWidth;
		this.goalDepth = goalDepth;
		this.goalHalfWidth = goalHalfWidth;
		this.goalHeight = goalHeight;
		this.penaltyWidth = penaltyWidth;
		this.penaltyHalfLength = penaltyHalfLength;
		this.centerCircleRadius = centerCircleRadius;

		ownGoalPosition = new Vector3D(-fieldHalfLength, 0, 0);
		otherGoalPosition = new Vector3D(fieldHalfLength, 0, 0);

		fieldArea = new Area2D.Float(-fieldHalfLength, fieldHalfLength, -fieldHalfWidth, fieldHalfWidth);
	}

	@Override
	public float fieldHalfLength()
	{
		return fieldHalfLength;
	}

	@Override
	public float fieldHalfWidth()
	{
		return fieldHalfWidth;
	}

	@Override
	public float goalHalfWidth()
	{
		return goalHalfWidth;
	}

	@Override
	public float goalHeight()
	{
		return goalHeight;
	}

	@Override
	public float goalDepth()
	{
		return goalDepth;
	}

	@Override
	public float penaltyWidth()
	{
		return penaltyWidth;
	}

	@Override
	public float penaltyHalfLength()
	{
		return penaltyHalfLength;
	}

	@Override
	public float centerCircleRadius()
	{
		return centerCircleRadius;
	}

	@Override
	public Vector3D getOwnGoalPosition()
	{
		return ownGoalPosition;
	}

	@Override
	public Vector3D getOtherGoalPosition()
	{
		return otherGoalPosition;
	}

	@Override
	public Float getFieldArea()
	{
		return fieldArea;
	}
}
