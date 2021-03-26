/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package hso.autonomy.agent.decision.behavior.complex;

import hso.autonomy.agent.decision.behavior.BehaviorMap;
import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import java.util.Collections;
import java.util.List;

/**
 * Convenience class for complex behaviors which decide only for a single next
 * behavior.
 *
 * @see ComplexBehavior
 * @author Stefan Glaser
 */
public abstract class SingleComplexBehavior extends ComplexBehavior
{
	public SingleComplexBehavior(
			String name, IThoughtModel thoughtModel, BehaviorMap behaviors, String defaultBehaviorName)
	{
		super(name, thoughtModel, behaviors, defaultBehaviorName);
	}

	public SingleComplexBehavior(String name, IThoughtModel thoughtModel, BehaviorMap behaviors)
	{
		super(name, thoughtModel, behaviors);
	}

	@Override
	protected List<IBehavior> decideNextBasicBehaviors()
	{
		return Collections.singletonList(decideNextBasicBehavior());
	}

	/**
	 * Decide for a single next behavior.
	 */
	protected abstract IBehavior decideNextBasicBehavior();
}
