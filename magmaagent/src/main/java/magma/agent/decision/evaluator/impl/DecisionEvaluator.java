/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.evaluator.impl;

import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import magma.agent.decision.evaluator.IDecisionEvaluator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

public abstract class DecisionEvaluator implements IDecisionEvaluator
{
	protected IDecisionMaker decisionMaker;
	protected IRoboCupThoughtModel thoughtModel;

	public DecisionEvaluator(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		this.decisionMaker = decisionMaker;
		this.thoughtModel = thoughtModel;
	}
}
