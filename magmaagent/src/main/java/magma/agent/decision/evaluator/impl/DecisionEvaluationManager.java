/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.evaluator.impl;

import hso.autonomy.agent.decision.decisionmaker.IDecisionMaker;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import magma.agent.decision.evaluator.IDecisionEvaluator;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * Evaluator composite
 * @author kdorer
 */
public class DecisionEvaluationManager implements IDecisionEvaluator, Serializable
{
	/** all evaluators that should be triggered */
	private List<IDecisionEvaluator> evaluators;

	public DecisionEvaluationManager(IDecisionMaker decisionMaker, IRoboCupThoughtModel thoughtModel)
	{
		evaluators = new ArrayList<>();

		// comment all these in normal games
		// evaluators.add(new KickEvaluator(decisionMaker, thoughtModel));
		// evaluators.add(new FallEvaluator(decisionMaker, thoughtModel));
	}

	@Override
	public void evaluate()
	{
		for (IDecisionEvaluator evaluator : evaluators) {
			evaluator.evaluate();
		}
	}
}
