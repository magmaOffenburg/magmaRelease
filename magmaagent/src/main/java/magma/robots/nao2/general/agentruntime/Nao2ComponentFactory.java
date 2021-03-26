/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao2.general.agentruntime;

import kdo.util.parameter.ParameterMap;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao2.decision.behavior.movement.GetUpFromBackParametersNao2;
import magma.robots.nao2.model.agentmeta.Nao2AgentMetaModel;

public class Nao2ComponentFactory extends NaoComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return Nao2AgentMetaModel.INSTANCE;
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();
		result.put(GET_UP_BACK, new GetUpFromBackParametersNao2());
		return result;
	}
}
