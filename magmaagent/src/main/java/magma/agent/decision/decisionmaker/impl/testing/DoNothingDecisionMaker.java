/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl.testing;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import magma.agent.decision.decisionmaker.impl.SoccerDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * Basic decision making class used for testing mainly that just beams and then
 * stands still
 *
 * @author Klaus Dorer
 */
public class DoNothingDecisionMaker extends SoccerDecisionMaker
{
	public DoNothingDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		super(behaviors, thoughtModel);
	}

	/**
	 * Called to decide if we should wait for game start or not. Default
	 * implementation waits.
	 * @return the behavior to execute, null if this part of decision does not
	 *         trigger a behavior currently
	 */
	@Override
	protected String waitForGameStart()
	{
		// this makes sure that after beaming we do nothing
		return IBehavior.NONE;
	}

	@Override
	protected String performFocusBall()
	{
		return null;
	}

	@Override
	protected String performSay()
	{
		return null;
	}
}
