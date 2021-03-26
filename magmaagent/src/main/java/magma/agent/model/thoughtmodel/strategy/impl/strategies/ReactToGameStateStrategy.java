/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.model.thoughtmodel.strategy.impl.strategies;

import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;
import magma.agent.model.thoughtmodel.strategy.impl.roles.BallAcceptor;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Ballguard;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Ballguard.BallguardPosition;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Defender;
import magma.agent.model.thoughtmodel.strategy.impl.roles.DefensiveMidfielder;
import magma.agent.model.thoughtmodel.strategy.impl.roles.FieldArea;
import magma.agent.model.thoughtmodel.strategy.impl.roles.ManToManMarker;
import magma.agent.model.thoughtmodel.strategy.impl.roles.OpenSpaceSeeker;
import magma.agent.model.thoughtmodel.strategy.impl.roles.ReactToGameState;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Role;
import magma.agent.model.thoughtmodel.strategy.impl.roles.Wing;

public class ReactToGameStateStrategy extends BaseStrategy
{
	public static final String NAME = "ReactToGameState";

	public ReactToGameStateStrategy(IRoboCupThoughtModel thoughtModel)
	{
		super(NAME, thoughtModel);

		// acceptor for passes
		availableRoles.add(
				new OpenSpaceSeeker(new BallAcceptor("BallAcceptor", worldModel, 6.0f, 6.0f, 8f), thoughtModel));

		// defender
		Role defender = new Defender("Defender", worldModel, 7.0f);

		// defensive midfielder
		Role defensiveMidfielder = new DefensiveMidfielder("DefensiveMidfielder", worldModel, 1.5f, 6.0f);

		// man-to-man marker
		availableRoles.add(new ManToManMarker(defender, FieldArea.UPPER));
		availableRoles.add(new ManToManMarker(defensiveMidfielder, FieldArea.LOWER));

		// wing
		availableRoles.add(new OpenSpaceSeeker(new Wing("Wing", worldModel, 12.0f, 5.0f), thoughtModel));

		// guards
		Role leftBallguard = new Ballguard("LeftBallguard", worldModel, BallguardPosition.LEFT, 1.5f, 2.0f, 4f);
		Role rightBallguard = new Ballguard("RightBallguard", worldModel, BallguardPosition.RIGHT, 1.5f, 2.0f, 4f);
		Role leftSupportguard = new Ballguard("LeftSupportguard", worldModel, BallguardPosition.LEFT, 5.0f, 0.0f, 2.0f);
		Role rightSupportguard =
				new Ballguard("RightSupportguard", worldModel, BallguardPosition.RIGHT, 5.0f, 0.0f, 2.0f);
		Role centerBallguard = new Ballguard("CenterBallguard", worldModel, BallguardPosition.CENTER, 2.3f, 1.5f, 1.0f);
		Role centerSupportguard =
				new Ballguard("CenterSupportguard", worldModel, BallguardPosition.CENTER, 3.5f, 1f, 3.0f);

		// react to GameState
		availableRoles.add(new ReactToGameState(leftBallguard, thoughtModel, FieldArea.LOWER));
		availableRoles.add(new ReactToGameState(rightBallguard, thoughtModel, FieldArea.UPPER));
		availableRoles.add(new ReactToGameState(leftSupportguard, thoughtModel, FieldArea.LOWER));
		availableRoles.add(new ReactToGameState(rightSupportguard, thoughtModel, FieldArea.UPPER));
		availableRoles.add(new ReactToGameState(centerBallguard, thoughtModel, FieldArea.LOWER));
		availableRoles.add(new ReactToGameState(centerSupportguard, thoughtModel, FieldArea.UPPER));
	}
}
