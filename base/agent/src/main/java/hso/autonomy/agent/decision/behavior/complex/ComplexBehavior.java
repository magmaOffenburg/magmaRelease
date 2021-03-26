/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.decision.behavior.complex;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.decision.behavior.basic.Behavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all behaviors that use a couple of other behaviors. <br>
 * A complex behavior is typically used as a decision behavior, instrumenting
 * other behaviors in order to fulfill a more complex and abstract task. For
 * this purpose, a complex behavior supports switching to a list of alternative
 * behaviors in each cycle, which are provided and parametrized by the
 * {@link #decideNextBasicBehaviors()} method.<br>
 * <br>
 * Example: A simple example is the WalkToPosition complex behavior, which is
 * instrumenting the underlying walk behavior in order to walk a path to the
 * intended position.
 *
 * @author Klaus Dorer, Stefan Glaser
 */
public abstract class ComplexBehavior extends Behavior
{
	/** the behaviors that are available */
	protected transient final BehaviorMap behaviors;

	/** the behavior that was last selected to perform */
	private IBehavior currentBehavior;

	/** the behavior to which we initialize */
	private transient final IBehavior defaultBehavior;

	public ComplexBehavior(String name, IThoughtModel thoughtModel, BehaviorMap behaviors, String defaultBehaviorName)
	{
		super(name, thoughtModel);
		this.behaviors = behaviors;
		this.defaultBehavior = behaviors.get(defaultBehaviorName);
		currentBehavior = defaultBehavior;
	}

	public ComplexBehavior(String name, IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		this(name, thoughtModel, behaviors, IBehavior.NONE);
	}

	@Override
	public void perform()
	{
		super.perform();

		List<IBehavior> possibleBehaviors = decideNextBasicBehaviors();
		IBehavior newBehavior = currentBehavior;

		for (IBehavior toExecute : possibleBehaviors) {
			if (toExecute == currentBehavior) {
				// stop try to switch if we reach a behavior that is in execution
				newBehavior = currentBehavior;
				break;
			} else {
				// we prefer to switch to another behavior, so try to do so
				newBehavior = toExecute.switchFrom(currentBehavior);
				if (newBehavior == toExecute) {
					break;
				}
			}
		}

		if (currentBehavior == newBehavior) {
			currentBehavior.stayIn();
		} else {
			currentBehavior = newBehavior;
		}

		currentBehavior.perform();
	}

	/**
	 * Decide for a list of possible next behaviors, sorted from the most to the
	 * least preferred behavior.
	 *
	 * @return next basic behaviors
	 */
	protected abstract List<IBehavior> decideNextBasicBehaviors();

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		// decide for the next behaviors which would be performed in this complex
		// behavior
		List<IBehavior> possibleBehaviors = decideNextBasicBehaviors();

		for (IBehavior toExecute : possibleBehaviors) {
			// if this complex behavior decides to perform a behavior that is
			// actually in execution, directly switch to this behavior
			if (isBehaviorInExecution(toExecute, actualBehavior)) {
				if (actualBehavior != toExecute) {
					// notify the actual behavior that it is replaced with our
					// decision
					actualBehavior.onLeavingBehavior(toExecute);
				}

				// initialize this behavior
				currentBehavior = toExecute;
				return this;
			}

			// if we decided for a behavior, which is not actually performed
			// already, ask our decision if it is capable to switch from the actual
			// behavior
			IBehavior newB = toExecute.switchFrom(actualBehavior);

			// if the behavior was able to switch, return this behavior, otherwise
			// stick to the actual behavior
			if (newB == toExecute) {
				return this;
			}
		}

		return actualBehavior;
	}

	@Override
	public void onLeavingBehavior(IBehavior newBehavior)
	{
		if (this == newBehavior) {
			// just to make sure to not leave the new behavior
			return;
		}

		// only forward leaving notification to the current behavior if it is not
		// the new behavior
		if (currentBehavior != null && currentBehavior != newBehavior) {
			currentBehavior.onLeavingBehavior(newBehavior);
		}

		super.onLeavingBehavior(newBehavior);
	}

	@Override
	public void abort()
	{
		getCurrentBehavior().abort();
		super.abort();
	}

	public IBehavior getCurrentBehavior()
	{
		return currentBehavior;
	}

	@Override
	public boolean isFinished()
	{
		return currentBehavior.isFinished();
	}

	@Override
	public String toString()
	{
		return super.toString() + " > " + currentBehavior.toString();
	}

	@Override
	public void init()
	{
		super.init();
		// we have to initialize the current behavior. Otherwise on the next
		// invocation of this complex behavior the old currentBehavior will be
		// asked if it is finished which does not make sense.
		if (behaviors != null) {
			currentBehavior = defaultBehavior;
		}
	}

	/**
	 * @return the root of the currentBehavior
	 */
	@Override
	public IBehavior getRootBehavior()
	{
		return currentBehavior.getRootBehavior();
	}

	/**
	 * Checks if the testee behavior is in execution by the referenceBehavior.
	 * The check is performed recursively along the chain of currently performed
	 * behaviors in the referenceBehavior.
	 *
	 * @param testee the behavior we are looking for
	 * @param referenceBehavior the reference behavior in which we search for the
	 *        testee
	 * @return true if the testee behavior is currently in execution by the
	 *         referenceBehavior, false otherwise
	 */
	public static boolean isBehaviorInExecution(IBehavior testee, IBehavior referenceBehavior)
	{
		IBehavior reference = referenceBehavior;

		// traverse down the behavior chain and search for the testee behavior
		while (reference != null && reference instanceof ComplexBehavior) {
			if (testee == reference) {
				return true;
			}

			// fetch the next child behavior
			reference = ((ComplexBehavior) reference).getCurrentBehavior();
		}

		// if we reached a basic behavior, make the final check
		return reference == testee;
	}

	/**
	 * Retrieve the chain of behavior that are currently performed, starting with
	 * the given referenceBehavior.
	 *
	 * @param referenceBehavior the top level behavior to expand
	 * @return the list of currently performed behaviors of the referenceBehavior
	 */
	public static List<IBehavior> getCurrentBehaviorChain(IBehavior referenceBehavior)
	{
		List<IBehavior> chain = new ArrayList<>();
		IBehavior reference = referenceBehavior;

		// traverse down the behavior chain and search for the testee behavior
		while (reference != null && reference instanceof ComplexBehavior) {
			chain.add(reference);

			// fetch the next child behavior
			reference = ((ComplexBehavior) reference).getCurrentBehavior();
		}

		// if we reached a basic behavior, add it and return the chain
		if (reference != null) {
			chain.add(reference);
		}

		return chain;
	}
}
