/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.decisionmaker.impl;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public class SoccerGoalieDecisionMaker implements IDecisionMaker
{
	private IDecisionMaker soccerDecisionMaker;
	private IDecisionMaker goalieDecisionMaker;

	private IDecisionMaker decisionMaker;

	private IRoboCupThoughtModel thoughtModel;

	public SoccerGoalieDecisionMaker(BehaviorMap behaviors, IRoboCupThoughtModel thoughtModel)
	{
		soccerDecisionMaker = new SoccerDecisionMaker(behaviors, thoughtModel);
		goalieDecisionMaker = new GoalieDecisionMaker(behaviors, thoughtModel);

		decisionMaker = soccerDecisionMaker;

		this.thoughtModel = thoughtModel;
	}

	@Override
	public boolean decide()
	{
		if (thoughtModel.shouldReplaceGoalie()) {
			decisionMaker = goalieDecisionMaker;
		} else {
			decisionMaker = soccerDecisionMaker;
		}

		return decisionMaker.decide();
	}

	@Override
	public IBehavior getBehavior(String name)
	{
		return decisionMaker.getBehavior(name);
	}

	@Override
	public IBehavior getCurrentBehavior()
	{
		return decisionMaker.getCurrentBehavior();
	}

	@Override
	public IBehavior getDesiredBehavior()
	{
		return decisionMaker.getDesiredBehavior();
	}

	@Override
	public void reset()
	{
		soccerDecisionMaker.reset();
		goalieDecisionMaker.reset();
	}
}