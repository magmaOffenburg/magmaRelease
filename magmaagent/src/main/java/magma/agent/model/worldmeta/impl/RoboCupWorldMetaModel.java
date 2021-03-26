/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.worldmeta.impl;

import java.util.HashMap;
import java.util.Map;
import magma.agent.model.worldmeta.ILineFeatureConfiguration;
import magma.agent.model.worldmeta.IPointFeatureConfiguration;
import magma.agent.model.worldmeta.IRoboCupWorldMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public abstract class RoboCupWorldMetaModel implements IRoboCupWorldMetaModel
{
	protected Vector2D fieldDimensions;

	protected Vector3D goalDimensions;

	protected Vector2D penaltyAreaDimensions;

	protected Map<String, IPointFeatureConfiguration> landmarks;

	protected Map<String, ILineFeatureConfiguration> fieldLines;

	@Override
	public final Vector2D getFieldDimensions()
	{
		if (fieldDimensions == null) {
			fieldDimensions = initFieldDimensions();
		}
		return fieldDimensions;
	}

	@Override
	public final Vector3D getGoalDimensions()
	{
		if (goalDimensions == null) {
			goalDimensions = initGoalDimensions();
		}
		return goalDimensions;
	}

	@Override
	public final Vector2D getPenaltyAreaDimensions()
	{
		if (penaltyAreaDimensions == null) {
			penaltyAreaDimensions = initPenaltyAreaDimensions();
		}
		return penaltyAreaDimensions;
	}

	@Override
	public final Map<String, IPointFeatureConfiguration> getLandmarks()
	{
		if (landmarks == null) {
			landmarks = new HashMap<>();
			initLandMarks();
		}
		return landmarks;
	}

	protected void addLandmark(String name, String type, Vector3D knownPosition)
	{
		landmarks.put(name, new PointFeatureConfiguration(name, type, knownPosition));
	}

	@Override
	public final Map<String, ILineFeatureConfiguration> getFieldLines()
	{
		if (fieldLines == null) {
			fieldLines = new HashMap<>();
			initFieldLines();
		}
		return fieldLines;
	}

	protected void addFieldLine(String name, String type, Vector3D knownPosition1, Vector3D knownPosition2)
	{
		fieldLines.put(name, new LineFeatureConfiguration(name, type, knownPosition1, knownPosition2));
	}

	protected abstract Vector2D initFieldDimensions();

	protected abstract Vector3D initGoalDimensions();

	protected abstract Vector2D initPenaltyAreaDimensions();

	protected abstract void initLandMarks();

	protected abstract void initFieldLines();
}
