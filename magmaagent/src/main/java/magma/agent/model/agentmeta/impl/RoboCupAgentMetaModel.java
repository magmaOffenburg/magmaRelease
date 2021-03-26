/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.impl.AgentMetaModel;
import hso.autonomy.util.geometry.IPose3D;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Agent meta model class
 *
 * @author Stefan Glaser
 */
public abstract class RoboCupAgentMetaModel extends AgentMetaModel implements IRoboCupAgentMetaModel
{
	protected final String sceneString;

	private final float soccerPositionKneeAngle;

	private final float soccerPositionHipAngle;

	private final int goalPredictionTime;

	private final float cycleTime;

	private final float visionCycleTime;

	protected final Vector3D staticPivotPoint;

	private final float torsoZUpright;

	/**
	 * Constructor.
	 *
	 * @param modelName - the name of this model
	 * @param sceneString - the scene string
	 * @param staticPivotPoint - the static pivot-point related to this robot
	 * @param bodyPartContainingCamera - the name of the body part containing the
	 *        camera
	 */
	public RoboCupAgentMetaModel(String modelName, String sceneString, Vector3D staticPivotPoint,
			String bodyPartContainingCamera, IPose3D cameraOffset, float soccerPositionKneeAngle,
			float soccerPositionHipAngle, float height, int goalPredictionTime, float cycleTime, float visionCycleTime,
			float torsoZUpright)
	{
		super(modelName, bodyPartContainingCamera, cameraOffset, height);
		this.sceneString = sceneString;
		this.staticPivotPoint = staticPivotPoint;
		this.soccerPositionKneeAngle = soccerPositionKneeAngle;
		this.soccerPositionHipAngle = soccerPositionHipAngle;
		this.goalPredictionTime = goalPredictionTime;
		this.cycleTime = cycleTime;
		this.visionCycleTime = visionCycleTime;
		this.torsoZUpright = torsoZUpright;
	}

	@Override
	public String getSceneString()
	{
		return sceneString;
	}

	@Override
	public Vector3D getStaticPivotPoint()
	{
		return staticPivotPoint;
	}

	@Override
	public float getSoccerPositionKneeAngle()
	{
		return soccerPositionKneeAngle;
	}

	@Override
	public float getSoccerPositionHipAngle()
	{
		return soccerPositionHipAngle;
	}

	@Override
	public int getGoalPredictionTime()
	{
		return goalPredictionTime;
	}

	@Override
	public float getCycleTime()
	{
		return cycleTime;
	}

	@Override
	public float getVisionCycleTime()
	{
		return visionCycleTime;
	}

	@Override
	public float getTorsoZUpright()
	{
		return torsoZUpright;
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return false;
	}
}
