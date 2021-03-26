/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao1.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import java.util.List;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Nao1AgentMetaModel extends NaoAgentMetaModel
{
	public static final Nao1AgentMetaModel INSTANCE = new Nao1AgentMetaModel();

	public static final String NAME = "Nao1";

	private Nao1AgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO + " 1");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		getBodyPart(configs, RElbow).setTranslation(new Vector3D(-0.01, 0.10664, 0.009));
		getBodyPart(configs, RThight)
				.setTranslation(new Vector3D(0, 0.01, -0.05832f))
				.setJointAnchor(new Vector3D(0, -0.01, 0.05832f));
		getBodyPart(configs, RAnkle).setTranslation(new Vector3D(0, -0.01, -0.07332));

		getBodyPart(configs, LElbow).setTranslation(new Vector3D(0.01, 0.10664, 0.009));
		getBodyPart(configs, LThight)
				.setTranslation(new Vector3D(0, 0.01, -0.05832f))
				.setJointAnchor(new Vector3D(0, -0.01, 0.05832f));
		getBodyPart(configs, LAnkle).setTranslation(new Vector3D(0, -0.01, -0.07332));

		return configs;
	}
}
