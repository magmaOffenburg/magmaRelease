/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.decision.behavior;

/**
 * Interface to access all behaviors of the agent
 */
public interface IBehavior {
	/** Name for the none behavior */
	String NONE = "None";

	/**
	 * Reinitializes the Behavior-StateMachine (if present)
	 */
	void init();

	/**
	 * Called to perform the behavior
	 */
	void perform();

	/**
	 * Retrieve the behavior name
	 *
	 * @return Behavior name
	 */
	String getName();

	/**
	 * Check if this behavior is finished performing
	 *
	 * @return True if finished, false if not
	 */
	boolean isFinished();

	/**
	 * Called when to stop this behavior without caring for stopping it softly
	 */
	void abort();

	/**
	 * Decide if it is possible to switch to this behavior from the current
	 * behavior.
	 *
	 * @param actualBehavior the currently/actually performed behavior
	 * @return This behavior if it is possible to switch to this behavior,
	 *         otherwise the current behavior
	 */
	IBehavior switchFrom(IBehavior actualBehavior);

	/**
	 * Called to notify this behavior that is no longer performed and replaced by
	 * the new behavior.
	 *
	 * @param newBehavior the new behavior which will be performed after this
	 *        behavior
	 */
	void onLeavingBehavior(IBehavior newBehavior);

	/**
	 * Called if we decided again for that behavior
	 */
	void stayIn();

	/**
	 * Retrieve the number of times this behavior has been performed/initialized
	 *
	 * @return Number of performs
	 */
	int getPerforms();

	/**
	 * @return the number of cycles this behavior has been performed in total
	 */
	int getPerformedCycles();

	/**
	 * @return the number of consecutive performs without interruption
	 */
	int getConsecutivePerforms();

	/**
	 * A call does <b><u>not</u></b> have side effects. But a call before
	 * decideNextBasicBehavior will result in the old current behavior in case of
	 * Complex behaviors.
	 * @return the underlying basic behavior that will be performed.
	 */
	IBehavior getRootBehavior();

	/**
	 * Called before the decision maker decides.
	 */
	void preDecisionUpdate();
}
