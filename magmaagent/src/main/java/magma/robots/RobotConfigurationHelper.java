/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import magma.agent.IMagmaConstants;
import magma.agent.agentruntime.ComponentFactory;
import magma.agent.agentruntime.ThinClientComponentFactory;
import magma.agent.model.agentmeta.impl.RoboCupAgentMetaModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao.general.agentruntime.PenaltyComponentFactory;
import magma.robots.nao.model.agentmeta.NaoAgentMetaModel;
import magma.robots.nao1.general.agentruntime.Nao1ComponentFactory;
import magma.robots.nao1.model.agentmeta.Nao1AgentMetaModel;
import magma.robots.nao2.general.agentruntime.Nao2ComponentFactory;
import magma.robots.nao2.model.agentmeta.Nao2AgentMetaModel;
import magma.robots.nao3.general.agentruntime.Nao3ComponentFactory;
import magma.robots.nao3.model.agentmeta.Nao3AgentMetaModel;
import magma.robots.naoGazebo.general.agentruntime.NaoGazeboComponentFactory;
import magma.robots.naotoe.general.agentruntime.NaoToeComponentFactory;
import magma.robots.naotoe.model.agentmeta.NaoToeAgentMetaModel;

public class RobotConfigurationHelper
{
	@FunctionalInterface
	public interface ComponentFactoryConstructor {
		ComponentFactory create();
	}

	/**
	 * A read-only list of all available robot model names.
	 */
	public static final Map<String, ComponentFactoryConstructor> ROBOT_MODELS;

	static
	{
		Map<String, ComponentFactoryConstructor> models = new LinkedHashMap<>();
		models.put(IMagmaConstants.DEFAULT_FACTORY, null);
		models.put(NaoAgentMetaModel.NAME, NaoComponentFactory::new);
		models.put(Nao1AgentMetaModel.NAME, Nao1ComponentFactory::new);
		models.put(Nao2AgentMetaModel.NAME, Nao2ComponentFactory::new);
		models.put(Nao3AgentMetaModel.NAME, Nao3ComponentFactory::new);
		models.put(NaoToeAgentMetaModel.NAME, NaoToeComponentFactory::new);
		models.put(PenaltyComponentFactory.NAME, PenaltyComponentFactory::new);
		models.put(NaoGazeboComponentFactory.NAME, NaoGazeboComponentFactory::new);
		ROBOT_MODELS = Collections.unmodifiableMap(models);
	}

	public static ComponentFactory getComponentFactory(String robotModel)
	{
		return getComponentFactory(robotModel, -1, false);
	}

	public static ComponentFactory getComponentFactory(String robotModel, int playerNumber, boolean thinClient)
	{
		ComponentFactory factory;
		if (robotModel == null || robotModel.equals(IMagmaConstants.DEFAULT_FACTORY)) {
			factory = getComponentFactory(playerNumber);
		} else {
			factory = ROBOT_MODELS.get(robotModel).create();
		}

		if (thinClient) {
			factory = new ThinClientComponentFactory(factory);
		}

		return factory;
	}

	private static ComponentFactory getComponentFactory(int playerNumber)
	{
		String name;

		switch (playerNumber) {
		case 1:
		case 3:
		case 7:
		case 8:
		case 9:
		case 10:
		case 11:
			name = NaoToeAgentMetaModel.NAME;
			break;
		case 4:
		case 6:
			name = Nao2AgentMetaModel.NAME;
			break;
		case 2:
		case 5:
		default:
			name = NaoAgentMetaModel.NAME;
			break;
		}

		return ROBOT_MODELS.get(name).create();
	}

	public static RoboCupAgentMetaModel getAgentMetaModel(String robotModel)
	{
		switch (robotModel) {
		case Nao1AgentMetaModel.NAME:
			return Nao1AgentMetaModel.INSTANCE;
		case Nao2AgentMetaModel.NAME:
			return Nao2AgentMetaModel.INSTANCE;
		case Nao3AgentMetaModel.NAME:
			return Nao3AgentMetaModel.INSTANCE;
		case NaoToeAgentMetaModel.NAME:
			return NaoToeAgentMetaModel.INSTANCE;
		default:
			return NaoAgentMetaModel.INSTANCE;
		}
	}
}
