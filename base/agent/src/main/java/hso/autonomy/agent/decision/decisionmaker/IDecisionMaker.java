/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.decision.decisionmaker;

import hso.autonomy.agent.decision.behavior.IBehavior;

/**
 * Interface to decision making modules
 */
public interface IDecisionMaker {
	/**
	 * Called to take a decision on what to do next. The effect of the decision
	 * will be that one or more behaviors are performed
	 * @return true if there was a decision for a behavior
	 */
	boolean decide();

	/**
	 * @param name the name of the behavior as specified in IBehavior
	 * @return the specified behavior, null if not existing
	 */
	IBehavior getBehavior(String name);

	/**
	 * @return the behavior that has been selected most recently
	 */
	IBehavior getCurrentBehavior();

	/**
	 * @return the behavior that is planned to switch to
	 */
	IBehavior getDesiredBehavior();

	/**
	 * Resets the internal state of the decision maker.
	 */
	void reset();
}
