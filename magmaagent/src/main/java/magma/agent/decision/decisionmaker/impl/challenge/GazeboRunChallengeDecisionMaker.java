/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.challenge;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.util.geometry.Angle;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.decision.behavior.IWalk;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class GazeboRunChallengeDecisionMaker extends SoccerDecisionMaker
{
	public GazeboRunChallengeDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	@Override
	protected String performFocusBall()
	{
		return null;
	}

	@Override
	protected String beamHome()
	{
		return null;
	}

	@Override
	protected String getUp()
	{
		// challenge is over when we fall anyway
		return null;
	}

	@Override
	protected String getReady()
	{
		// our getReady doesn't work in Gazebo yet
		return null;
	}

	@Override
	protected String move()
	{
		IWalk walk = (IWalk) behaviors.get(IBehaviorConstants.WALK);
		walk.walk(50, 0, Angle.ZERO);
		return walk.getName();
	}
}
