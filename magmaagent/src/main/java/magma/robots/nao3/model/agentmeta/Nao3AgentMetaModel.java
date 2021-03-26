/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao3.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import java.util.List;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Nao3AgentMetaModel extends NaoAgentMetaModel
{
	public static final Nao3AgentMetaModel INSTANCE = new Nao3AgentMetaModel();

	public static final String NAME = "Nao3";

	private Nao3AgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO + " 3");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		getBodyPart(configs, RElbow).setTranslation(new Vector3D(-0.01, 0.11, 0.009));
		getBodyPart(configs, RHip1).setTranslation(new Vector3D(0.066, -0.01, -0.115));
		getBodyPart(configs, RThight)
				.setTranslation(new Vector3D(0, 0.01, -0.06f))
				.setJointAnchor(new Vector3D(0, -0.01, 0.05832f));
		getBodyPart(configs, RAnkle).setTranslation(new Vector3D(0, -0.01, -0.075));

		getBodyPart(configs, LElbow).setTranslation(new Vector3D(0.01, 0.11, 0.009));
		getBodyPart(configs, LHip1).setTranslation(new Vector3D(-0.066, -0.01, -0.115));
		getBodyPart(configs, LThight)
				.setTranslation(new Vector3D(0, 0.01, -0.06f))
				.setJointAnchor(new Vector3D(0, -0.01, 0.05832f));
		getBodyPart(configs, LAnkle).setTranslation(new Vector3D(0, -0.01, -0.075));

		return configs;
	}
}
