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

/**
 * @author Klaus Dorer
 */
public class RunChallengeDecisionMaker extends SoccerDecisionMaker
{
	public RunChallengeDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	/**
	 * Called to decide if we should beam us to our home position
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	@Override
	protected String beamHome()
	{
		// run challenge does not beam
		return null;
	}

	/**
	 * Called to decide if movement is necessary
	 * @return walk behavior if no team mate is closer to ball
	 */
	@Override
	protected String move()
	{
		if (numberOfDecisions < 50) {
			// we have to wait the first 50 cycles
			return null;
		}

		((IWalk) behaviors.get(IBehaviorConstants.WALK)).walk(100, 0, Angle.ZERO);
		return IBehaviorConstants.WALK;
	}
}
