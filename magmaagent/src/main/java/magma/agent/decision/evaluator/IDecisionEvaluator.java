/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.evaluator;

/**
 * @author kdorer
 *
 */
public interface IDecisionEvaluator {
	/**
	 * Called to evaluate the current state of the agent
	 */
	void evaluate();
}
