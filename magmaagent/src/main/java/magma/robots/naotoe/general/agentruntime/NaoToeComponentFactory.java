/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.robots.naotoe.general.agentruntime;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import java.util.List;
import kdo.util.parameter.ParameterMap;
import magma.agent.model.agentmeta.IRoboCupAgentMetaModel;
import magma.robots.nao.decision.behavior.movement.fullsearch.kickwalk.ParameterListComposite;
import magma.robots.nao.general.agentruntime.NaoComponentFactory;
import magma.robots.naotoe.decision.behavior.movement.GetUpFromBackParametersToe;
import magma.robots.naotoe.decision.behavior.movement.Kick11mParametersToe;
import magma.robots.naotoe.decision.behavior.movement.Kick8mParametersToe;
import magma.robots.naotoe.decision.behavior.movement.kickwalk.KickWalkBackwardParametersToe;
import magma.robots.naotoe.decision.behavior.movement.kickwalk.KickWalkStandingParametersToe;
import magma.robots.naotoe.model.agentmeta.NaoToeAgentMetaModel;

public class NaoToeComponentFactory extends NaoComponentFactory
{
	@Override
	public IRoboCupAgentMetaModel getAgentMetaModel()
	{
		return NaoToeAgentMetaModel.INSTANCE;
	}

	@Override
	protected List<String> createDefaultAvailableKicks(BehaviorMap behaviors)
	{
		List<String> kicks = super.createDefaultAvailableKicks(behaviors);
		// addKick(behaviors, kicks, IBehaviorConstants.KICK_WALK_STRAIGHT_SIDE);
		return kicks;
	}

	@Override
	protected ParameterMap createSpecificParameters()
	{
		ParameterMap result = super.createSpecificParameters();

		result.put(KICK_8M.KICK.BASE_NAME, new Kick8mParametersToe());
		result.put(KICK_11M.KICK.BASE_NAME, new Kick11mParametersToe());
		result.put(KICK_WALK_STANDING.BASE_NAME,
				ParameterListComposite.fromSingle(KickWalkStandingParametersToe.instance()));
		result.put(KICK_WALK_BACKWARD.BASE_NAME,
				ParameterListComposite.fromSingle(KickWalkBackwardParametersToe.instance()));
		// for learning only
		result.put(GET_UP_BACK, new GetUpFromBackParametersToe());

		return result;
	}
}
