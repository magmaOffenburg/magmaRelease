/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.misc;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.util.List;
import magma.agent.decision.behavior.IBehaviorConstants;

public class PassingChallengeAttack extends Attack
{
	public PassingChallengeAttack(IThoughtModel thoughtModel, BehaviorMap behaviors, List<String> availableKicks)
	{
		super(IBehaviorConstants.PASSING_CHALLENGE_ATTACK, thoughtModel, behaviors, availableKicks);
	}

	@Override
	protected float getKickPower()
	{
		return 0.5f;
	}
}
