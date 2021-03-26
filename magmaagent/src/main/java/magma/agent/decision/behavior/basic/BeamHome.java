/* Copyright 2008 - 2021 Hochschule Offenburg
 * For a list of authors see README.md
 * This software of HSOAutonomy is released under GPL-3 License (see gpl.txt).
 */

package magma.agent.decision.behavior.basic;

import hso.autonomy.agent.decision.behavior.IBehavior;
import magma.agent.decision.behavior.IBehaviorConstants;
import magma.agent.model.thoughtmodel.IRoboCupThoughtModel;

/**
 * This Behavior is to beam the agent somewhere to the field
 *
 * @author Ingo Schindler
 */
public class BeamHome extends BeamToPosition
{
	public BeamHome(IRoboCupThoughtModel thoughtModel)
	{
		super(IBehaviorConstants.BEAM_HOME, thoughtModel);
	}

	@Override
	public void perform()
	{
		// the position is based on if we play left to right or right to left
		setPose(getThoughtModel().getHomePose());

		super.perform();

		// Perform initial pose
		getAgentModel().getFutureBodyModel().performInitialPose();
	}

	@Override
	public IBehavior switchFrom(IBehavior actualBehavior)
	{
		// the beam behavior has the highest decision priority, therefore either
		// leave or abort the actual behavior
		if (actualBehavior.isFinished()) {
			actualBehavior.onLeavingBehavior(this);
		} else {
			actualBehavior.abort();
		}

		return this;
	}
}
