/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class RCServerMetaModelV64 extends RCServerMetaModelV63
{
	public static final RCServerMetaModelV64 INSTANCE = new RCServerMetaModelV64();

	@Override
	public int getVersion()
	{
		return 64;
	}

	@Override
	protected Vector2D initFieldDimensions()
	{
		return new Vector2D(21, 14);
	}

	@Override
	protected void initFieldLines()
	{
		double fieldHalfLength = getFieldDimensions().getX() / 2;
		double fieldHalfWidth = getFieldDimensions().getY() / 2;

		// Ground lines
		String type = "FieldLine";
		addFieldLine("UGL", // Upper-ground-line
				type, new Vector3D(-fieldHalfLength, fieldHalfWidth, 0),
				new Vector3D(fieldHalfLength, fieldHalfWidth, 0));

		addFieldLine("LGL", // Lower-ground-line
				type, new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0),
				new Vector3D(fieldHalfLength, -fieldHalfWidth, 0));

		// Side lines
		addFieldLine("LSL", // Left-Side-Line
				type, new Vector3D(-fieldHalfLength, fieldHalfWidth, 0),
				new Vector3D(-fieldHalfLength, -fieldHalfWidth, 0));

		addFieldLine("RSL", // Right-side-line
				type, new Vector3D(fieldHalfLength, fieldHalfWidth, 0),
				new Vector3D(fieldHalfLength, -fieldHalfWidth, 0));

		// Middle line
		addFieldLine("ML", // Middle line
				type, new Vector3D(0, fieldHalfWidth, 0), new Vector3D(0, -fieldHalfWidth, 0));

		double penaltyHalfLength = getPenaltyHalfLength();
		double penaltyWidth = getPenaltyAreaDimensions().getX();

		// Left penalty area lines
		addFieldLine("LPAUL", // Left-penalty-area-upper-line
				type, new Vector3D(-fieldHalfLength, penaltyHalfLength, 0),
				new Vector3D(-fieldHalfLength + penaltyWidth, penaltyHalfLength, 0));

		addFieldLine("LPALL", // Left-penalty-area-lower-line
				type, new Vector3D(-fieldHalfLength, -penaltyHalfLength, 0),
				new Vector3D(-fieldHalfLength + penaltyWidth, -penaltyHalfLength, 0));

		addFieldLine("LPAFL", // Left-penalty-area-front-line
				type, new Vector3D(-fieldHalfLength + penaltyWidth, penaltyHalfLength, 0),
				new Vector3D(-fieldHalfLength + penaltyWidth, -penaltyHalfLength, 0));

		// Right penalty area lines
		addFieldLine("RPAUL", // Right-penalty-area-upper-line
				type, new Vector3D(fieldHalfLength, penaltyHalfLength, 0),
				new Vector3D(fieldHalfLength - penaltyWidth, penaltyHalfLength, 0));

		addFieldLine("RPALL", // Right-penalty-area-lower-line
				type, new Vector3D(fieldHalfLength, -penaltyHalfLength, 0),
				new Vector3D(fieldHalfLength - penaltyWidth, -penaltyHalfLength, 0));

		addFieldLine("RPAFL", // Right-penalty-area-front-line
				type, new Vector3D(fieldHalfLength - penaltyWidth, penaltyHalfLength, 0),
				new Vector3D(fieldHalfLength - penaltyWidth, -penaltyHalfLength, 0));

		// Middle circle lines
		double px1 = getMiddleCircleRadius();
		double py1 = 0;
		double px2, py2;

		for (int deg = 36; deg <= 360; deg += 36) {
			px2 = Math.cos(Math.toRadians(deg)) * getMiddleCircleRadius();
			py2 = Math.sin(Math.toRadians(deg)) * getMiddleCircleRadius();

			addFieldLine("MC_" + (deg - 36) + "-" + deg, type, new Vector3D(px1, py1, 0), new Vector3D(px2, py2, 0));

			px1 = px2;
			py1 = py2;
		}
	}

	protected double getPenaltyHalfLength()
	{
		return getPenaltyAreaDimensions().getY() / 2 + getGoalDimensions().getY() / 2;
	}
}
