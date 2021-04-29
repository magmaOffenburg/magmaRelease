/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.complex.walk;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.complex.SingleComplexBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import hso.autonomy.util.geometry.Angle;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;

/**
 * Search for the ball
 * @author Maximilian Kroeg
 */
public class SearchBall extends SingleComplexBehavior
{
	public SearchBall(IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(IBehaviorConstants.SEARCH_BALL, thoughtModel, behaviors);
	}

	@Override
	public IBehavior decideNextBasicBehavior()
	{
		IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
		walk.walk(0, 0, Angle.deg(60));
		return walk;
	}
}
