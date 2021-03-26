/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.impl.formations.PassingChallengeFormation;
import magma.agent.model.thoughtmodel.strategy.impl.roles.BallAcceptor;

public class PassingChallengeStrategy extends BaseStrategy
{
	public static final String NAME = "PassingChallenge";

	public PassingChallengeStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		ownKickOffFormation = new PassingChallengeFormation();

		availableRoles.add(new BallAcceptor("dummy1", worldModel, 0.0f, 6.0f, 1.0f));
		availableRoles.add(new BallAcceptor("dummy2", worldModel, 0.0f, 6.0f, 2.0f));
		availableRoles.add(new BallAcceptor("dummy3", worldModel, 0.0f, 6.0f, 3.0f));
		availableRoles.add(new BallAcceptor("dummy4", worldModel, 0.0f, 6.0f, 4.0f));
	}
}
