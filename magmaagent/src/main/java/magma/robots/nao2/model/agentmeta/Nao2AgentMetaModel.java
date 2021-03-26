/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao2.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.IHingeJointConfiguration;
import java.util.List;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;

public class Nao2AgentMetaModel extends NaoAgentMetaModel
{
	public static final Nao2AgentMetaModel INSTANCE = new Nao2AgentMetaModel();

	public static final String NAME = "Nao2";

	private Nao2AgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO + " 2");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		((IHingeJointConfiguration) getBodyPart(configs, RAnkle).getJointConfiguration()).setMaxSpeed(8.80667f);
		((IHingeJointConfiguration) getBodyPart(configs, RFoot).getJointConfiguration()).setMaxSpeed(3.47234f);

		((IHingeJointConfiguration) getBodyPart(configs, LAnkle).getJointConfiguration()).setMaxSpeed(8.80667f);
		((IHingeJointConfiguration) getBodyPart(configs, LFoot).getJointConfiguration()).setMaxSpeed(3.47234f);

		return configs;
	}
}
