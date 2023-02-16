/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.impl.formations.PenaltyKickerFormation;
import magma.agent.model.thoughtmodel.strategy.impl.roles.DummyRole;

public class PenaltyKickerStrategy extends BaseStrategy
{
	public static final String NAME = "PenaltyKicker";

	public PenaltyKickerStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		ownKickOffFormation = new PenaltyKickerFormation();
		// probably not needed, just to be safe
		opponentKickOffFormation = new PenaltyKickerFormation();

		availableRoles.add(DummyRole.INSTANCE);
	}
}
