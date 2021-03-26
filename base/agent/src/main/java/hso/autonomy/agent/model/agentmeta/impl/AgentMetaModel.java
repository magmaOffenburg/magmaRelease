/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.model.agentmeta.impl;

import hso.autonomy.agent.model.agentmeta.IAgentMetaModel;
import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import hso.autonomy.util.geometry.IPose3D;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Agent meta model class.
 *
 * @author Stefan Glaser
 */
public abstract class AgentMetaModel implements IAgentMetaModel
{
	protected final String modelName;

	protected List<IBodyPartConfiguration> bodyPartConfigs;

	protected String bodyPartContainingCamera;

	protected IPose3D cameraOffset;

	protected float height;

	protected IBodyPartConfiguration rootBodyConfig;

	protected List<IHingeJointConfiguration> jointConfigs;

	protected List<String> jointNames;

	protected List<String> jointPerceptorNames;

	protected List<String> jointEffectorNames;

	/**
	 * Constructor.
	 *
	 * @param modelName - the name of this model
	 * @param bodyPartContainingCamera - the name of the body part containing the
	 *        camera
	 */
	public AgentMetaModel(String modelName, String bodyPartContainingCamera, IPose3D cameraOffset, float height)
	{
		this.modelName = modelName;
		this.bodyPartContainingCamera = bodyPartContainingCamera;
		this.cameraOffset = cameraOffset;
		this.height = height;

		// create body parts
		bodyPartConfigs = createBodyPartConfigs();

		rootBodyConfig =
				bodyPartConfigs.stream().filter(bodyPart -> bodyPart.getParent() == null).findFirst().orElse(null);

		jointConfigs =
				Collections.unmodifiableList(bodyPartConfigs.stream()
													 .filter(bodyPart -> bodyPart.getJointConfiguration() != null)
													 .map(IBodyPartConfiguration::getJointConfiguration)
													 .collect(Collectors.toList()));
		jointNames = Collections.unmodifiableList(
				jointConfigs.stream().map(IHingeJointConfiguration::getName).collect(Collectors.toList()));
		jointPerceptorNames = Collections.unmodifiableList(
				jointConfigs.stream().map(IHingeJointConfiguration::getPerceptorName).collect(Collectors.toList()));
		jointEffectorNames = Collections.unmodifiableList(
				jointConfigs.stream().map(IHingeJointConfiguration::getEffectorName).collect(Collectors.toList()));
	}

	protected abstract List<IBodyPartConfiguration> createBodyPartConfigs();

	@Override
	public String getName()
	{
		return modelName;
	}

	@Override
	public List<IBodyPartConfiguration> getBodyPartConfigurations()
	{
		return bodyPartConfigs;
	}

	@Override
	public String getNameOfCameraBodyPart()
	{
		return bodyPartContainingCamera;
	}

	@Override
	public IPose3D getCameraOffset()
	{
		return cameraOffset;
	}

	@Override
	public List<IBodyPartConfiguration> getChildBodyConfigurations(IBodyPartConfiguration bodyPart)
	{
		if (bodyPart != null) {
			String parentName = bodyPart.getName();

			return bodyPartConfigs.stream()
					.filter(body -> parentName.equals(body.getParent()))
					.collect(Collectors.toList());
		}

		return Collections.emptyList();
	}

	@Override
	public IBodyPartConfiguration getRootBodyConfiguration()
	{
		return rootBodyConfig;
	}

	@Override
	public List<IHingeJointConfiguration> getAvailableJoints()
	{
		return jointConfigs;
	}

	@Override
	public List<String> getAvailableJointNames()
	{
		return jointNames;
	}

	@Override
	public List<String> getJointPerceptorNames()
	{
		return jointPerceptorNames;
	}

	@Override
	public List<String> getAvailableEffectorNames()
	{
		return jointEffectorNames;
	}

	@Override
	public float getHeight()
	{
		return height;
	}
}
