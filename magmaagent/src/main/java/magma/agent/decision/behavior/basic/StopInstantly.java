/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.decision.behavior.IBehavior;
import hso.autonomy.agent.model.thoughtmodel.IThoughtModel;
import magma.agent.decision.behavior.IBehaviorConstants;

/**
 * Stops the previous behavior instantly by aborting it and resets all joints.
 */
public class StopInstantly extends StopBehavior
{
	public StopInstantly(IThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.STOP_INSTANTLY, thoughtModel);
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		actualBehavior.abort();
		return this;
	}
}
