/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao3.general.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.ikMovement.IKWalkBehavior;
import magma.agent.decision.behavior.ikMovement.walk.IKDynamicWalkMovement;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao3.decision.behavior.ikMovement.walk.IKWalkMovementParametersNao3;
import magma.robots.nao3.decision.behavior.movement.GetUpFromBackParametersNao3;
import magma.robots.nao3.model.agentmeta.Nao3AgentMetaModel;

public class Nao3ComponentFactory extends NaoComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return Nao3AgentMetaModel.INSTANCE;
	}

	@Override
	protected IBaseWalk createWalk(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		params.put(IKDynamicWalkMovement.NAME, new IKWalkMovementParametersNao3());
		return (IBaseWalk) behaviors.put(new IKWalkBehavior(thoughtModel, params, behaviors));
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();
		result.put(GET_UP_BACK, new GetUpFromBackParametersNao3());
		return result;
	}
}
