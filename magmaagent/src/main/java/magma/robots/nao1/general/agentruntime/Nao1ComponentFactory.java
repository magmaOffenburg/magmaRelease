/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.nao1.general.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import kdo.util.parameter.ParameterMap;
import magma.agent.decision.behavior.IBaseWalk;
import magma.agent.decision.behavior.ikMovement.IKWalkBehavior;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.nao1.decision.behavior.ikMovement.walk.IKWalkMovementParametersNao1;
import magma.robots.nao1.decision.behavior.movement.GetUpFromBackParametersNao1;
import magma.robots.nao1.model.agentmeta.Nao1AgentMetaModel;

public class Nao1ComponentFactory extends NaoComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return Nao1AgentMetaModel.INSTANCE;
	}

	@Override
	protected IBaseWalk createWalk(IRoboCupThoughtModel thoughtModel, ParameterMap params, BehaviorMap behaviors)
	{
		return (IBaseWalk) behaviors.put(new IKWalkBehavior(thoughtModel, params, behaviors));
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();

		result.put(IK_WALK_STEP, new IKWalkMovementParametersNao1());
		result.put(IK_WALK, new IKWalkMovementParametersNao1());
		result.put(IK_MOVEMENT, new IKWalkMovementParametersNao1());
		result.put(STABILIZE.BASE_NAME, new IKWalkMovementParametersNao1());
		result.put(GET_UP_BACK, new GetUpFromBackParametersNao1());

		return result;
	}
}
