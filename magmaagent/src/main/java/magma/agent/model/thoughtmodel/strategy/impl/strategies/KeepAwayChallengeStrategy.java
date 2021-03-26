/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.impl.roles.KeepAwayChallengeBallAcceptor;

public class KeepAwayChallengeStrategy extends BaseStrategy
{
	public static final String NAME = "KeepAwayChallenge";

	public KeepAwayChallengeStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		availableRoles.add(new KeepAwayChallengeBallAcceptor("Acceptor1", 1, thoughtModel));
		availableRoles.add(new KeepAwayChallengeBallAcceptor("Acceptor2", 1, thoughtModel));
	}
}
