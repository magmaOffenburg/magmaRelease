/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.util.geometry.IPose3D;
import java.util.List;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class AgentMetaModelDecorator implements IRoboCupAgentMetaModel
{
	private final IRoboCupAgentMetaModel decoratee;

	public AgentMetaModelDecorator(IRoboCupAgentMetaModel decoratee)
	{
		this.decoratee = decoratee;
	}

	@Override
	public String getName()
	{
		return decoratee.getName();
	}

	@Override
	public String getSceneString()
	{
		return decoratee.getSceneString();
	}

	@Override
	public Vector3D getStaticPivotPoint()
	{
		return decoratee.getStaticPivotPoint();
	}

	@Override
	public List<IBodyPartConfiguration> getBodyPartConfigurations()
	{
		return decoratee.getBodyPartConfigurations();
	}

	@Override
	public String getNameOfCameraBodyPart()
	{
		return decoratee.getNameOfCameraBodyPart();
	}

	@Override
	public IPose3D getCameraOffset()
	{
		return decoratee.getCameraOffset();
	}

	@Override
	public List<IHingeJointConfiguration> getAvailableJoints()
	{
		return decoratee.getAvailableJoints();
	}

	@Override
	public List<IBodyPartConfiguration> getChildBodyConfigurations(IBodyPartConfiguration bodyPart)
	{
		return decoratee.getChildBodyConfigurations(bodyPart);
	}

	@Override
	public IBodyPartConfiguration getRootBodyConfiguration()
	{
		return decoratee.getRootBodyConfiguration();
	}

	@Override
	public List<String> getAvailableJointNames()
	{
		return decoratee.getAvailableJointNames();
	}

	@Override
	public List<String> getJointPerceptorNames()
	{
		return decoratee.getJointPerceptorNames();
	}

	@Override
	public List<String> getAvailableEffectorNames()
	{
		return decoratee.getAvailableEffectorNames();
	}

	@Override
	public float getSoccerPositionKneeAngle()
	{
		return decoratee.getSoccerPositionKneeAngle();
	}

	@Override
	public float getSoccerPositionHipAngle()
	{
		return decoratee.getSoccerPositionHipAngle();
	}

	@Override
	public float getHeight()
	{
		return decoratee.getHeight();
	}

	@Override
	public int getGoalPredictionTime()
	{
		return decoratee.getGoalPredictionTime();
	}

	@Override
	public float getCycleTime()
	{
		return decoratee.getCycleTime();
	}

	@Override
	public float getVisionCycleTime()
	{
		return decoratee.getVisionCycleTime();
	}

	@Override
	public float getTorsoZUpright()
	{
		return decoratee.getTorsoZUpright();
	}

	@Override
	public boolean hasFootForceSensors()
	{
		return decoratee.hasFootForceSensors();
	}
}
