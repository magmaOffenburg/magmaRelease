/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.challenge;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.impl.KickPositionProfiler;
import magma.agent.model.thoughtmodel.impl.PassingChallengeKickPositionProfiler;
import magma.agent.model.thoughtmodel.strategy.impl.RoleManager;
import magma.agent.model.thoughtmodel.strategy.impl.strategies.PassingChallengeStrategy;

public class PassingChallengeDecisionMaker extends SoccerDecisionMaker
{
	public PassingChallengeDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
		KickPositionProfiler kickPositionProfiler = new PassingChallengeKickPositionProfiler(thoughtModel);
		thoughtModel.setKickPositionProfiler(kickPositionProfiler);

		thoughtModel.setRoleManager(new RoleManager(getWorldModel(), new PassingChallengeStrategy(thoughtModel)));
	}

	protected String move()
	{
		if (getThoughtModel().isClosestToBall()) {
			return IBehaviorConstants.PASSING_CHALLENGE_ATTACK;
		}
		return null;
	}
}
