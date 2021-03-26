/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.model.agentmeta;

import hso.autonomy.agent.model.agentmeta.IBodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.BodyPartConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.HingeJointConfiguration;
import hso.autonomy.agent.model.agentmeta.impl.SensorConfiguration;
import java.util.List;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class NaoToeAgentMetaModel extends NaoAgentMetaModel
{
	public static final NaoToeAgentMetaModel INSTANCE = new NaoToeAgentMetaModel();

	public static final String NAME = "NaoToe";

	private NaoToeAgentMetaModel()
	{
		super(NAME, ACTION_SCENE_HETERO_TOE + " 4");
	}

	@Override
	protected List<IBodyPartConfiguration> createBodyPartConfigs()
	{
		List<IBodyPartConfiguration> configs = super.createBodyPartConfigs();

		getBodyPart(configs, RFoot)
				.setTranslation(new Vector3D(0, 0.01, -0.035))
				.setMass(0.15f)
				.setGeometry(new Vector3D(0.08, 0.12, 0.02))
				.setJointAnchor(new Vector3D(0, -0.01, 0.035));

		configs.add(new BodyPartConfiguration(RToe, RFoot, new Vector3D(0, 0.08, -0.005), 0.05f,
				new Vector3D(0.08, 0.04, 0.01),
				new HingeJointConfiguration(RToePitch, RToesP, RToesE, Vector3D.PLUS_I, -1, 70, MAX_JOINT_SPEED, true),
				new Vector3D(0, -0.02, -0.005), null, null, new SensorConfiguration("RToeForce", "rf1"), null, null,
				null, null));

		getBodyPart(configs, LFoot)
				.setTranslation(new Vector3D(0, 0.01, -0.035))
				.setMass(0.15f)
				.setGeometry(new Vector3D(0.08, 0.12, 0.02))
				.setJointAnchor(new Vector3D(0, -0.01, 0.035));

		configs.add(new BodyPartConfiguration(LToe, LFoot, new Vector3D(0, 0.08, -0.005), 0.05f,
				new Vector3D(0.08, 0.04, 0.01),
				new HingeJointConfiguration(LToePitch, LToesP, LToesE, Vector3D.PLUS_I, -1, 70, MAX_JOINT_SPEED, true),
				new Vector3D(0, -0.02, -0.005), null, null, new SensorConfiguration("LToeForce", "lf1"), null, null,
				null, null));

		return configs;
	}
}
